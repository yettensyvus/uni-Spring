package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.service.ProfesorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesori")
public class ProfesorController {
    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    public List<Profesor> getAll() {
        return profesorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getById(@PathVariable Long id) {
        return profesorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Profesor create(@RequestBody Profesor profesor) {
        return profesorService.save(profesor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> update(@PathVariable Long id, @RequestBody Profesor profesor) {
        return ResponseEntity.ok(profesorService.update(id, profesor));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Profesor> patch(@PathVariable Long id, @RequestBody Profesor profesor) {
        return profesorService.findById(id)
                .map(existing -> {
                    if (profesor.getNume() != null) existing.setNume(profesor.getNume());
                    if (profesor.getMaterie() != null) existing.setMaterie(profesor.getMaterie());
                    if (profesor.getExperientaAni() != 0) existing.setExperientaAni(profesor.getExperientaAni());
                    return profesorService.save(existing);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profesorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}