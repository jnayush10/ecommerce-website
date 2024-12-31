package com.ecommerce.application.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    long fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("Resource %s not found for field %s with name %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, long fieldId) {
        super(String.format("Resource %s not found for field %s with id %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException(){}
}
