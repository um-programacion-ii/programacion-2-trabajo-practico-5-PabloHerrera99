package com.empresa.gestion_empleados.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.exeptions.DepartamentoNoEncontradoExeption;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class TestDepartamentoService {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepertamentoServiceImpl departamentoService;

    private Departamento departamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Recursos Humanos");
        departamento.setDescripcion("Departamento de RRHH");
    }

    @Test
    void cuandoGuardarDepartamento_SePersisteCorrectamente() {
        when(departamentoRepository.save(departamento)).thenReturn(departamento);

        Departamento guardado = departamentoService.save(departamento);

        assertNotNull(guardado);
        assertEquals("Recursos Humanos", guardado.getNombre());
        verify(departamentoRepository, times(1)).save(departamento);
    }

    @Test
    void cuandoActualizarDepartamentoExistente_SeActualiza() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        Departamento actualizado = departamentoService.update(1L, departamento);

        assertEquals("Recursos Humanos", actualizado.getNombre());
        verify(departamentoRepository, times(1)).existsById(1L);
        verify(departamentoRepository, times(1)).save(departamento);
    }

    @Test
    void cuandoActualizarDepartamentoInexistente_LanzaExcepcion() {
        when(departamentoRepository.existsById(1L)).thenReturn(false);

        assertThrows(DepartamentoNoEncontradoExeption.class,
                () -> departamentoService.update(1L, departamento));
    }

    @Test
    void cuandoEliminarDepartamentoExistente_SeElimina() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);

        departamentoService.delete(1L);

        verify(departamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void cuandoEliminarDepartamentoInexistente_LanzaExcepcion() {
        when(departamentoRepository.existsById(1L)).thenReturn(false);

        assertThrows(DepartamentoNoEncontradoExeption.class,
                () -> departamentoService.delete(1L));
    }

    @Test
    void cuandoBuscarPorIdExistente_RetornaDepartamento() {
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));

        Departamento encontrado = departamentoService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("Recursos Humanos", encontrado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdInexistente_LanzaExcepcion() {
        when(departamentoRepository.existsById(1L)).thenReturn(false);

        assertThrows(DepartamentoNoEncontradoExeption.class,
                () -> departamentoService.findById(1L));
    }

    @Test
    void cuandoBuscarTodos_RetornaListaDepartamentos() {
        List<Departamento> lista = Arrays.asList(departamento);
        when(departamentoRepository.findAll()).thenReturn(lista);

        List<Departamento> resultado = departamentoService.findAll();

        assertEquals(1, resultado.size());
        verify(departamentoRepository, times(1)).findAll();
    }
}
