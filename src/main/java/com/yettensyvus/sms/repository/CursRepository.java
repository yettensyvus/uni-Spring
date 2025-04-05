package com.yettensyvus.sms.repository;

import com.yettensyvus.sms.model.Curs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursRepository extends JpaRepository<Curs, Long> {
}