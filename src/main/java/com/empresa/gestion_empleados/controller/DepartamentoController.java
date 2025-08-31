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

    @GetMapping
    public ResponseEntity<List<Departamento>> findAll() {
        return ResponseEntity.ok(departamentoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(departamentoService.findById(id));
        } catch (DepartamentoNoEncontradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Departamento> create(@RequestBody Departamento departamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.save(departamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> update(@PathVariable Long id, @RequestBody Departamento departamento) {
        try {
            return ResponseEntity.ok(departamentoService.update(id, departamento));
        }catch (DepartamentoNoEncontradoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

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
