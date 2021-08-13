package com.example.demo.custom.exception;

public class EntityNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public EntityNotFoundException() {

    }
    public EntityNotFoundException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {

        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
    }
    public static long getSerialversionuid() {

        return serialVersionUID;
    }
}
