package com.example.demo.custom.exception;

public class ServiceLayerException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public ServiceLayerException() {

    }
    public ServiceLayerException(String errorMessage) {
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
