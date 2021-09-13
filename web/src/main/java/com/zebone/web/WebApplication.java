package com.zebone.web;
//
//import net.hasor.spring.boot.EnableHasor;
//import net.hasor.spring.boot.EnableHasorWeb;
import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableHasor
@EnableHasorWeb
@SpringBootApplication(scanBasePackages = "com.zebone")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}
