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
        System.out.println("Fetching all students");
        return studentRepository.findAll()
                .stream()
                .map(EntityToDtoMapper::toStudentDTO)
                .collect(Collectors.toList());
    }

    public Optional<StudentDTO> findById(Long id) {
        validateId(id);
        System.out.println("Fetching student with ID: " + id);
        return studentRepository.findById(id)
                .map(EntityToDtoMapper::toStudentDTO);
    }

    @Transactional
    public StudentDTO save(StudentDTO studentDTO) {
        System.out.println("Saving new student with DTO: " + studentDTO);
        validateStudentDTO(studentDTO, false);

        Student student = EntityToDtoMapper.toStudentEntity(studentDTO);
        handleCourseRelationships(student, studentDTO.getCursuriIds());

        Student savedStudent = studentRepository.save(student);
        System.out.println("Saved student with ID: " + savedStudent.getId());
        return EntityToDtoMapper.toStudentDTO(savedStudent);
    }

    @Transactional
    public StudentDTO update(Long id, StudentDTO studentDTO) {
        validateId(id);
        validateStudentDTO(studentDTO, true);

        System.out.println("Updating student with ID: " + id);
        return studentRepository.findById(id)
                .map(existing -> {
                    updateStudentFields(existing, studentDTO);
                    handleCourseRelationships(existing, studentDTO.getCursuriIds());

                    Student updatedStudent = studentRepository.save(existing);
                    System.out.println("Updated student with ID: " + updatedStudent.getId());
                    return EntityToDtoMapper.toStudentDTO(updatedStudent);
                })
                .orElseThrow(() -> {
                    System.out.println("Student with ID " + id + " not found");
                    return new IllegalArgumentException("Student with ID " + id + " not found");
                });
    }

    @Transactional
    public StudentDTO patch(Long id, StudentDTO studentDTO) {
        validateId(id);
        System.out.println("Patching student with ID: " + id);

        return studentRepository.findById(id)
                .map(existing -> {
                    patchStudentFields(existing, studentDTO);
                    if (studentDTO.getCursuriIds() != null) {
                        handleCourseRelationships(existing, studentDTO.getCursuriIds());
                    }

                    Student updatedStudent = studentRepository.save(existing);
                    System.out.println("Patched student with ID: " + updatedStudent.getId());
                    return EntityToDtoMapper.toStudentDTO(updatedStudent);
                })
                .orElseThrow(() -> {
                    System.out.println("Student with ID " + id + " not found");
                    return new IllegalArgumentException("Student with ID " + id + " not found");
                });
    }

    @Transactional
    public void delete(Long id) {
        validateId(id);
        System.out.println("Deleting student with ID: " + id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Student with ID " + id + " not found");
                    return new IllegalArgumentException("Student with ID " + id + " not found");
                });

        student.getCursuri().forEach(curs -> curs.getStudenti().remove(student));
        student.getCursuri().clear();
        studentRepository.deleteById(id);
        System.out.println("Deleted student with ID: " + id);
    }

    // Helper methods
    private void updateStudentFields(Student student, StudentDTO studentDTO) {
        student.setNume(studentDTO.getNume());
        student.setVarsta(studentDTO.getVarsta());
        student.setEmail(studentDTO.getEmail());
        student.setSpecialitate(studentDTO.getSpecialitate());
        student.setAnStudiu(studentDTO.getAnStudiu());
        student.setMedie(studentDTO.getMedie());
        student.setBursier(studentDTO.isBursier());
        student.setDataInscrierii(studentDTO.getDataInscrierii());
    }

    private void patchStudentFields(Student student, StudentDTO studentDTO) {
        if (studentDTO.getNume() != null) {
            validateField("nume", studentDTO.getNume());
            student.setNume(studentDTO.getNume());
        }
        if (studentDTO.getVarsta() != 0) {
            validateVarsta(studentDTO.getVarsta());
            student.setVarsta(studentDTO.getVarsta());
        }
        if (studentDTO.getEmail() != null) {
            validateEmail(studentDTO.getEmail());
            student.setEmail(studentDTO.getEmail());
        }
        if (studentDTO.getSpecialitate() != null) {
            validateField("specialitate", studentDTO.getSpecialitate());
            student.setSpecialitate(studentDTO.getSpecialitate());
        }
        if (studentDTO.getAnStudiu() != 0) {
            validateAnStudiu(studentDTO.getAnStudiu());
            student.setAnStudiu(studentDTO.getAnStudiu());
        }
        if (studentDTO.getMedie() != 0.0) {
            validateMedie(studentDTO.getMedie());
            student.setMedie(studentDTO.getMedie());
        }
        if (studentDTO.getDataInscrierii() != null) {
            validateDataInscrierii(studentDTO.getDataInscrierii());
            student.setDataInscrierii(studentDTO.getDataInscrierii());
        }
        student.setBursier(studentDTO.isBursier());
    }

    private void handleCourseRelationships(Student student, List<Long> cursuriIds) {
        if (cursuriIds != null && !cursuriIds.isEmpty()) {
            System.out.println("Handling course relationships for student ID: " + student.getId());
            List<Curs> cursuri = validateAndGetCourses(cursuriIds);

            // Clear existing relationships
            student.getCursuri().forEach(curs -> curs.getStudenti().remove(student));
            student.getCursuri().clear();

            // Set new relationships
            student.setCursuri(cursuri);
            cursuri.forEach(curs -> curs.getStudenti().add(student));
        }
    }

    private List<Curs> validateAndGetCourses(List<Long> cursuriIds) {
        List<Curs> cursuri = cursRepository.findAllById(cursuriIds);
        if (cursuri.size() != cursuriIds.size()) {
            System.out.println("One or more course IDs do not exist");
            throw new IllegalArgumentException("One or more course IDs do not exist");
        }
        return cursuri;
    }

    // Validation methods
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }

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
