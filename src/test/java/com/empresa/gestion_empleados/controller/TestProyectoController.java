package com.empresa.gestion_empleados.controller;


import com.empresa.gestion_empleados.entity.Proyecto;
import com.empresa.gestion_empleados.repository.ProyectoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TestProyectoController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto proyecto1;

    @BeforeEach
    void setUp() {
        proyectoRepository.deleteAll();

        proyecto1 = new Proyecto();
        proyecto1.setNombre("Sistema de Gestión");
        proyecto1.setDescripcion("Proyecto para gestionar empleados");
        proyecto1.setEstado("EN_PROGRESO");
        proyecto1.setFechaInicio(LocalDate.of(2025, 1, 1));
        proyecto1.setFechaFin(LocalDate.of(2025, 12, 31));
    }

    @Test
    void cuandoGetAll_retornaListaProyectos() throws Exception {
        proyectoRepository.save(proyecto1);

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sistema de Gestión"))
                .andExpect(jsonPath("$[0].descripcion").value("Proyecto para gestionar empleados"))
                .andExpect(jsonPath("$[0].estado").value("EN_PROGRESO"))
                .andExpect(jsonPath("$[0].fechaInicio").value("2025-01-01"))
                .andExpect(jsonPath("$[0].fechaFin").value("2025-12-31"));
    }

    @Test
    void cuandoGetById_existente_retornaProyecto() throws Exception {
        Proyecto guardado = proyectoRepository.save(proyecto1);

        mockMvc.perform(get("/api/proyectos/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sistema de Gestión"))
                .andExpect(jsonPath("$.descripcion").value("Proyecto para gestionar empleados"))
                .andExpect(jsonPath("$.estado").value("EN_PROGRESO"))
                .andExpect(jsonPath("$.fechaInicio").value("2025-01-01"))
                .andExpect(jsonPath("$.fechaFin").value("2025-12-31"));
    }

    @Test
    void cuandoGetById_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/proyectos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoGetByStatus_retornaListaProyectosConEseEstado() throws Exception {
        proyectoRepository.save(proyecto1);

        mockMvc.perform(get("/api/proyectos/estado/{status}", "EN_PROGRESO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EN_PROGRESO"));
    }

    @Test
    void cuandoPost_creaNuevoProyecto() throws Exception {
        Proyecto nuevo = new Proyecto();
        nuevo.setNombre("Nuevo Proyecto");
        nuevo.setDescripcion("Proyecto de prueba");
        nuevo.setEstado("PLANIFICADO");
        nuevo.setFechaInicio(LocalDate.of(2025, 3, 1));
        nuevo.setFechaFin(LocalDate.of(2025, 6, 30));

        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Nuevo Proyecto"))
                .andExpect(jsonPath("$.estado").value("PLANIFICADO"))
                .andExpect(jsonPath("$.fechaInicio").value("2025-03-01"))
                .andExpect(jsonPath("$.fechaFin").value("2025-06-30"));
    }

    @Test
    void cuandoPut_actualizaProyectoExistente() throws Exception {
        Proyecto guardado = proyectoRepository.save(proyecto1);
        guardado.setNombre("Sistema Actualizado");
        guardado.setDescripcion("Descripción actualizada");
        guardado.setEstado("FINALIZADO");
        guardado.setFechaInicio(LocalDate.of(2025, 2, 1));
        guardado.setFechaFin(LocalDate.of(2025, 11, 30));

        mockMvc.perform(put("/api/proyectos/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guardado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sistema Actualizado"))
                .andExpect(jsonPath("$.descripcion").value("Descripción actualizada"))
                .andExpect(jsonPath("$.estado").value("FINALIZADO"))
                .andExpect(jsonPath("$.fechaInicio").value("2025-02-01"))
                .andExpect(jsonPath("$.fechaFin").value("2025-11-30"));
    }

    @Test
    void cuandoPut_noExistente_retorna404() throws Exception {
        Proyecto modificado = new Proyecto();
        modificado.setNombre("falso");
        modificado.setDescripcion("No existe en la DB");
        modificado.setEstado("CANCELADO");
        modificado.setFechaInicio(LocalDate.of(2025, 5, 1));
        modificado.setFechaFin(LocalDate.of(2025, 10, 1));

        mockMvc.perform(put("/api/proyectos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoDelete_existente_eliminaYRetorna204() throws Exception {
        Proyecto guardado = proyectoRepository.save(proyecto1);

        mockMvc.perform(delete("/api/proyectos/{id}", guardado.getId()))
                .andExpect(status().isNoContent());

        assertFalse(proyectoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoDelete_noExistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/proyectos/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
