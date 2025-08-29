package com.empresa.gestion_empleados.exeptions;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("El email ya está registrado: " + email);
    }
}
