package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository  extends JpaRepository<Empleado, Long> {
}
