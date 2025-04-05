package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.dto.ProfesorDTO;
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
    public List<ProfesorDTO> getAllProfesori() {
        return profesorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> getById(@PathVariable Long id) {
        return profesorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfesorDTO> create(@RequestBody ProfesorDTO profesorDTO) {
        ProfesorDTO savedProfesorDTO = profesorService.save(profesorDTO);
        return ResponseEntity.ok(savedProfesorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> update(@PathVariable Long id, @RequestBody ProfesorDTO profesorDTO) {
        ProfesorDTO updatedProfesorDTO = profesorService.update(id, profesorDTO);
        return ResponseEntity.ok(updatedProfesorDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfesorDTO> patch(@PathVariable Long id, @RequestBody ProfesorDTO profesorDTO) {
        ProfesorDTO patchedProfesorDTO = profesorService.patch(id, profesorDTO);
        return ResponseEntity.ok(patchedProfesorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profesorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}