package com.yettensyvus.sms.service;

import com.yettensyvus.sms.dto.ProfesorDTO;
import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.repository.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesorService {
    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<ProfesorDTO> findAll() {
        return profesorRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toProfesorDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfesorDTO> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return profesorRepository.findById(id)
                .map(EntityToDtoMapper::toProfesorDTO);
    }

    public ProfesorDTO save(ProfesorDTO profesorDTO) {
        validateProfesorDTO(profesorDTO, false); // false indicates new entity
        Profesor profesor = EntityToDtoMapper.toProfesorEntity(profesorDTO);
        Profesor savedProfesor = profesorRepository.save(profesor);
        return EntityToDtoMapper.toProfesorDTO(savedProfesor);
    }

    public ProfesorDTO update(Long id, ProfesorDTO profesorDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        validateProfesorDTO(profesorDTO, true); // true indicates update
        return profesorRepository.findById(id)
                .map(existing -> {
                    existing.setNume(profesorDTO.getNume());
                    existing.setMaterie(profesorDTO.getMaterie());
                    existing.setExperientaAni(profesorDTO.getExperientaAni());
                    Profesor updatedProfesor = profesorRepository.save(existing);
                    return EntityToDtoMapper.toProfesorDTO(updatedProfesor);
                })
                .orElseThrow(() -> new IllegalArgumentException("Professor with ID " + id + " not found"));
    }

    public ProfesorDTO patch(Long id, ProfesorDTO profesorDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return profesorRepository.findById(id)
                .map(existing -> {
                    if (profesorDTO.getNume() != null) {
                        validateField("nume", profesorDTO.getNume());
                        existing.setNume(profesorDTO.getNume());
                    }
                    if (profesorDTO.getMaterie() != null) {
                        validateField("materie", profesorDTO.getMaterie());
                        existing.setMaterie(profesorDTO.getMaterie());
                    }
                    if (profesorDTO.getExperientaAni() != 0) {
                        validateExperientaAni(profesorDTO.getExperientaAni());
                        existing.setExperientaAni(profesorDTO.getExperientaAni());
                    }
                    Profesor updatedProfesor = profesorRepository.save(existing);
                    return EntityToDtoMapper.toProfesorDTO(updatedProfesor);
                })
                .orElseThrow(() -> new IllegalArgumentException("Professor with ID " + id + " not found"));
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        if (!profesorRepository.existsById(id)) {
            throw new IllegalArgumentException("Professor with ID " + id + " not found");
        }
        profesorRepository.deleteById(id);
    }

    // Validation logic
    private void validateProfesorDTO(ProfesorDTO profesorDTO, boolean isUpdate) {
        if (profesorDTO == null) {
            throw new IllegalArgumentException("ProfessorDTO object cannot be null");
        }
        if (!isUpdate) { // For new entities, all fields might be required
            validateField("nume", profesorDTO.getNume());
            validateField("materie", profesorDTO.getMaterie());
            validateExperientaAni(profesorDTO.getExperientaAni());
        } else { // For updates, only validate non-null fields
            if (profesorDTO.getNume() != null) validateField("nume", profesorDTO.getNume());
            if (profesorDTO.getMaterie() != null) validateField("materie", profesorDTO.getMaterie());
            if (profesorDTO.getExperientaAni() != 0) validateExperientaAni(profesorDTO.getExperientaAni());
        }
    }

    private void validateField(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 100 characters");
        }
    }

    private void validateExperientaAni(int experientaAni) {
        if (experientaAni < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative");
        }
        if (experientaAni > 100) {
            throw new IllegalArgumentException("Experience years cannot exceed 100");
        }
    }
}