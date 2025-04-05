package com.yettensyvus.sms.service;

import com.yettensyvus.sms.dto.CursDTO;
import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.model.Student;
import com.yettensyvus.sms.repository.CursRepository;
import com.yettensyvus.sms.repository.ProfesorRepository;
import com.yettensyvus.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursService {
    private final CursRepository cursRepository;
    private final StudentRepository studentRepository;
    private final ProfesorRepository profesorRepository;

    public CursService(CursRepository cursRepository, StudentRepository studentRepository, ProfesorRepository profesorRepository) {
        this.cursRepository = cursRepository;
        this.studentRepository = studentRepository;
        this.profesorRepository = profesorRepository;
    }

    public List<CursDTO> findAll() {
        System.out.println("Fetching all courses");
        return cursRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toCursDTO)
                .collect(Collectors.toList());
    }

    public Optional<CursDTO> findById(Long id) {
        validateId(id);
        System.out.println("Fetching course with ID: " + id);
        return cursRepository.findById(id)
                .map(EntityToDtoMapper::toCursDTO);
    }

    @Transactional
    public CursDTO save(CursDTO cursDTO) {
        System.out.println("Saving new course with DTO: " + cursDTO);
        validateCursDTO(cursDTO);
        Curs curs = new Curs();
        curs.setDenumire(cursDTO.getDenumire());
        curs.setCredite(cursDTO.getCredite());

        // Handle the profesor relationship
        if (cursDTO.getProfesorId() != null) {
            System.out.println("Fetching professor with ID: " + cursDTO.getProfesorId());
            Profesor profesor = getValidProfessor(cursDTO.getProfesorId());
            curs.setProfesor(profesor);
            profesor.getCursuri().add(curs);
            System.out.println("Set professor " + profesor.getId() + " for course " + curs.getDenumire());
        } else {
            System.out.println("No professor ID provided, leaving profesor as null");
        }

        // Handle the studenti relationship
        if (cursDTO.getStudentiIds() != null && !cursDTO.getStudentiIds().isEmpty()) {
            System.out.println("Fetching students with IDs: " + cursDTO.getStudentiIds());
            List<Student> studenti = validateAndGetStudents(cursDTO.getStudentiIds());
            curs.setStudenti(studenti);
            studenti.forEach(student -> student.getCursuri().add(curs));
            System.out.println("Set " + studenti.size() + " students for course " + curs.getDenumire());
        }

        Curs savedCurs = cursRepository.save(curs);
        System.out.println("Saved course with ID: " + savedCurs.getId());
        return EntityToDtoMapper.toCursDTO(savedCurs);
    }

    @Transactional
    public CursDTO update(Long id, CursDTO cursDTO) {
        validateId(id);
        validateCursDTO(cursDTO);
        return cursRepository.findById(id)
                .map(existing -> {
                    System.out.println("Updating course with ID: " + id);
                    existing.setDenumire(cursDTO.getDenumire());
                    existing.setCredite(cursDTO.getCredite());

                    // Update profesor relationship
                    updateProfessorRelationship(existing, cursDTO.getProfesorId());

                    // Update studenti relationship
                    if (cursDTO.getStudentiIds() != null) {
                        List<Student> studenti = validateAndGetStudents(cursDTO.getStudentiIds());
                        updateStudentRelationship(existing, studenti);
                    }

                    Curs updatedCurs = cursRepository.save(existing);
                    System.out.println("Updated course with ID: " + updatedCurs.getId());
                    return EntityToDtoMapper.toCursDTO(updatedCurs);
                })
                .orElseThrow(() -> {
                    System.out.println("Course with ID " + id + " not found");
                    return new IllegalArgumentException("Curs with ID " + id + " not found");
                });
    }

    @Transactional
    public CursDTO patch(Long id, CursDTO cursDTO) {
        validateId(id);
        return cursRepository.findById(id)
                .map(existing -> {
                    System.out.println("Patching course with ID: " + id);
                    if (cursDTO.getDenumire() != null) {
                        validateDenumire(cursDTO.getDenumire());
                        existing.setDenumire(cursDTO.getDenumire());
                    }
                    if (cursDTO.getCredite() != 0) {
                        validateCredite(cursDTO.getCredite());
                        existing.setCredite(cursDTO.getCredite());
                    }
                    if (cursDTO.getProfesorId() != null) {
                        updateProfessorRelationship(existing, cursDTO.getProfesorId());
                    }
                    if (cursDTO.getStudentiIds() != null) {
                        List<Student> studenti = validateAndGetStudents(cursDTO.getStudentiIds());
                        updateStudentRelationship(existing, studenti);
                    }

                    Curs updatedCurs = cursRepository.save(existing);
                    System.out.println("Patched course with ID: " + updatedCurs.getId());
                    return EntityToDtoMapper.toCursDTO(updatedCurs);
                })
                .orElseThrow(() -> {
                    System.out.println("Course with ID " + id + " not found");
                    return new IllegalArgumentException("Curs with ID " + id + " not found");
                });
    }

    @Transactional
    public void delete(Long id) {
        validateId(id);
        System.out.println("Deleting course with ID: " + id);
        Curs curs = cursRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Course with ID " + id + " not found");
                    return new IllegalArgumentException("Curs with ID " + id + " not found");
                });
        if (curs.getProfesor() != null) {
            curs.getProfesor().getCursuri().remove(curs);
        }
        curs.getStudenti().forEach(student -> student.getCursuri().remove(curs));
        curs.getStudenti().clear();
        cursRepository.deleteById(id);
        System.out.println("Deleted course with ID: " + id);
    }

    // Validation and Helper Methods
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }

    private void validateCursDTO(CursDTO cursDTO) {
        if (cursDTO == null) {
            throw new IllegalArgumentException("CursDTO object cannot be null");
        }
        validateDenumire(cursDTO.getDenumire());
        validateCredite(cursDTO.getCredite());
    }

    private void validateDenumire(String denumire) {
        if (denumire == null || denumire.trim().isEmpty()) {
            throw new IllegalArgumentException("denumire cannot be null or empty");
        }
        if (denumire.length() > 100) {
            throw new IllegalArgumentException("denumire cannot exceed 100 characters");
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

    private Profesor getValidProfessor(Long profesorId) {
        return profesorRepository.findById(profesorId)
                .orElseThrow(() -> {
                    System.out.println("Professor with ID " + profesorId + " not found");
                    return new IllegalArgumentException("Professor with ID " + profesorId + " not found");
                });
    }

    private List<Student> validateAndGetStudents(List<Long> studentiIds) {
        List<Student> studenti = studentRepository.findAllById(studentiIds);
        if (studenti.size() != studentiIds.size()) {
            System.out.println("One or more student IDs do not exist");
            throw new IllegalArgumentException("One or more student IDs do not exist");
        }
        return studenti;
    }

    private void updateProfessorRelationship(Curs existing, Long newProfesorId) {
        if (newProfesorId != null) {
            System.out.println("Fetching professor with ID: " + newProfesorId);
            Profesor newProfesor = getValidProfessor(newProfesorId);
            if (existing.getProfesor() != null && !existing.getProfesor().getId().equals(newProfesorId)) {
                existing.getProfesor().getCursuri().remove(existing);
            }
            existing.setProfesor(newProfesor);
            newProfesor.getCursuri().add(existing);
            System.out.println("Updated professor to ID: " + newProfesor.getId());
        } else if (newProfesorId == null && existing.getProfesor() != null) {
            existing.getProfesor().getCursuri().remove(existing);
            existing.setProfesor(null);
            System.out.println("Removed professor from course");
        }
    }

    private void updateStudentRelationship(Curs existing, List<Student> studenti) {
        existing.getStudenti().forEach(student -> student.getCursuri().remove(existing));
        existing.getStudenti().clear();
        existing.setStudenti(studenti);
        studenti.forEach(student -> student.getCursuri().add(existing));
    }
}