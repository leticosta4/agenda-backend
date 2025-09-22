package com.engsw.agenda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Docker Spring Boot está funcionando!";
    }

     @GetMapping("/health/vida")
    public String health() {
        return "essa rota é da legalzinha até";
    }
}
