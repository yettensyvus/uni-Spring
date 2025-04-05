package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.dto.StudentDTO;
import com.yettensyvus.sms.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studenti")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDTO> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO studentDTO) {
        StudentDTO savedStudentDTO = studentService.save(studentDTO);
        return ResponseEntity.ok(savedStudentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudentDTO = studentService.update(id, studentDTO);
        return ResponseEntity.ok(updatedStudentDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentDTO> patch(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        StudentDTO patchedStudentDTO = studentService.patch(id, studentDTO);
        return ResponseEntity.ok(patchedStudentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}