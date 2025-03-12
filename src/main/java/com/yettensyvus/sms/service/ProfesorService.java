package com.yettensyvus.sms.service;

import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.repository.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService {
    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    public Optional<Profesor> findById(Long id) {
        return profesorRepository.findById(id);
    }

    public Profesor save(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    public Profesor update(Long id, Profesor profesor) {
        profesor.setId(id);
        return profesorRepository.save(profesor);
    }

    public void delete(Long id) {
        profesorRepository.deleteById(id);
    }
}