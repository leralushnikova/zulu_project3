package com.kupreychik.controller;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.kupreychik.consts.RegexConsts.SIGNWEB_FORMAT;
import static com.kupreychik.consts.WebConsts.*;

public class StudentController implements HttpHandler {
    private final StudentService studentService;
    private final JsonParseService jsonParseService;

    public StudentController(StudentService studentService, JsonParseService jsonParseService) {
        this.studentService = studentService;
        this.jsonParseService = jsonParseService;
    }

    @Override
    @SneakyThrows
    public void handle(HttpExchange exchange){
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                //если записывать на кириллице, то ответа не будет, нужно понять, где charset=UTF-8 нужно прописать?
                sendOk(exchange, studentService.save((StudentRequest) studentRequest));
            }

            case GET -> {
                String responseAsString;

                String url = String.valueOf(exchange.getRequestURI());

                if (url.equals(STUDENT_PATH)) {
                    var response = studentService.getStudents();
                    responseAsString = jsonParseService.writeToJson(response);
                } else {
                    if (url.contains("surname")) {
                        String paramSurname = url.substring(url.indexOf("surname=") + 8, url.lastIndexOf("&"));
                        String paramName = url.substring(url.indexOf("&name=") + 6);
                        // Если в конце прописывать числа, то на выходе получаем другое имя. Почему так?
                        responseAsString = studentService.getStudentBySurnameAndName(paramSurname, paramName);
                    } else {
                        String id = url.replace(STUDENT_PATH + "/", "");
                        if (id.matches(SIGNWEB_FORMAT)) {
                            responseAsString = studentService.getStudentById(Long.parseLong(id));
                        } else {
                            responseAsString = getError();
                        }
                    }
                }

                sendOk(exchange, responseAsString);
            }

            case DELETE -> {
                String url = String.valueOf(exchange.getRequestURI());
                String id = url.replace(STUDENT_PATH + "/", "");
                String responseAsString;

                if(id.matches(SIGNWEB_FORMAT)) {
                    responseAsString = studentService.delete(Long.parseLong(id));
                } else responseAsString = getError();

                sendOk(exchange, responseAsString);
            }

            case PUT -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                String url = String.valueOf(exchange.getRequestURI());
                String id = url.replace(STUDENT_PATH + "/", "");

                String responseAsString;
                if(id.matches(SIGNWEB_FORMAT)) {
                    responseAsString = studentService.change(Long.parseLong(id), (StudentRequest) studentRequest);
                } else responseAsString = getError();
                sendOk(exchange, responseAsString);
            }
        }
    }

    private void sendOk(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    @SneakyThrows
    private String getError(){
        return jsonParseService.writeToJson(new ErrorResponse("Wrong request"));
    }
}
