package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{
    List<Proyecto> findProyectoActivo(String nombre);

    @Query("SELECT p from Proyecto p WHERE p.fechaFin > :hoy")
    List<Proyecto> findProyectoActivo(@Param("hoy") LocalDate hoy);
}
