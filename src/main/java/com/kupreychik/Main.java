package com.kupreychik;

import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;

import java.util.concurrent.CopyOnWriteArrayList;


public class Main {

    public static void main(String[] args) {
        JsonParseServiceImpl jsonParseService = new JsonParseServiceImpl();
        StudentMapper studentMapper = StudentMapper.INSTANCE;
        AllStudentsServiceFromMyComputer students = new AllStudentsServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(students.getStudents()));
        StudentServiceImpl service = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);

        System.out.println(service.getStudents());
    }



}
