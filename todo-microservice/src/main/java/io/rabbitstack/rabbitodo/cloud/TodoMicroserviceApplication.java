package io.rabbitstack.rabbitodo.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TodoMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoMicroserviceApplication.class, args);
    }
}
