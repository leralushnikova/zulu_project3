package com.kupreychik.controller;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.TeacherService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.kupreychik.consts.WebConsts.*;

public class TeacherController implements HttpHandler {
    private final TeacherService teacherService;
    private final JsonParseService jsonParseService;

    public TeacherController(TeacherService teacherService, JsonParseService jsonParseService) {
        this.teacherService = teacherService;
        this.jsonParseService = jsonParseService;
    }

    @Override
    @SneakyThrows
    public void handle(HttpExchange exchange){
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), TeacherRequest.class);
                //если записывать на кириллице, то ответа не будет, нужно понять, где charset=UTF-8 нужно прописать?
                sendOk(exchange, teacherService.save((TeacherRequest) teacherRequest));
            }

            case GET -> {
                String responseAsString;

                String url = String.valueOf(exchange.getRequestURI());

                if (url.equals(TEACHERS_PATH)) {
                    var response = teacherService.getTeachers();
                    responseAsString = jsonParseService.writeToJson(response);
                } else {
                    if (url.contains("surname")) {
                        String paramSurname = url.substring(url.indexOf("surname=") + 8, url.lastIndexOf("&"));
                        String paramName = url.substring(url.indexOf("&name=") + 6);
                        // Если в конце прописывать числа, то на выходе получаем другое имя. Почему так?
                        responseAsString = teacherService.getTeacherBySurname(paramSurname, paramName);
                    } else {
                        String id = url.replace(TEACHERS_PATH + "/", "");
                        responseAsString = teacherService.getTeacherById(Long.parseLong(id));
                    }
                }
                sendOk(exchange, responseAsString);

            }

            case DELETE -> {
                String url = String.valueOf(exchange.getRequestURI());
                String id = url.replace(TEACHERS_PATH + "/", "");
                var responseAsString = teacherService.delete(Long.parseLong(id));
                sendOk(exchange, responseAsString);
            }

            case PUT -> {
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                String url = String.valueOf(exchange.getRequestURI());
                String id = url.replace(TEACHERS_PATH + "/", "");
                var responseAsString = teacherService.change(Long.parseLong(id), (TeacherRequest) teacherRequest);
                sendOk(exchange, responseAsString);
            }

        }

    }

    private static void sendOk(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
