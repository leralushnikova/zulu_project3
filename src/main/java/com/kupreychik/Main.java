package com.kupreychik;

import com.kupreychik.controller.GroupController;
import com.kupreychik.controller.StudentController;
import com.kupreychik.controller.TeacherController;
import com.kupreychik.controller.TimeTableController;
import com.kupreychik.mapper.GroupMapper;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.mapper.TeacherMapper;
import com.kupreychik.mapper.TimeTableMapper;
import com.kupreychik.repository.GroupRepository;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.repository.TeacherRepository;
import com.kupreychik.repository.TimeTableRepository;
import com.kupreychik.service.*;
import com.kupreychik.service.impl.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.WebConsts.*;


public class Main {

    private static final JsonParseService jsonParseService = new JsonParseServiceImpl();
    private static final StudentMapper studentMapper = StudentMapper.INSTANCE;
    private static final StudentServiceFromMyComputer service = new StudentServiceFromMyComputer();
    private static final StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(service.listStudents()));
    private static final TeacherRepository teacherRepository = new TeacherRepository();
    private static final GroupRepository groupRepository = new GroupRepository();

    public static void main(String[] args){
        StudentController studentController = getStudentController();
        TeacherController teacherController = getTeacherController();
        GroupController groupController = getGroupController();
        TimeTableController timeTableController = getTimeTableController();

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
        server.createContext(TIMETABLE_PATH, timeTableController);
        server.start();

    }

    private static StudentController getStudentController() {
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        return new StudentController(studentService, jsonParseService);
    }

    private static TeacherController getTeacherController() {
        TeacherMapper teacherMapper = TeacherMapper.INSTANCE;
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, jsonParseService, teacherMapper);
        return new TeacherController(teacherService, jsonParseService);
    }

    private static GroupController getGroupController() {
        GroupMapper groupMapper = GroupMapper.INSTANCE;
        GroupService groupService = new GroupServiceImpl(groupRepository, jsonParseService, groupMapper, studentRepository);
        return new GroupController(groupService, jsonParseService);
    }

    private static TimeTableController getTimeTableController() {
        TimeTableMapper timeTableMapper = TimeTableMapper.INSTANCE;
        TimeTableRepository timeTableRepository = new TimeTableRepository();
        TimeTableService timeTableService = new TimeTableServiceImpl(timeTableRepository, jsonParseService, timeTableMapper, teacherRepository, groupRepository, studentRepository);
        return new TimeTableController(timeTableService, jsonParseService);
    }


}
