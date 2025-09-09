package com.empresa.gestion_empleados.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.empresa.gestion_empleados.entity.Proyecto;
import com.empresa.gestion_empleados.exeptions.ProyectoNoEncotradoExeption;
import com.empresa.gestion_empleados.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class TestProyectoService {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoServiceImpl proyectoService;

    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Sistema de Gestión");
        proyecto.setDescripcion("Proyecto de IT");
        proyecto.setEstado("EN_PROGRESO");
    }

    @Test
    void cuandoGuardarProyecto_SePersisteCorrectamente() {
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto guardado = proyectoService.save(proyecto);

        assertNotNull(guardado);
        assertEquals("Sistema de Gestión", guardado.getNombre());
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    void cuandoActualizarProyectoExistente_SeActualiza() {
        when(proyectoRepository.existsById(1L)).thenReturn(true);
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyecto);

        Proyecto actualizado = proyectoService.update(1L, proyecto);

        assertEquals("Sistema de Gestión", actualizado.getNombre());
        verify(proyectoRepository, times(1)).existsById(1L);
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    void cuandoActualizarProyectoInexistente_LanzaExcepcion() {
        when(proyectoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProyectoNoEncotradoExeption.class,
                () -> proyectoService.update(1L, proyecto));
    }

    @Test
    void cuandoEliminarProyectoExistente_SeElimina() {
        when(proyectoRepository.existsById(1L)).thenReturn(true);

        proyectoService.delete(1L);

        verify(proyectoRepository, times(1)).deleteById(1L);
    }

    @Test
    void cuandoEliminarProyectoInexistente_LanzaExcepcion() {
        when(proyectoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProyectoNoEncotradoExeption.class,
                () -> proyectoService.delete(1L));
    }

    @Test
    void cuandoBuscarPorIdExistente_RetornaProyecto() {
        when(proyectoRepository.existsById(1L)).thenReturn(true);
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        Proyecto encontrado = proyectoService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("Sistema de Gestión", encontrado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdInexistente_LanzaExcepcion() {
        when(proyectoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProyectoNoEncotradoExeption.class,
                () -> proyectoService.findById(1L));
    }

    @Test
    void cuandoBuscarTodos_RetornaListaProyectos() {
        List<Proyecto> lista = Arrays.asList(proyecto);
        when(proyectoRepository.findAll()).thenReturn(lista);

        List<Proyecto> resultado = proyectoService.findAll();

        assertEquals(1, resultado.size());
        verify(proyectoRepository, times(1)).findAll();
    }

    @Test
    void cuandoBuscarPorEstado_RetornaListaCorrecta() {
        List<Proyecto> lista = Arrays.asList(proyecto);
        when(proyectoRepository.findByEstado("EN_PROGRESO")).thenReturn(lista);

        List<Proyecto> resultado = proyectoService.findByStatus("EN_PROGRESO");

        assertEquals(1, resultado.size());
        assertEquals("EN_PROGRESO", resultado.get(0).getEstado());
        verify(proyectoRepository, times(1)).findByEstado("EN_PROGRESO");
    }
}
