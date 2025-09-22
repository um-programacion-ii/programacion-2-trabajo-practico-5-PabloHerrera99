package com.empresa.gestion_empleados.controller;

import com.empresa.gestion_empleados.entity.Proyecto;
import com.empresa.gestion_empleados.exeptions.ProyectoNoEncotradoExeption;
import com.empresa.gestion_empleados.service.ProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/proyectos")
public class ProyectoController {
    private ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // Obtener todos los proyectos
    // Método: GET /api/proyectos
    // Retorna una lista con todos los proyectos registrados en el sistema
    @GetMapping
    public ResponseEntity<List<Proyecto>> findAll() {
        return ResponseEntity.ok(proyectoService.findAll());
    }

    // Buscar un proyecto por su ID
    // Método: GET /api/proyectos/{id}
    // Si el proyecto existe, retorna el proyecto correspondiente
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(proyectoService.findById(id));
        }catch (ProyectoNoEncotradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Buscar proyectos por estado
    // Método: GET /api/proyectos/estado/{status}
    // Retorna una lista de proyectos que coinciden con el estado proporcionado
    @GetMapping("estado/{status}")
    public ResponseEntity<List<Proyecto>> findByStatus(@PathVariable String status){
        return ResponseEntity.ok(proyectoService.findByStatus(status));
    }

    // Crear un nuevo proyecto
    // Método: POST /api/proyectos
    // Recibe un objeto Proyecto en el cuerpo de la petición
    // Retorna el proyecto creado junto con el estado 201 (CREATED)
    @PostMapping
    public ResponseEntity<Proyecto> create(@RequestBody @Validated Proyecto proyecto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.save(proyecto));
    }

    // Actualizar un proyecto existente por su ID
    // Método: PUT /api/proyectos/{id}
    // Si el proyecto existe, lo actualiza y retorna el proyecto modificado
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> update(@PathVariable Long id, @RequestBody @Validated Proyecto proyecto) {
        try {
            return ResponseEntity.ok(proyectoService.update(id, proyecto));
        }catch (ProyectoNoEncotradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar un proyecto por su ID
    // Método: DELETE /api/proyectos/{id}
    // Si el proyecto existe, lo elimina y devuelve estado 204 (NO CONTENT)
    // Si no existe, devuelve un estado 404 (NOT FOUND)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            proyectoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (ProyectoNoEncotradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
