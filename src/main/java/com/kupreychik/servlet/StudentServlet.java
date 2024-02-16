package com.kupreychik.servlet;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.AllStudentsServiceFromMyComputer;
import com.kupreychik.service.StudentService;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import com.kupreychik.service.impl.StudentServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;


import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.RegexConsts.SIGNWEB_FORMAT;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String signAfterSlash = req.getPathInfo();

        String paramSurname = req.getParameter("surname");
        String paramName = req.getParameter("name");

        if (StringUtils.isNotBlank(paramSurname) && StringUtils.isNotBlank(paramName)) {
            var responseStudent = studentService.getStudentsBySurname(paramSurname, paramName);
            sendOk(req, resp, responseStudent);
        } else {
            if (signAfterSlash == null) {
                var response = studentService.getStudents();
                var responseAsStringStudents = jsonParseService.writeToJson(response);
                sendOk(req, resp, responseAsStringStudents);
            } else {
                String id = req.getPathInfo().replace("/", "");
                if (id.matches(SIGNWEB_FORMAT)) {
                    var responseStudents = studentService.getStudentById(Long.parseLong(id));
                    sendOk(req, resp, responseStudents);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var studentRequest = jsonParseService.readObject(req.getInputStream(), StudentRequest.class);
        sendOk(req, resp, studentService.save((StudentRequest) studentRequest));
    }

   /* @Override
    @SneakyThrows
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    @SneakyThrows
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String paramSurname = req.getParameter("surname");
        String paramName = req.getParameter("name");

        if (StringUtils.isNotBlank(paramSurname) && StringUtils.isNotBlank(paramName)) {
            var student = studentService.getStudentsBySurname(paramSurname, paramName);
            studentService.delete(student);
            sendOkStudent(req, resp, student);

        }
    }*/



    @SneakyThrows
    private void sendOk(HttpServletRequest req, HttpServletResponse resp, Object object) {
        req.setAttribute("object", object);
        getServletContext().getRequestDispatcher("/students.jsp").forward(req, resp);
    }

}
