package com.cicd.credentials.controller;

import com.cicd.credentials.dto.ValidationRequest;
import com.cicd.credentials.dto.ValidationResponse;
import com.cicd.credentials.service.ValidationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validate")
public class ValidationController {

    private final ValidationService service;

    public ValidationController(ValidationService service) {
        this.service = service;
    }

    @PostMapping
    public ValidationResponse validate(@RequestBody ValidationRequest request) {
        return service.validate(request.repoUrl(), request.secretId());
    }}