package com.yettensyvus.sms.controller;

import com.yettensyvus.sms.dto.CursDTO;
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
    public List<CursDTO> getAll() {
        return cursService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursDTO> getById(@PathVariable Long id) {
        return cursService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursDTO> create(@RequestBody CursDTO cursDTO) {
        CursDTO savedCursDTO = cursService.save(cursDTO);
        return ResponseEntity.ok(savedCursDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursDTO> update(@PathVariable Long id, @RequestBody CursDTO cursDTO) {
        CursDTO updatedCursDTO = cursService.update(id, cursDTO);
        return ResponseEntity.ok(updatedCursDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CursDTO> patch(@PathVariable Long id, @RequestBody CursDTO cursDTO) {
        CursDTO patchedCursDTO = cursService.patch(id, cursDTO);
        return ResponseEntity.ok(patchedCursDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursService.delete(id);
        return ResponseEntity.noContent().build();
    }
}