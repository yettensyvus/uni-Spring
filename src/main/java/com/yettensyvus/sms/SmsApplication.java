package com.yettensyvus.sms;

import com.yettensyvus.sms.model.Curs;
import com.yettensyvus.sms.model.Profesor;
import com.yettensyvus.sms.model.Student;
import com.yettensyvus.sms.repository.CursRepository;
import com.yettensyvus.sms.repository.ProfesorRepository;
import com.yettensyvus.sms.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SmsApplication implements CommandLineRunner {

	private final ApplicationContext context;
	private final StudentRepository studentRepository;
	private final ProfesorRepository profesorRepository;
	private final CursRepository cursRepository;

	public SmsApplication(ApplicationContext context,
						  StudentRepository studentRepository,
						  ProfesorRepository profesorRepository,
						  CursRepository cursRepository) {
		this.context = context;
		this.studentRepository = studentRepository;
		this.profesorRepository = profesorRepository;
		this.cursRepository = cursRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

	@Override
	@Transactional // Ensure this method runs within a transaction
	public void run(String... args) {
		System.out.println("Application has started successfully!");

		try {
			testStudentCrud();
			testProfesorCrud();
			testCursCrud();
			testRelationships();
			System.out.println("Database connection test completed successfully!");
		} catch (Exception e) {
			System.err.println("Database connection test failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void testStudentCrud() {
		// Create and save a new student
		Student testStudent = new Student(
				"John Doe", 20, "john.doe@example.com", "Informatica",
				2, 8.5, true, LocalDate.now(), null
		);
		Student savedStudent = studentRepository.save(testStudent);
		System.out.println("Saved student: " + savedStudent);

		// Retrieve the student
		studentRepository.findById(savedStudent.getStudentId())
				.ifPresent(student -> System.out.println("Retrieved student: " + student));

		// Update the student
		savedStudent.setNume("Jane Doe");
		savedStudent.setMedie(9.0);
		studentRepository.save(savedStudent);
		System.out.println("Updated student: " + savedStudent);
	}

	private void testProfesorCrud() {
		// Create and save a new profesor
		Profesor testProfesor = new Profesor("Maria Popescu", "Matematica", 10, null);
		Profesor savedProfesor = profesorRepository.save(testProfesor);
		System.out.println("Saved profesor: " + savedProfesor);

		// Retrieve the profesor
		profesorRepository.findById(savedProfesor.getId())
				.ifPresent(profesor -> System.out.println("Retrieved profesor: " + profesor));

		// Update the profesor
		savedProfesor.setExperientaAni(12);
		profesorRepository.save(savedProfesor);
		System.out.println("Updated profesor: " + savedProfesor);
	}

	private void testCursCrud() {
		// First, ensure a profesor exists for the curs
		Profesor profesorForCurs = new Profesor("Ion Ionescu", "Fizica", 8, null);
		Profesor savedProfesor = profesorRepository.save(profesorForCurs);

		// Create and save a new curs
		Curs testCurs = new Curs("Fizica Avansata", 6, savedProfesor, null);
		Curs savedCurs = cursRepository.save(testCurs);
		System.out.println("Saved curs: " + savedCurs);

		// Retrieve the curs
		cursRepository.findById(savedCurs.getId())
				.ifPresent(curs -> System.out.println("Retrieved curs: " + curs));

		// Update the curs
		savedCurs.setCredite(7);
		cursRepository.save(savedCurs);
		System.out.println("Updated curs: " + savedCurs);
	}

	@Transactional // Ensure this method runs within a transaction
    protected void testRelationships() {
		// Create a profesor
		Profesor profesor = new Profesor("Ana Vasile", "Chimie", 5, null);
		Profesor savedProfesor = profesorRepository.save(profesor);

		// Create a student
		Student student = new Student("Mihai Popescu", 21, "mihai.popescu@example.com", "Chimie", 3, 8.7, true, LocalDate.now(), null);
		Student savedStudent = studentRepository.save(student);

		// Create a curs and associate it with the profesor and student
		Curs curs = new Curs("Chimie Organica", 5, savedProfesor, Arrays.asList(savedStudent));
		Curs savedCurs = cursRepository.save(curs);

		// Retrieve the curs and verify the relationships
		cursRepository.findById(savedCurs.getId()).ifPresent(c -> {
			System.out.println("Curs with profesor and students: " + c);
			System.out.println("Profesor: " + c.getProfesor().getNume());
			System.out.println("Students: " + c.getStudenti().stream().map(Student::getNume).toList());
		});
	}
}