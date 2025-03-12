package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.service.CursService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursuri")
public class CursController {
    private final CursService cursService;

    public CursController(CursService cursService) {
        this.cursService = cursService;
    }

    @GetMapping
    public List<Curs> getAll() {
        return cursService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curs> getById(@PathVariable Long id) {
        return cursService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Curs create(@RequestBody Curs curs) {
        return cursService.save(curs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curs> update(@PathVariable Long id, @RequestBody Curs curs) {
        return ResponseEntity.ok(cursService.update(id, curs));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Curs> patch(@PathVariable Long id, @RequestBody Curs curs) {
        return cursService.findById(id)
                .map(existing -> {
                    if (curs.getDenumire() != null) existing.setDenumire(curs.getDenumire());
                    if (curs.getCredite() != 0) existing.setCredite(curs.getCredite());
                    if (curs.getProfesor() != null) existing.setProfesor(curs.getProfesor());
                    return cursService.save(existing);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursService.delete(id);
        return ResponseEntity.noContent().build();
    }
}