package com.kupreychik;

import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.StudentService;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;

import java.util.concurrent.CopyOnWriteArrayList;

public class Solution {
    public static void main(String[] args) {
        JsonParseService jsonParseService = new JsonParseServiceImpl();

        StudentMapper studentMapper = StudentMapper.INSTANCE;
        AllStudentsServiceFromMyComputer servlet = new AllStudentsServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(servlet.getStudents()));
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        System.out.println(studentService.getStudents());
    }
}
