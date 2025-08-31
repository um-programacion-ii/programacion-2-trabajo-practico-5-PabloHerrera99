package com.empresa.gestion_empleados.service;

import com.empresa.gestion_empleados.entity.Proyecto;
import com.empresa.gestion_empleados.exeptions.ProyectoNoEncotradoExeption;
import com.empresa.gestion_empleados.repository.ProyectoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {
    private ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto update(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncotradoExeption(id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void delete(Long id) {
        if  (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncotradoExeption(id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public Proyecto findById(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncotradoExeption(id);
        }
        return proyectoRepository.findById(id).get();
    }

    @Override
    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    @Override
    public List<Proyecto> findActivo(LocalDate hoy) {
        return proyectoRepository.findProyectoActivo(hoy);
    }
}
