package com.nicolasMasson.shoppingCart.service.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
            super(message);
        }
}
