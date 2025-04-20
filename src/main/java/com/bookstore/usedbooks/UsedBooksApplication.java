package com.bookstore.usedbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Indique que c'est une application Spring Boot et active la configuration automatique , Annotation qui combine @Configuration, @EnableAutoConfiguration et @ComponentScan.

public class UsedBooksApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsedBooksApplication.class, args);//
    }
}

