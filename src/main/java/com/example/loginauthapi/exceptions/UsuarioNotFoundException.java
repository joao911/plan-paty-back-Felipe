package com.example.loginauthapi.exceptions;

public class UsuarioNotFoundException extends RuntimeException{

    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
