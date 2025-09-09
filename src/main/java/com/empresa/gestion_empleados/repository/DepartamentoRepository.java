package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
