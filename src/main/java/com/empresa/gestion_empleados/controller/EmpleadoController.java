package com.empresa.gestion_empleados.controller;

import com.empresa.gestion_empleados.entity.Empleado;
import com.empresa.gestion_empleados.exeptions.EmailDuplicadoException;
import com.empresa.gestion_empleados.exeptions.EmpleadoNoEncontradoException;
import com.empresa.gestion_empleados.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Empleado> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(empleadoService.findbyId(id));
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody @Validated Empleado empleado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.save(empleado));
        }catch (EmailDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            empleadoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id")
    public ResponseEntity<Empleado> update(@PathVariable Long id, @RequestBody Empleado empleado) {
        try {
            return ResponseEntity.ok(empleadoService.update(id, empleado));
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> findByNombreDepartamento(@PathVariable String nombreDepartamento) {
        try {
            return ResponseEntity.ok(empleadoService.findbyDepartamento(nombreDepartamento));
        }catch (Exception e)  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/salario_promedio/{depertamentoId}")
    public ResponseEntity<BigDecimal> getSalarioPromedio(@PathVariable Long depertamentoId) {
        return ResponseEntity.ok(empleadoService.obtenerSalarioPromedioPorDepartamento(depertamentoId));
    }

    @GetMapping("/rango-salario")
    public ResponseEntity<List<Empleado>> getRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax) {
        return ResponseEntity.ok(empleadoService.findbyRangoSalario(salarioMin, salarioMax));
    }
}
