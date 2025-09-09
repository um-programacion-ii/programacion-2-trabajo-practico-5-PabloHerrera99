package com.empresa.gestion_empleados.controller;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.entity.Empleado;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import com.empresa.gestion_empleados.repository.EmpleadoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TestEmpleadoController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento;
    private Empleado empleado1;

    @BeforeEach
    void setUp() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();

        departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de tecnología");
        departamento = departamentoRepository.save(departamento);

        empleado1 = new Empleado();
        empleado1.setNombre("Juan");
        empleado1.setApellido("Perez");
        empleado1.setEmail("juan.perez@test.com");
        empleado1.setFechaContratacion(LocalDate.of(2023, 1, 15));
        empleado1.setSalario(new BigDecimal("5000"));
        empleado1.setDepartamento(departamento);
    }

    @Test
    void cuandoGetAll_retornaListaEmpleados() throws Exception {
        empleadoRepository.save(empleado1);

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"))
                .andExpect(jsonPath("$[0].email").value("juan.perez@test.com"))
                .andExpect(jsonPath("$[0].salario").value(5000));
    }

    @Test
    void cuandoGetById_existente_retornaEmpleado() throws Exception {
        Empleado guardado = empleadoRepository.save(empleado1);

        mockMvc.perform(get("/api/empleados/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan.perez@test.com"))
                .andExpect(jsonPath("$.salario").value(5000));
    }

    @Test
    void cuandoGetById_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/empleados/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoPost_creaEmpleado() throws Exception {
        Empleado nuevo = new Empleado();
        nuevo.setNombre("Maria");
        nuevo.setApellido("Gomez");
        nuevo.setEmail("maria.gomez@test.com");
        nuevo.setFechaContratacion(LocalDate.of(2024, 5, 10));
        nuevo.setSalario(new BigDecimal("6000"));
        nuevo.setDepartamento(departamento);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Maria"))
                .andExpect(jsonPath("$.apellido").value("Gomez"))
                .andExpect(jsonPath("$.salario").value(6000));
    }

    @Test
    void cuandoPut_actualizaEmpleado() throws Exception {
        Empleado guardado = empleadoRepository.save(empleado1);
        guardado.setNombre("Juan Actualizado");
        guardado.setApellido("Modificado");
        guardado.setSalario(new BigDecimal("7000"));

        mockMvc.perform(put("/api/empleados/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guardado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"))
                .andExpect(jsonPath("$.apellido").value("Modificado"))
                .andExpect(jsonPath("$.salario").value(7000));
    }

    @Test
    void cuandoPut_noExistente_retorna404() throws Exception {
        Empleado modificado = new Empleado();
        modificado.setNombre("Pedro");
        modificado.setApellido("Torrecilla");
        modificado.setEmail("pedro@test.com");
        modificado.setFechaContratacion(LocalDate.of(2020, 1, 1));
        modificado.setSalario(new BigDecimal("3000"));
        modificado.setDepartamento(departamento);

        mockMvc.perform(put("/api/empleados/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoDelete_existente_eliminaYRetorna204() throws Exception {
        Empleado guardado = empleadoRepository.save(empleado1);

        mockMvc.perform(delete("/api/empleados/{id}", guardado.getId()))
                .andExpect(status().isNoContent());

        assertFalse(empleadoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoDelete_noExistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/empleados/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoGetByDepartamento_retornaListaEmpleados() throws Exception {
        empleadoRepository.save(empleado1);

        mockMvc.perform(get("/api/empleados/departamento/{nombreDepartamento}", "IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"));
    }

    @Test
    void cuandoGetSalarioPromedioPorDepartamento_retornaPromedio() throws Exception {
        empleadoRepository.save(empleado1);

        mockMvc.perform(get("/api/empleados/salario_promedio/{departamentoId}", departamento.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));
    }

    @Test
    void cuandoGetByRangoSalario_retornaEmpleadosEnRango() throws Exception {
        empleadoRepository.save(empleado1);

        mockMvc.perform(get("/api/empleados/rango-salario")
                        .param("salarioMin", "4000")
                        .param("salarioMax", "6000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"));
    }
}
