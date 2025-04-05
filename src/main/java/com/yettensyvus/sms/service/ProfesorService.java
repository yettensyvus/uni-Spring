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
        System.out.println("Fetching all professors");
        return profesorRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toProfesorDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfesorDTO> findById(Long id) {
        validateId(id);
        System.out.println("Fetching professor with ID: " + id);
        return profesorRepository.findById(id)
                .map(EntityToDtoMapper::toProfesorDTO);
    }

    public ProfesorDTO save(ProfesorDTO profesorDTO) {
        System.out.println("Saving new professor with DTO: " + profesorDTO);
        validateProfesorDTO(profesorDTO, false);
        Profesor profesor = EntityToDtoMapper.toProfesorEntity(profesorDTO);
        Profesor savedProfesor = profesorRepository.save(profesor);
        System.out.println("Saved professor with ID: " + savedProfesor.getId());
        return EntityToDtoMapper.toProfesorDTO(savedProfesor);
    }

    public ProfesorDTO update(Long id, ProfesorDTO profesorDTO) {
        validateId(id);
        validateProfesorDTO(profesorDTO, true);

        return profesorRepository.findById(id)
                .map(existing -> {
                    System.out.println("Updating professor with ID: " + id);
                    existing.setNume(profesorDTO.getNume());
                    existing.setMaterie(profesorDTO.getMaterie());
                    existing.setExperientaAni(profesorDTO.getExperientaAni());
                    Profesor updatedProfesor = profesorRepository.save(existing);
                    System.out.println("Updated professor with ID: " + updatedProfesor.getId());
                    return EntityToDtoMapper.toProfesorDTO(updatedProfesor);
                })
                .orElseThrow(() -> {
                    System.out.println("Professor with ID " + id + " not found");
                    return new IllegalArgumentException("Professor with ID " + id + " not found");
                });
    }

    public ProfesorDTO patch(Long id, ProfesorDTO profesorDTO) {
        validateId(id);

        return profesorRepository.findById(id)
                .map(existing -> {
                    System.out.println("Patching professor with ID: " + id);
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
                    System.out.println("Patched professor with ID: " + updatedProfesor.getId());
                    return EntityToDtoMapper.toProfesorDTO(updatedProfesor);
                })
                .orElseThrow(() -> {
                    System.out.println("Professor with ID " + id + " not found");
                    return new IllegalArgumentException("Professor with ID " + id + " not found");
                });
    }

    public void delete(Long id) {
        validateId(id);
        if (!profesorRepository.existsById(id)) {
            System.out.println("Professor with ID " + id + " not found");
            throw new IllegalArgumentException("Professor with ID " + id + " not found");
        }
        System.out.println("Deleting professor with ID: " + id);
        profesorRepository.deleteById(id);
        System.out.println("Deleted professor with ID: " + id);
    }

    // Validation methods
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }

    private void validateProfesorDTO(ProfesorDTO profesorDTO, boolean isUpdate) {
        if (profesorDTO == null) {
            throw new IllegalArgumentException("ProfessorDTO object cannot be null");
        }

        if (!isUpdate) {
            // For new entities, all fields are required
            validateField("nume", profesorDTO.getNume());
            validateField("materie", profesorDTO.getMaterie());
            validateExperientaAni(profesorDTO.getExperientaAni());
        } else {
            // For updates, only validate fields that are present
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