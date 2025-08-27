package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{
}
