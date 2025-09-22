package com.empresa.gestion_empleados.controller;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.exeptions.DepartamentoNoEncontradoExeption;
import com.empresa.gestion_empleados.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departamentos")
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    // Obtener todos los departamentos
    // Método: GET /api/departamentos
    // Retorna una lista con todos los departamentos registrados en el sistema
    @GetMapping
    public ResponseEntity<List<Departamento>> findAll() {
        return ResponseEntity.ok(departamentoService.findAll());
    }

    // Buscar un departamento por su ID
    // Método: GET /api/departamentos/{id}
    // Si el departamento existe, lo retorna
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(departamentoService.findById(id));
        } catch (DepartamentoNoEncontradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Crear un nuevo departamento
    // Método: POST /api/departamentos
    // Recibe un objeto Departamento en el cuerpo de la petición
    // Retorna el departamento creado junto con estado 201 (CREATED)
    @PostMapping
    public ResponseEntity<Departamento> create(@RequestBody Departamento departamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.save(departamento));
    }


    // Actualizar un departamento existente por su ID
    // Método: PUT /api/departamentos/{id}
    // Si el departamento existe, lo actualiza y retorna el actualizado
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> update(@PathVariable Long id, @RequestBody Departamento departamento) {
        try {
            return ResponseEntity.ok(departamentoService.update(id, departamento));
        }catch (DepartamentoNoEncontradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Eliminar un departamento por su ID
    // Método: DELETE /api/departamentos/{id}
    // Si el departamento existe, lo elimina y devuelve estado 204 (NO CONTENT)
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            departamentoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (DepartamentoNoEncontradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
