package com.empresa.gestion_empleados.service;

import com.empresa.gestion_empleados.entity.Empleado;
import com.empresa.gestion_empleados.exeptions.EmailDuplicadoException;
import com.empresa.gestion_empleados.exeptions.EmpleadoNoEncontradoException;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import com.empresa.gestion_empleados.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               DepartamentoRepository departamentoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Empleado save(Empleado empleado) {
        if (empleadoRepository.findByEmail(empleado.getEmail()).isPresent()) {
            throw new EmailDuplicadoException(empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado findbyId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));
    }

    @Override
    public List<Empleado> findbyDepartamento(String nombreDepartamento) {
        return empleadoRepository.findByNombreDepartamento(nombreDepartamento);
    }

    @Override
    public List<Empleado> findbyRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax) {
        return empleadoRepository.findBySalarioBetween(salarioMin, salarioMax);
    }

    @Override
    public BigDecimal obtenerSalarioPromedioPorDepartamento(Long departamentoId) {
        return empleadoRepository.findAverageSalarioByDepartamento(departamentoId)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado update(Long id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException(id);
        }
        empleado.setId(id);
        return empleadoRepository.save(empleado);
    }

    @Override
    public void delete(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException(id);
        }
        empleadoRepository.deleteById(id);
    }
}
