package com.kupreychik.controller;

import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.TeacherService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.kupreychik.consts.RegexConsts.SIGNWEB_FORMAT;
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
                } else responseAsString = getError();

                sendOk(exchange, responseAsString);

            }

            case DELETE -> {
                String url = String.valueOf(exchange.getRequestURI());
                String id = url.replace(TEACHERS_PATH + "/", "");
                String responseAsString;

                if(id.matches(SIGNWEB_FORMAT)) {
                    responseAsString = teacherService.delete(Long.parseLong(id));
                } else responseAsString = getError();

                sendOk(exchange, responseAsString);
            }

            case PUT -> {
                try (InputStreamReader inputStream = new InputStreamReader(exchange.getRequestBody());
                     BufferedReader bufferedReader = new BufferedReader(inputStream)){
                    int b;
                    StringBuilder item = new StringBuilder();
                    while ((b = bufferedReader.read()) != -1) {
                        item.append((char) b);
                    }
                    String url = String.valueOf(exchange.getRequestURI());
                    String id = url.replace(TEACHERS_PATH + "/", "");

                    String responseAsString;

                    if(id.matches(SIGNWEB_FORMAT)) {
                        responseAsString = teacherService.change(Long.parseLong(id), item.toString());
                    } else responseAsString = getError();

                    sendOk(exchange, responseAsString);
                }
            }
        }
    }

    private static void sendOk(HttpExchange exchange, String response) throws IOException {
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
