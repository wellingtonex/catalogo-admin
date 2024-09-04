package com.fullcycle.admin.catalogo.infrastructure;


import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello World");
        SpringApplication.run(WebServerConfig.class);
    }
}