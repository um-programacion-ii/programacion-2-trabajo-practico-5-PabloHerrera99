package com.empresa.gestion_empleados.exeptions;

public class EmpleadoNoEncontradoException extends RuntimeException {
    public EmpleadoNoEncontradoException(Long id) {
        super("Empleado no encontrado con ID: " + id);
    }
}
