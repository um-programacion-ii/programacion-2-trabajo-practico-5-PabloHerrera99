package com.empresa.gestion_empleados.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "proyectos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 100)
    private String estado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToMany(mappedBy = "proyectos")
    private Set<Empleado> empleados = new HashSet<>();
}
