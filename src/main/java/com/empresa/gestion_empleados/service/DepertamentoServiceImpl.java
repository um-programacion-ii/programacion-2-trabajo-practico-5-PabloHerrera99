package com.empresa.gestion_empleados.service;

import com.empresa.gestion_empleados.entity.Departamento;
import com.empresa.gestion_empleados.exeptions.DepartamentoNoEncontradoExeption;
import com.empresa.gestion_empleados.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepertamentoServiceImpl implements DepartamentoService{
    private final DepartamentoRepository departamentoRepository;

    public DepertamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento update(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoExeption(id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public void delete(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoExeption(id);
        }
        departamentoRepository.deleteById(id);
    }

    @Override
    public Departamento findById(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoExeption(id);
        }
        return departamentoRepository.findById(id).get();
    }

    @Override
    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }
}
