package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEstado(String estado);

    @Query("SELECT p.estado FROM Proyecto p WHERE p.id = :idProyecto")
    String findEstadoById(@Param("idProyecto") Long idProyecto);
}
