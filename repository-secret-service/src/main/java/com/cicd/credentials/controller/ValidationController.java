package com.cicd.credentials.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ValidationController.class);
    private final ValidationService service;

    public ValidationController(ValidationService service) {
        this.service = service;
    }

    @PostMapping
    public ValidationResponse validate(@RequestBody ValidationRequest request) {
        log.info("Received repository validation request for URL: {}",
                request.repoUrl());
        ValidationResponse response = service.validate(request.repoUrl(), request.secretId());
        log.info("Validation completed. Status result (isValid): {}", response.valid());
        return response;
    }}