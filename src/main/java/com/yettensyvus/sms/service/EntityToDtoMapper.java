// Într-o clasă de serviciu sau utilitară
package com.yettensyvus.sms.service;

import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.model.Student;
import com.yettensyvus.sms.dto.CursDTO;
import com.yettensyvus.sms.dto.ProfesorDTO;
import com.yettensyvus.sms.dto.StudentDTO;

import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public static ProfesorDTO toProfesorDTO(Profesor profesor) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(profesor.getId());
        dto.setNume(profesor.getNume());
        dto.setMaterie(profesor.getMaterie());
        dto.setExperientaAni(profesor.getExperientaAni());
        dto.setCursuriIds(profesor.getCursuri().stream()
                .map(Curs::getId)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Profesor toProfesorEntity(ProfesorDTO dto) {
        Profesor profesor = new Profesor();
        profesor.setId(dto.getId());
        profesor.setNume(dto.getNume());
        profesor.setMaterie(dto.getMaterie());
        profesor.setExperientaAni(dto.getExperientaAni());
        return profesor;
    }

    public static CursDTO toCursDTO(Curs curs) {
        CursDTO dto = new CursDTO();
        dto.setId(curs.getId());
        dto.setDenumire(curs.getDenumire());
        dto.setCredite(curs.getCredite());
        dto.setProfesorId(curs.getProfesor() != null ? curs.getProfesor().getId() : null);
        dto.setStudentiIds(curs.getStudenti().stream()
                .map(Student::getId)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Curs toCursEntity(CursDTO dto) {
        Curs curs = new Curs();
        curs.setId(dto.getId());
        curs.setDenumire(dto.getDenumire());
        curs.setCredite(dto.getCredite());
        return curs;
    }

    public static StudentDTO toStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setNume(student.getNume());
        dto.setVarsta(student.getVarsta());
        dto.setEmail(student.getEmail());
        dto.setSpecialitate(student.getSpecialitate());
        dto.setAnStudiu(student.getAnStudiu());
        dto.setMedie(student.getMedie());
        dto.setBursier(student.isBursier());
        dto.setDataInscrierii(student.getDataInscrierii());
        dto.setCursuriIds(student.getCursuri().stream()
                .map(Curs::getId)
                .collect(Collectors.toList()));
        return dto;
    }
    public static Student toStudentEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setNume(dto.getNume());
        student.setVarsta(dto.getVarsta());
        student.setEmail(dto.getEmail());
        student.setSpecialitate(dto.getSpecialitate());
        student.setAnStudiu(dto.getAnStudiu());
        student.setMedie(dto.getMedie());
        student.setBursier(dto.isBursier());
        student.setDataInscrierii(dto.getDataInscrierii());
        return student;
    }
}