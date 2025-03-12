package com.yettensyvus.sms.service;

import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.repository.CursRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursService {
    private final CursRepository cursRepository;

    public CursService(CursRepository cursRepository) {
        this.cursRepository = cursRepository;
    }

    public List<Curs> findAll() {
        return cursRepository.findAll();
    }

    public Optional<Curs> findById(Long id) {
        return cursRepository.findById(id);
    }

    public Curs save(Curs curs) {
        return cursRepository.save(curs);
    }

    public Curs update(Long id, Curs curs) {
        curs.setId(id);
        return cursRepository.save(curs);
    }

    public void delete(Long id) {
        cursRepository.deleteById(id);
    }
}