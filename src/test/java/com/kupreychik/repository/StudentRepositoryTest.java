package com.kupreychik.repository;

import com.kupreychik.model.Student;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class StudentRepositoryTest {
    AllStudentsServiceFromMyComputer servlet = new AllStudentsServiceFromMyComputer();
    StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(servlet.getStudents()));

    @Test
    void shouldFindStudent() {
        Student student = Student.builder()
                .name("Valeriya")
                .surname("Lushnikova")
                .phone("89060584565")
                .birthday(LocalDate.parse("1992-11-29"))
                .build();
        studentRepository.save(student);
        Student studentFromRepository = studentRepository.getAllStudents()
                .stream()
                .filter(student::equals)
                .findFirst()
                .orElseThrow();
        assertEquals(student, studentFromRepository);
    }

    @SneakyThrows
    @Test
    void shouldNotFindStudent() {
        Student student = Student.builder()
                .name("Salavat")
                .surname("Valitov")
                .build();
        Student studentFromRepository = studentRepository.getStudentBySurname(student.getSurname(), student.getName());
        studentRepository.delete(student);
        assertNotEquals(student, studentFromRepository);
    }

}