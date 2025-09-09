package com.empresa.gestion_empleados.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.empresa.gestion_empleados.entity.Empleado;
import com.empresa.gestion_empleados.exeptions.EmailDuplicadoException;
import com.empresa.gestion_empleados.exeptions.EmpleadoNoEncontradoException;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import com.empresa.gestion_empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

class TestEmpleadoService {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setSalario(new BigDecimal("50000"));
    }

    @Test
    void cuandoGuardarEmpleadoConEmailUnico_SePersisteCorrectamente() {
        when(empleadoRepository.findByEmail(empleado.getEmail())).thenReturn(Optional.empty());
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado guardado = empleadoService.save(empleado);

        assertNotNull(guardado);
        assertEquals("Juan Pérez", guardado.getNombre());
        verify(empleadoRepository, times(1)).save(empleado);
    }

    @Test
    void cuandoGuardarEmpleadoConEmailDuplicado_LanzaExcepcion() {
        when(empleadoRepository.findByEmail(empleado.getEmail())).thenReturn(Optional.of(empleado));

        assertThrows(EmailDuplicadoException.class,
                () -> empleadoService.save(empleado));
    }

    @Test
    void cuandoBuscarPorIdExistente_RetornaEmpleado() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        Empleado encontrado = empleadoService.findbyId(1L);

        assertEquals("Juan Pérez", encontrado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdInexistente_LanzaExcepcion() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmpleadoNoEncontradoException.class,
                () -> empleadoService.findbyId(1L));
    }

    @Test
    void cuandoBuscarPorDepartamento_RetornaListaEmpleados() {
        List<Empleado> lista = Arrays.asList(empleado);
        when(empleadoRepository.findByNombreDepartamento("IT")).thenReturn(lista);

        List<Empleado> resultado = empleadoService.findbyDepartamento("IT");

        assertEquals(1, resultado.size());
        verify(empleadoRepository, times(1)).findByNombreDepartamento("IT");
    }

    @Test
    void cuandoBuscarPorRangoSalario_RetornaListaCorrecta() {
        List<Empleado> lista = Arrays.asList(empleado);
        when(empleadoRepository.findBySalarioBetween(new BigDecimal("40000"), new BigDecimal("60000")))
                .thenReturn(lista);

        List<Empleado> resultado = empleadoService.findbyRangoSalario(new BigDecimal("40000"), new BigDecimal("60000"));

        assertEquals(1, resultado.size());
        verify(empleadoRepository, times(1))
                .findBySalarioBetween(new BigDecimal("40000"), new BigDecimal("60000"));
    }

    @Test
    void cuandoObtenerSalarioPromedioDepartamentoExiste_RetornaValor() {
        when(empleadoRepository.findAverageSalarioByDepartamento(1L))
                .thenReturn(Optional.of(new BigDecimal("55000")));

        BigDecimal promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(1L);

        assertEquals(new BigDecimal("55000"), promedio);
    }

    @Test
    void cuandoObtenerSalarioPromedioDepartamentoInexistente_RetornaCero() {
        when(empleadoRepository.findAverageSalarioByDepartamento(1L))
                .thenReturn(Optional.empty());

        BigDecimal promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(1L);

        assertEquals(BigDecimal.ZERO, promedio);
    }

    @Test
    void cuandoBuscarTodos_RetornaListaEmpleados() {
        List<Empleado> lista = Arrays.asList(empleado);
        when(empleadoRepository.findAll()).thenReturn(lista);

        List<Empleado> resultado = empleadoService.findAll();

        assertEquals(1, resultado.size());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    void cuandoActualizarEmpleadoExistente_SeActualiza() {
        when(empleadoRepository.existsById(1L)).thenReturn(true);
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado actualizado = empleadoService.update(1L, empleado);

        assertEquals("Juan Pérez", actualizado.getNombre());
        verify(empleadoRepository, times(1)).save(empleado);
    }

    @Test
    void cuandoActualizarEmpleadoInexistente_LanzaExcepcion() {
        when(empleadoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EmpleadoNoEncontradoException.class,
                () -> empleadoService.update(1L, empleado));
    }

    @Test
    void cuandoEliminarEmpleadoExistente_SeElimina() {
        when(empleadoRepository.existsById(1L)).thenReturn(true);

        empleadoService.delete(1L);

        verify(empleadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void cuandoEliminarEmpleadoInexistente_LanzaExcepcion() {
        when(empleadoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EmpleadoNoEncontradoException.class,
                () -> empleadoService.delete(1L));
    }
}

