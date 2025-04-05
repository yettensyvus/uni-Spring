package com.yettensyvus.sms.service;

import com.yettensyvus.sms.dto.CursDTO;
import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.model.Student;
import com.yettensyvus.sms.repository.CursRepository;
import com.yettensyvus.sms.repository.ProfesorRepository;
import com.yettensyvus.sms.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursService {
    private static final Logger logger = LoggerFactory.getLogger(CursService.class);

    private final CursRepository cursRepository;
    private final StudentRepository studentRepository;
    private final ProfesorRepository profesorRepository;

    public CursService(CursRepository cursRepository, StudentRepository studentRepository, ProfesorRepository profesorRepository) {
        this.cursRepository = cursRepository;
        this.studentRepository = studentRepository;
        this.profesorRepository = profesorRepository;
    }

    public List<CursDTO> findAll() {
        return cursRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toCursDTO)
                .collect(Collectors.toList());
    }

    public Optional<CursDTO> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return cursRepository.findById(id)
                .map(EntityToDtoMapper::toCursDTO);
    }

    @Transactional
    public CursDTO save(CursDTO cursDTO) {
        logger.info("Saving new course with DTO: {}", cursDTO);
        validateCursDTO(cursDTO);
        Curs curs = new Curs();
        curs.setDenumire(cursDTO.getDenumire());
        curs.setCredite(cursDTO.getCredite());

        // Handle the profesor relationship
        if (cursDTO.getProfesorId() != null) {
            logger.info("Fetching professor with ID: {}", cursDTO.getProfesorId());
            Profesor profesor = profesorRepository.findById(cursDTO.getProfesorId())
                    .orElseThrow(() -> {
                        logger.error("Professor with ID {} not found", cursDTO.getProfesorId());
                        return new IllegalArgumentException("Professor with ID " + cursDTO.getProfesorId() + " not found");
                    });
            logger.info("Professor found: {}", profesor);
            curs.setProfesor(profesor);
            profesor.getCursuri().add(curs);
            logger.info("Set professor {} for course {}", profesor.getId(), curs.getDenumire());
        } else {
            logger.info("No professor ID provided, leaving profesor as null");
        }

        // Handle the studenti relationship
        if (cursDTO.getStudentiIds() != null && !cursDTO.getStudentiIds().isEmpty()) {
            logger.info("Fetching students with IDs: {}", cursDTO.getStudentiIds());
            List<Student> studenti = studentRepository.findAllById(cursDTO.getStudentiIds());
            if (studenti.size() != cursDTO.getStudentiIds().size()) {
                throw new IllegalArgumentException("One or more student IDs do not exist");
            }
            curs.setStudenti(studenti);
            studenti.forEach(student -> student.getCursuri().add(curs));
            logger.info("Set {} students for course {}", studenti.size(), curs.getDenumire());
        }

        Curs savedCurs = cursRepository.save(curs);
        logger.info("Saved course with ID: {}", savedCurs.getId());
        return EntityToDtoMapper.toCursDTO(savedCurs);
    }

    @Transactional
    public CursDTO update(Long id, CursDTO cursDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        validateCursDTO(cursDTO);
        return cursRepository.findById(id)
                .map(existing -> {
                    logger.info("Updating course with ID: {}", id);
                    existing.setDenumire(cursDTO.getDenumire());
                    existing.setCredite(cursDTO.getCredite());

                    // Update profesor relationship
                    if (cursDTO.getProfesorId() != null) {
                        logger.info("Fetching professor with ID: {}", cursDTO.getProfesorId());
                        Profesor newProfesor = profesorRepository.findById(cursDTO.getProfesorId())
                                .orElseThrow(() -> new IllegalArgumentException("Professor with ID " + cursDTO.getProfesorId() + " not found"));
                        if (existing.getProfesor() != null && !existing.getProfesor().getId().equals(cursDTO.getProfesorId())) {
                            existing.getProfesor().getCursuri().remove(existing);
                        }
                        existing.setProfesor(newProfesor);
                        newProfesor.getCursuri().add(existing);
                        logger.info("Updated professor to ID: {}", newProfesor.getId());
                    } else if (cursDTO.getProfesorId() == null && existing.getProfesor() != null) {
                        existing.getProfesor().getCursuri().remove(existing);
                        existing.setProfesor(null);
                        logger.info("Removed professor from course");
                    }

                    // Update studenti relationship
                    if (cursDTO.getStudentiIds() != null) {
                        List<Student> studenti = studentRepository.findAllById(cursDTO.getStudentiIds());
                        if (studenti.size() != cursDTO.getStudentiIds().size()) {
                            throw new IllegalArgumentException("One or more student IDs do not exist");
                        }
                        existing.getStudenti().forEach(student -> student.getCursuri().remove(existing));
                        existing.getStudenti().clear();
                        existing.setStudenti(studenti);
                        studenti.forEach(student -> student.getCursuri().add(existing));
                    }

                    Curs updatedCurs = cursRepository.save(existing);
                    logger.info("Updated course with ID: {}", updatedCurs.getId());
                    return EntityToDtoMapper.toCursDTO(updatedCurs);
                })
                .orElseThrow(() -> new IllegalArgumentException("Curs with ID " + id + " not found"));
    }

    @Transactional
    public CursDTO patch(Long id, CursDTO cursDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return cursRepository.findById(id)
                .map(existing -> {
                    if (cursDTO.getDenumire() != null) {
                        validateField("denumire", cursDTO.getDenumire());
                        existing.setDenumire(cursDTO.getDenumire());
                    }
                    if (cursDTO.getCredite() != 0) {
                        validateCredite(cursDTO.getCredite());
                        existing.setCredite(cursDTO.getCredite());
                    }
                    if (cursDTO.getProfesorId() != null) {
                        logger.info("Patching professor with ID: {}", cursDTO.getProfesorId());
                        Profesor newProfesor = profesorRepository.findById(cursDTO.getProfesorId())
                                .orElseThrow(() -> new IllegalArgumentException("Professor with ID " + cursDTO.getProfesorId() + " not found"));
                        if (existing.getProfesor() != null && !existing.getProfesor().getId().equals(cursDTO.getProfesorId())) {
                            existing.getProfesor().getCursuri().remove(existing);
                        }
                        existing.setProfesor(newProfesor);
                        newProfesor.getCursuri().add(existing);
                        logger.info("Patched professor to ID: {}", newProfesor.getId());
                    }
                    if (cursDTO.getStudentiIds() != null) {
                        List<Student> studenti = studentRepository.findAllById(cursDTO.getStudentiIds());
                        if (studenti.size() != cursDTO.getStudentiIds().size()) {
                            throw new IllegalArgumentException("One or more student IDs do not exist");
                        }
                        existing.getStudenti().forEach(student -> student.getCursuri().remove(existing));
                        existing.getStudenti().clear();
                        existing.setStudenti(studenti);
                        studenti.forEach(student -> student.getCursuri().add(existing));
                    }

                    Curs updatedCurs = cursRepository.save(existing);
                    return EntityToDtoMapper.toCursDTO(updatedCurs);
                })
                .orElseThrow(() -> new IllegalArgumentException("Curs with ID " + id + " not found"));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        Curs curs = cursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curs with ID " + id + " not found"));
        if (curs.getProfesor() != null) {
            curs.getProfesor().getCursuri().remove(curs);
        }
        curs.getStudenti().forEach(student -> student.getCursuri().remove(curs));
        curs.getStudenti().clear();
        cursRepository.deleteById(id);
    }

    // Validation Methods
    private void validateCursDTO(CursDTO cursDTO) {
        if (cursDTO == null) {
            throw new IllegalArgumentException("CursDTO object cannot be null");
        }
        validateField("denumire", cursDTO.getDenumire());
        validateCredite(cursDTO.getCredite());
    }

    private void validateField(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 100 characters");
        }
    }

    private void validateCredite(int credite) {
        if (credite <= 0) {
            throw new IllegalArgumentException("Credite must be positive");
        }
        if (credite > 20) {
            throw new IllegalArgumentException("Credite cannot exceed 20");
        }
    }
}