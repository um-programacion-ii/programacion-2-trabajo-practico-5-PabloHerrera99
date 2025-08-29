package com.empresa.gestion_empleados.service;

import com.empresa.gestion_empleados.entity.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto save(Proyecto proyecto);
    Proyecto update(Long id, Proyecto proyecto);
    void delete(Long id);
    Proyecto findById(Long id);
    List<Proyecto> findAll();


}
