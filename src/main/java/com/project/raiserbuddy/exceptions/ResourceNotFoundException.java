package com.project.raiserbuddy.exceptions;


public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    String resourceName;
    String field;
    String fieldName;
    Integer fieldId;
    int code;

    public ResourceNotFoundException() {}


    public ResourceNotFoundException(String resourceName, String field, String fieldName, int code) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.code=code;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Integer fieldId, int code) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.code=code;
        this.field = field;
        this.fieldId = fieldId;
    }

   }