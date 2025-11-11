package com.form.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.form.system", "com.form.system.formatter"})
public class FormSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormSystemApplication.class, args);
    }

}