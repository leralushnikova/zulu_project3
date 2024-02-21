package com.kupreychik.repository;

import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;


class StudentRepositoryTest {
    AllStudentsServiceFromMyComputer servlet = new AllStudentsServiceFromMyComputer();
    StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(servlet.getStudents()));

    @Test
    void shouldFindStudent() {

    }

}