package com.empresa.gestion_empleados.exeptions;

public class ProyectoNoEncotradoExeption extends RuntimeException {
    public ProyectoNoEncotradoExeption(Long id) {
        super("Proyecto no encontrado con ID: " + id);
    }
}
