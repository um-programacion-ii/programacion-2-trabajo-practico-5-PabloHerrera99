package com.empresa.gestion_empleados.service;

import com.empresa.gestion_empleados.entity.Empleado;

import java.math.BigDecimal;
import java.util.List;

public interface EmpleadoService {
    Empleado save(Empleado empleado);
    Empleado findbyId(Long id);
    List<Empleado> findbyDepartamento(String nombreDepartamento);
    List<Empleado> findbyRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax);
    BigDecimal obtenerSalarioPromedioPorDepartamento(Long departamentoId);
    List<Empleado> findAll();
    Empleado update(Long id, Empleado empleado);
    void delete(Long id);
}
