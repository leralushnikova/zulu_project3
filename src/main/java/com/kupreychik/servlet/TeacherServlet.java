package com.kupreychik.servlet;

import com.kupreychik.service.TeacherServiceFromMyComputer;
import com.kupreychik.service.impl.JsonParseServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.util.List;

import static com.kupreychik.consts.WebConsts.TEACHERS_PATH;

@WebServlet(TEACHERS_PATH)
public class TeacherServlet extends HttpServlet {
    private final TeacherServiceFromMyComputer servlet = new TeacherServiceFromMyComputer(new JsonParseServiceImpl());

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
//        List<String> listTeachers = servlet.listJson();
//        req.setAttribute("listTeachers", listTeachers);
        getServletContext().getRequestDispatcher("/teachers.jsp").forward(req, resp);
    }
}

