package com.kupreychik.controller;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;

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

                sendOk(exchange, studentService.save((StudentRequest) studentRequest));
            }

            case GET -> {
                var response = studentService.getStudents();
                var responseAsString = jsonParseService.writeToJson(response);
                String uri = String.valueOf(exchange.getRequestURI());
//                sendOk(exchange, uri);
                sendOk(exchange, responseAsString);

            }

           /* case DELETE -> {}

            case PUT -> {}*/

        }

    }

    private static void sendOk(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
