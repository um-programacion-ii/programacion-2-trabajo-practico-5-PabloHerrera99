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
    @GetMapping
    public ResponseEntity<List<Proyecto>> findAll() {
        return ResponseEntity.ok(proyectoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(proyectoService.findById(id));
        }catch (ProyectoNoEncotradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/Activo/{hoy}")
    public ResponseEntity<List<Proyecto>> findActivo(@PathVariable LocalDate hoy) {
        return ResponseEntity.ok(proyectoService.findActivo(hoy));
    }

    @PostMapping
    public ResponseEntity<Proyecto> create(@RequestBody @Validated Proyecto proyecto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.save(proyecto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> update(@PathVariable Long id, @RequestBody @Validated Proyecto proyecto) {
        try {
            return ResponseEntity.ok(proyectoService.update(id, proyecto));
        }catch (ProyectoNoEncotradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

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
