package com.kupreychik;

import com.kupreychik.controller.GroupController;
import com.kupreychik.controller.StudentController;
import com.kupreychik.controller.TeacherController;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.mapper.TeacherMapper;
import com.kupreychik.repository.GroupRepository;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.repository.TeacherRepository;
import com.kupreychik.service.*;
import com.kupreychik.service.impl.GroupServiceImpl;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;
import com.kupreychik.service.impl.TeacherServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.FileConsts.FILE_STUDENTS_GROUP1;
import static com.kupreychik.consts.WebConsts.*;


public class Main {

    private static final JsonParseService jsonParseService = new JsonParseServiceImpl();
    private static StudentMapper studentMapper = StudentMapper.INSTANCE;

    public static void main(String[] args){
        StudentController studentController = getStudentController();
        TeacherController teacherController = getTeacherController();
        GroupController groupController = getGroupController();

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
        server.createContext(TEACHERS_PATH, teacherController);
        server.createContext(GROUPS_PATH, groupController);
        server.start();

    }

    private static StudentController getStudentController() {
        StudentServiceFromMyComputer service = new StudentServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(service.listStudents()));
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        return new StudentController(studentService, jsonParseService);
    }

    private static TeacherController getTeacherController() {
        TeacherMapper teacherMapper = TeacherMapper.INSTANCE;
        TeacherRepository teacherRepository = new TeacherRepository();
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, jsonParseService, teacherMapper);
        return new TeacherController(teacherService, jsonParseService);
    }

    private static GroupController getGroupController() {
        StudentServiceFromMyComputer service1 = new StudentServiceFromMyComputer();
        StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(service1.listStudents()));
        Long idGroup1 = 1L;
        GroupRepository groupRepository = new GroupRepository(studentRepository, idGroup1);
        GroupService groupService = new GroupServiceImpl(jsonParseService, studentMapper, groupRepository);
        return new GroupController(groupService, jsonParseService);
    }


}
