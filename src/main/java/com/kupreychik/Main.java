package com.kupreychik;

import com.kupreychik.controller.StudentController;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.StudentService;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.WebConsts.STUDENTS_PATH;
import static com.kupreychik.consts.WebConsts.STUDENT_PATH;


public class Main {

    public static void main(String[] args){
        StudentController studentController = getStudentController();

        HttpServer server;
        try {
            System.out.println("Service start");
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);

        } catch (IOException e) {
            //LOGGER.error("Server can't start! Reason: {}", ex.getMessage(), ex);
            return;
        }

        server.createContext(STUDENT_PATH, studentController);
//        server.createContext(STUDENTS_PATH, studentController);
        server.start();

    }

    private static StudentController getStudentController() {
        JsonParseService jsonParseService = new JsonParseServiceImpl();

        StudentMapper studentMapper = StudentMapper.INSTANCE;
        AllStudentsServiceFromMyComputer servlet = new AllStudentsServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(servlet.getStudents()));
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        return new StudentController(studentService, jsonParseService);
    }


}
