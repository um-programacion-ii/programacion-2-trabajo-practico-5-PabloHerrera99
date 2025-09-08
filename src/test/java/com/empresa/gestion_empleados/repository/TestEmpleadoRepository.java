package com.empresa.gestion_empleados.repository;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.entity.Empleado;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
public class TestEmpleadoRepository {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento;
    private Empleado empleado1;
    private Empleado empleado2;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamento = departamentoRepository.save(departamento);

        empleado1 = new Empleado();
        empleado1.setNombre("Juan");
        empleado1.setApellido("Pérez");
        empleado1.setEmail("juan.perez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.of(2020, 1, 1));
        empleado1.setSalario(new BigDecimal("50000.00"));
        empleado1.setDepartamento(departamento);

        empleado2 = new Empleado();
        empleado2.setNombre("Pancho");
        empleado2.setApellido("Pérez");
        empleado2.setEmail("pancho.perez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.of(2024, 1, 1));
        empleado2.setSalario(new BigDecimal("40000.00"));
        empleado2.setDepartamento(departamento);
    }

    @Test
    void cuandoGuardarEmpleado_SePersisteCorrectamente() {
        Empleado guardado = empleadoRepository.save(empleado1);

        assertNotNull(guardado.getId());
        assertEquals("juan.perez@empresa.com", guardado.getEmail());
        assertTrue(empleadoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoBuscarPorEmailExistente_RetornaEmpleado() {
        empleadoRepository.save(empleado1);

        Optional<Empleado> resultado = empleadoRepository.findByEmail("juan.perez@empresa.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan.perez@empresa.com", resultado.get().getEmail());
    }

    @Test
    void cuandoBuscarPorFechaContratacionPosterior_RetornaCorrectos() {
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> recientes = empleadoRepository.findByFechaContratacionAfter(LocalDate.of(2019, 1, 1));

        assertEquals(2, recientes.size());
    }

    @Test
    void cuandoBuscarPorDepartamento_RetornaCorrectos() {
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> recientes = empleadoRepository.findByDepartamento(departamento);

        assertEquals(2, recientes.size());
    }

    @Test
    void cuandoBuscarPorRangoSalario_RetornaCorrectos() {
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> recientes = empleadoRepository.findBySalarioBetween(new BigDecimal("25000"), new BigDecimal("50000"));
        assertEquals(2, recientes.size());
    }

    @Test
    void cuandoBuscarPorNombreDepartamento_RetornaCorrectos() {
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> recientes = empleadoRepository.findByNombreDepartamento("IT");

        assertEquals(2, recientes.size());
    }

    @Test
    void cuandoCalcularPromedioSalarioPorDepartamento_RetornaCorrecto() {
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        Optional<BigDecimal> promedio = empleadoRepository.findAverageSalarioByDepartamento(departamento.getId());

        assertTrue(promedio.isPresent());
        assertEquals(0, promedio.get().compareTo(new BigDecimal("45000.00")));
    }
}