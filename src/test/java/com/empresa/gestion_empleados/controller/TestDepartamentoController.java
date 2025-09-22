package com.empresa.gestion_empleados.controller;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TestDepartamentoController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento1;

    @BeforeEach
    void setUp() {
        departamentoRepository.deleteAll();

        departamento1 = new Departamento();
        departamento1.setNombre("Recursos Humanos");
        departamento1.setDescripcion("Departamento encargado de la gestión del personal");
    }

    @Test
    void cuandoGetAll_retornaListaDepartamentos() throws Exception {
        departamentoRepository.save(departamento1);

        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Recursos Humanos"))
                .andExpect(jsonPath("$[0].descripcion").value("Departamento encargado de la gestión del personal"));
    }

    @Test
    void cuandoGetById_existente_retornaDepartamento() throws Exception {
        Departamento guardado = departamentoRepository.save(departamento1);

        mockMvc.perform(get("/api/departamentos/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Recursos Humanos"))
                .andExpect(jsonPath("$.descripcion").value("Departamento encargado de la gestión del personal"));
    }

    @Test
    void cuandoGetById_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/departamentos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoPost_creaNuevoDepartamento() throws Exception {
        Departamento nuevo = new Departamento();
        nuevo.setNombre("Tecnología");
        nuevo.setDescripcion("Departamento de IT");

        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Tecnología"))
                .andExpect(jsonPath("$.descripcion").value("Departamento de IT"));
    }

    @Test
    void cuandoPut_actualizaDepartamentoExistente() throws Exception {
        Departamento guardado = departamentoRepository.save(departamento1);
        guardado.setNombre("RRHH Actualizado");
        guardado.setDescripcion("Nueva descripción");

        mockMvc.perform(put("/api/departamentos/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guardado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("RRHH Actualizado"))
                .andExpect(jsonPath("$.descripcion").value("Nueva descripción"));
    }

    @Test
    void cuandoPut_noExistente_retorna404() throws Exception {
        Departamento modificado = new Departamento();
        modificado.setNombre("No Existe");
        modificado.setDescripcion("Depto falso");

        mockMvc.perform(put("/api/departamentos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoDelete_existente_eliminaYRetorna204() throws Exception {
        Departamento guardado = departamentoRepository.save(departamento1);

        mockMvc.perform(delete("/api/departamentos/{id}", guardado.getId()))
                .andExpect(status().isNoContent());

        assertFalse(departamentoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoDelete_noExistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/departamentos/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
