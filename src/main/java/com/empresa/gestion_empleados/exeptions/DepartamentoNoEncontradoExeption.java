package com.empresa.gestion_empleados.exeptions;

public class DepartamentoNoEncontradoExeption extends RuntimeException {
    public DepartamentoNoEncontradoExeption(Long id) {
        super("Departamento no encontrado con ID: " + id);
    }
}
