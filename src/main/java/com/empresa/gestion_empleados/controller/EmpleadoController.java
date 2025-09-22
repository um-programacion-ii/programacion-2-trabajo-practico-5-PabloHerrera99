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

    // Obtener todos los empleados
    // Método: GET /api/empleados
    // Retorna la lista completa de empleados registrados
    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    // Buscar un empleado por ID
    // Método: GET /api/empleados/{id}
    // Retorna el empleado si existe, o 404 si no se encuentra
    @GetMapping("{id}")
    public ResponseEntity<Empleado> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(empleadoService.findbyId(id));
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Crear un nuevo empleado
    // Método: POST /api/empleados
    // Recibe los datos de un empleado en el cuerpo de la petición
    // Retorna el empleado creado con estado 201 (CREATED)
    // Si el email ya existe, devuelve 400 (BAD REQUEST)
    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody @Validated Empleado empleado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.save(empleado));
        }catch (EmailDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Eliminar un empleado por su ID
    // Método: DELETE /api/empleados/{id}
    // Si el empleado existe, lo elimina y retorna 204 (NO CONTENT)
    // Si no existe, devuelve 404 (NOT FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            empleadoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Actualizar un empleado existente por su ID
    // Método: PUT /api/empleados/{id}
    // Si el empleado existe, lo actualiza y retorna el actualizado
    // Si no existe, devuelve 404 (NOT FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@PathVariable Long id, @RequestBody Empleado empleado) {
        try {
            return ResponseEntity.ok(empleadoService.update(id, empleado));
        }catch (EmpleadoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Buscar empleados por nombre de departamento
    // Método: GET /api/empleados/departamento/{nombreDepartamento}
    // Retorna la lista de empleados que pertenecen al departamento indicado
    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> findByNombreDepartamento(@PathVariable String nombreDepartamento) {
        try {
            return ResponseEntity.ok(empleadoService.findbyDepartamento(nombreDepartamento));
        }catch (Exception e)  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Obtener el salario promedio de un departamento
    // Método: GET /api/empleados/salario_promedio/{departamentoId}
    // Retorna el salario promedio de todos los empleados que pertenecen al departamento
    @GetMapping("/salario_promedio/{depertamentoId}")
    public ResponseEntity<BigDecimal> getSalarioPromedio(@PathVariable Long depertamentoId) {
        return ResponseEntity.ok(empleadoService.obtenerSalarioPromedioPorDepartamento(depertamentoId));
    }

    // Buscar empleados dentro de un rango de salario
    // Método: GET /api/empleados/rango-salario?salarioMin=###&salarioMax=###
    // Retorna todos los empleados cuyo salario esté dentro del rango indicado
    @GetMapping("/rango-salario")
    public ResponseEntity<List<Empleado>> getRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax) {
        return ResponseEntity.ok(empleadoService.findbyRangoSalario(salarioMin, salarioMax));
    }
}
