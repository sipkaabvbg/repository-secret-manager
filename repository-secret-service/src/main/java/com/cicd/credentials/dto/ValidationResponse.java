package com.cicd.credentials.dto;

public record ValidationResponse( boolean valid,
                                  String message
) {}
