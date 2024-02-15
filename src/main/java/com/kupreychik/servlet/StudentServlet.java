package com.kupreychik.servlet;

import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.StudentService;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.impl.StudentServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.WebConsts.STUDENTS_PATH;

@WebServlet(STUDENTS_PATH)
public class StudentServlet extends HttpServlet {
    JsonParseServiceImpl jsonParseService = new JsonParseServiceImpl();
    StudentMapper studentMapper = StudentMapper.INSTANCE;
    AllStudentsServiceFromMyComputer servlet = new AllStudentsServiceFromMyComputer();
    StudentRepository studentRepository = new StudentRepository(new CopyOnWriteArrayList<>(servlet.getStudents()));
    StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        var response = studentService.getStudents();
        List<String> listStudents = new ArrayList<>();
        for (StudentResponse studentResponse : response) {
            var responseAsString = jsonParseService.writeToJson(studentResponse);
            listStudents.add(responseAsString);
        }
        req.setAttribute("listStudents", listStudents);
        getServletContext().getRequestDispatcher("/students.jsp").forward(req, resp);
    }

     /*@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileWriter writer = new FileWriter(Group1Service.fileGroup, true);
        *//*String student = req.getParameter();
        writer.write(student);*//*
        writer.close();
    }*/

    private static void sendOk(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json"); // выводит данные в формате json
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
