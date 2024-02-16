package com.kupreychik;

import com.kupreychik.exception.JsonParseException;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.model.Student;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Main {

    public static void main(String[] args) throws JsonParseException {
        JsonParseServiceImpl jsonParseService = new JsonParseServiceImpl();
//        StudentMapper studentMapper = StudentMapper.INSTANCE;
        AllStudentsServiceFromMyComputer students = new AllStudentsServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(students.getStudents()));
//        StudentServiceImpl service = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);

        List<Student> list = studentRepository.getAllStudents();
        List<String> listStudents = new ArrayList<>();
        for (Student student : list) {
            var responseAsString = jsonParseService.writeToJson(student);
            listStudents.add(responseAsString);
        }
        String id = "/1".replace("/", "");;
        if (id.matches("\\d+")) {
            String student = listStudents.get(Integer.parseInt(id) - 1);
            System.out.println(student);
        }
//        System.out.println(listStudents);
    }



}
