package com.yettensyvus.sms.service;

import com.yettensyvus.sms.dto.StudentDTO;
import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.model.Student;
import com.yettensyvus.sms.repository.CursRepository;
import com.yettensyvus.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CursRepository cursRepository;

    public StudentService(StudentRepository studentRepository, CursRepository cursRepository) {
        this.studentRepository = studentRepository;
        this.cursRepository = cursRepository;
    }

    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toStudentDTO)
                .collect(Collectors.toList());
    }

    public Optional<StudentDTO> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return studentRepository.findById(id)
                .map(EntityToDtoMapper::toStudentDTO);
    }

    @Transactional
    public StudentDTO save(StudentDTO studentDTO) {
        validateStudentDTO(studentDTO, false);
        Student student = EntityToDtoMapper.toStudentEntity(studentDTO);

        // Handle the cursuri relationship
        if (studentDTO.getCursuriIds() != null && !studentDTO.getCursuriIds().isEmpty()) {
            List<Curs> cursuri = cursRepository.findAllById(studentDTO.getCursuriIds());
            if (cursuri.size() != studentDTO.getCursuriIds().size()) {
                throw new IllegalArgumentException("One or more course IDs do not exist");
            }
            student.setCursuri(cursuri);
            cursuri.forEach(curs -> curs.getStudenti().add(student));
        }

        Student savedStudent = studentRepository.save(student);
        return EntityToDtoMapper.toStudentDTO(savedStudent);
    }

    @Transactional
    public StudentDTO update(Long id, StudentDTO studentDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        validateStudentDTO(studentDTO, true);
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setNume(studentDTO.getNume());
                    existing.setVarsta(studentDTO.getVarsta());
                    existing.setEmail(studentDTO.getEmail());
                    existing.setSpecialitate(studentDTO.getSpecialitate());
                    existing.setAnStudiu(studentDTO.getAnStudiu());
                    existing.setMedie(studentDTO.getMedie());
                    existing.setBursier(studentDTO.isBursier());
                    existing.setDataInscrierii(studentDTO.getDataInscrierii());

                    if (studentDTO.getCursuriIds() != null) {
                        List<Curs> cursuri = cursRepository.findAllById(studentDTO.getCursuriIds());
                        if (cursuri.size() != studentDTO.getCursuriIds().size()) {
                            throw new IllegalArgumentException("One or more course IDs do not exist");
                        }
                        // Clear existing relationships and update
                        existing.getCursuri().forEach(curs -> curs.getStudenti().remove(existing));
                        existing.getCursuri().clear();
                        existing.setCursuri(cursuri);
                        cursuri.forEach(curs -> curs.getStudenti().add(existing));
                    }

                    Student updatedStudent = studentRepository.save(existing);
                    return EntityToDtoMapper.toStudentDTO(updatedStudent);
                })
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found"));
    }

    @Transactional
    public StudentDTO patch(Long id, StudentDTO studentDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return studentRepository.findById(id)
                .map(existing -> {
                    if (studentDTO.getNume() != null) {
                        validateField("nume", studentDTO.getNume());
                        existing.setNume(studentDTO.getNume());
                    }
                    if (studentDTO.getVarsta() != 0) {
                        validateVarsta(studentDTO.getVarsta());
                        existing.setVarsta(studentDTO.getVarsta());
                    }
                    if (studentDTO.getEmail() != null) {
                        validateEmail(studentDTO.getEmail());
                        existing.setEmail(studentDTO.getEmail());
                    }
                    if (studentDTO.getSpecialitate() != null) {
                        validateField("specialitate", studentDTO.getSpecialitate());
                        existing.setSpecialitate(studentDTO.getSpecialitate());
                    }
                    if (studentDTO.getAnStudiu() != 0) {
                        validateAnStudiu(studentDTO.getAnStudiu());
                        existing.setAnStudiu(studentDTO.getAnStudiu());
                    }
                    if (studentDTO.getMedie() != 0.0) {
                        validateMedie(studentDTO.getMedie());
                        existing.setMedie(studentDTO.getMedie());
                    }
                    if (studentDTO.getDataInscrierii() != null) {
                        validateDataInscrierii(studentDTO.getDataInscrierii());
                        existing.setDataInscrierii(studentDTO.getDataInscrierii());
                    }
                    existing.setBursier(studentDTO.isBursier());

                    if (studentDTO.getCursuriIds() != null) {
                        List<Curs> cursuri = cursRepository.findAllById(studentDTO.getCursuriIds());
                        if (cursuri.size() != studentDTO.getCursuriIds().size()) {
                            throw new IllegalArgumentException("One or more course IDs do not exist");
                        }
                        existing.getCursuri().forEach(curs -> curs.getStudenti().remove(existing));
                        existing.getCursuri().clear();
                        existing.setCursuri(cursuri);
                        cursuri.forEach(curs -> curs.getStudenti().add(existing));
                    }

                    Student updatedStudent = studentRepository.save(existing);
                    return EntityToDtoMapper.toStudentDTO(updatedStudent);
                })
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found"));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found"));
        student.getCursuri().forEach(curs -> curs.getStudenti().remove(student));
        student.getCursuri().clear();
        studentRepository.deleteById(id);
    }

    // Validation logic
    private void validateStudentDTO(StudentDTO studentDTO, boolean isUpdate) {
        if (studentDTO == null) {
            throw new IllegalArgumentException("StudentDTO object cannot be null");
        }
        if (!isUpdate) {
            validateField("nume", studentDTO.getNume());
            validateVarsta(studentDTO.getVarsta());
            validateEmail(studentDTO.getEmail());
            validateField("specialitate", studentDTO.getSpecialitate());
            validateAnStudiu(studentDTO.getAnStudiu());
            validateMedie(studentDTO.getMedie());
            validateDataInscrierii(studentDTO.getDataInscrierii());
        } else {
            if (studentDTO.getNume() != null) validateField("nume", studentDTO.getNume());
            if (studentDTO.getVarsta() != 0) validateVarsta(studentDTO.getVarsta());
            if (studentDTO.getEmail() != null) validateEmail(studentDTO.getEmail());
            if (studentDTO.getSpecialitate() != null) validateField("specialitate", studentDTO.getSpecialitate());
            if (studentDTO.getAnStudiu() != 0) validateAnStudiu(studentDTO.getAnStudiu());
            if (studentDTO.getMedie() != 0.0) validateMedie(studentDTO.getMedie());
            if (studentDTO.getDataInscrierii() != null) validateDataInscrierii(studentDTO.getDataInscrierii());
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

    private void validateVarsta(int varsta) {
        if (varsta < 16 || varsta > 100) {
            throw new IllegalArgumentException("Age must be between 16 and 100");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email cannot exceed 255 characters");
        }
    }

    private void validateAnStudiu(int anStudiu) {
        if (anStudiu < 1 || anStudiu > 6) {
            throw new IllegalArgumentException("Study year must be between 1 and 6");
        }
    }

    private void validateMedie(double medie) {
        if (medie < 1.0 || medie > 10.0) {
            throw new IllegalArgumentException("Average grade must be between 1.0 and 10.0");
        }
    }

    private void validateDataInscrierii(LocalDate dataInscrierii) {
        if (dataInscrierii == null) {
            throw new IllegalArgumentException("Enrollment date cannot be null");
        }
        if (dataInscrierii.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Enrollment date cannot be in the future");
        }
        if (dataInscrierii.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("Enrollment date cannot be before 1900");
        }
    }
}