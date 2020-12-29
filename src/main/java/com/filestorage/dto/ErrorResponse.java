package com.filestorage.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private String error;
}
