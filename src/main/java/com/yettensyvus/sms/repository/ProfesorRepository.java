package com.yettensyvus.sms.repository;

import com.yettensyvus.sms.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}