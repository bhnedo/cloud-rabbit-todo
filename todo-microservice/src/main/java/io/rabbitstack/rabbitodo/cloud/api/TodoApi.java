package io.rabbitstack.rabbitodo.cloud.api;

import io.rabbitstack.rabbitodo.cloud.data.Task;
import io.rabbitstack.rabbitodo.cloud.data.TaskPriority;
import io.rabbitstack.rabbitodo.cloud.data.Todo;
import io.rabbitstack.rabbitodo.cloud.data.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The REST controller which exposes the API for todo lists. The <code>TodoRepository</code>
 * mongo repository provides the operations for manipulating and listing
 * of todos and it's associated tasks.
 */

@RestController
public class TodoApi {

    @Autowired
    private TodoRepository todoRepository;

    /**
     * Gets all todo lists.
     * @return a JSON array of todo lists
     */
    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public List<Todo> todos() {
        return todoRepository.findAll();
    }

    /**
     * Gets todo list by the identifier.
     * @param id todo list identifier
     * @return a JSON representation of todo list
     */
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.GET)
    public Todo todo(@PathVariable String id) {
        return todoRepository.findOne(id);
    }

    /**
     * Creates a new todo list.
     * @param todo list to be created
     */
    @RequestMapping(value = "/todos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createTodo(@Valid @RequestBody Todo todo) {
        todo.setCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        todo.getTasks().forEach(
                task -> task.setId(UUID.randomUUID().toString())
        );
        todoRepository.insert(todo);
    }

    /**
     * Removes a todo list.
     * @param id Identifier of a todo list to be removed.
     */
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable String id) {
        todoRepository.delete(id);
    }

    /**
     * Marks a specified task as done.
     * @param todoId Identifier of a toddoneo list
     * @param taskId Identifier of the task which pertains to a todo list.
     */
    @RequestMapping(value = "/todos/{todoId}/tasks/{taskId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTaskDone(@PathVariable String todoId, @PathVariable String taskId) {
        Todo todo = todoRepository.findOne(todoId);
        if ( todo != null ) {
            Task task =todo.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .get();
            task.setDone(true);
            task.setFinishedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        }
        todoRepository.save(todo);
    }

    /**
     * Changes a task priority.
     * @param todoId Todo list identifier
     * @param taskId Task identifier
     * @param priority New task priority
     */
    @RequestMapping(value = "/todos/{todoId}/tasks/{taskId}/priority/{priority}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeTaskPriority(@PathVariable String todoId, @PathVariable String taskId, @PathVariable TaskPriority priority) {
        Todo todo = todoRepository.findOne(todoId);
        if (todo != null) {
            todo.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .get().setPriority(priority);
        }
        todoRepository.save(todo);
    }

}
