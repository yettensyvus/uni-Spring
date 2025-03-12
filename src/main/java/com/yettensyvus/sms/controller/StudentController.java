package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.model.Student;
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
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.save(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patch(@PathVariable Long id, @RequestBody Student student) {
        return studentService.findById(id)
                .map(existing -> {
                    if (student.getNume() != null) existing.setNume(student.getNume());
                    if (student.getVarsta() != 0) existing.setVarsta(student.getVarsta());
                    if (student.getEmail() != null) existing.setEmail(student.getEmail());
                    if (student.getSpecialitate() != null) existing.setSpecialitate(student.getSpecialitate());
                    if (student.getAnStudiu() != 0) existing.setAnStudiu(student.getAnStudiu());
                    if (student.getMedie() != 0.0) existing.setMedie(student.getMedie());
                    existing.setBursier(student.isBursier()); // No null check since boolean has a default value
                    if (student.getDataInscrierii() != null) existing.setDataInscrierii(student.getDataInscrierii());
                    return studentService.save(existing);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}