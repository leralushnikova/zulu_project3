package com.kupreychik.controller;

import com.kupreychik.dto.request.GroupRequestDouble;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.service.GroupService;
import com.kupreychik.service.JsonParseService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.kupreychik.consts.RegexConsts.SIGNWEB_FORMAT;
import static com.kupreychik.consts.WebConsts.*;


public class GroupController implements HttpHandler {
    private final GroupService groupService;
    private final JsonParseService jsonParseService;

    public GroupController(GroupService groupService, JsonParseService jsonParseService) {
        this.groupService = groupService;
        this.jsonParseService = jsonParseService;
    }

    @Override
    @SneakyThrows
    public void handle(HttpExchange exchange){
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var groupRequest = jsonParseService.readObject(exchange.getRequestBody(), GroupRequestDouble.class);
                //если записывать на кириллице, то ответа не будет, нужно понять, где charset=UTF-8 нужно прописать?
                sendOk(exchange, groupService.save((GroupRequestDouble) groupRequest));
            }

            case GET -> {
                String responseAsString;

                String url = String.valueOf(exchange.getRequestURI());

                if (url.equals(GROUPS_PATH)) {
                    var response = groupService.getGroups();
                    responseAsString = jsonParseService.writeToJson(response);
                } else {
                    if (url.contains("number")) {
                        Long number = Long.parseLong(url.substring((url.indexOf("=") + 1)));
                        responseAsString = groupService.getGroupByNumber(number);
                    } else if (url.contains("surname")) {
                        String paramSurname = url.substring((url.indexOf("=") + 1));
                        responseAsString = groupService.getGroupBySurnameOfStudent(paramSurname);
                    } else responseAsString = getError();
                }

                sendOk(exchange, responseAsString);
            }


            case PUT -> {
                try (InputStreamReader inputStream = new InputStreamReader(exchange.getRequestBody());
                     BufferedReader bufferedReader = new BufferedReader(inputStream)){
                    int b;
                    StringBuilder idStudent = new StringBuilder();
                    while ((b = bufferedReader.read()) != -1) {
                        idStudent.append((char) b);
                    }
                    String responseAsString;
                    String url = String.valueOf(exchange.getRequestURI());
                    String idGroup = url.replace(GROUPS_PATH + "/", "");

                    if (idGroup.matches(SIGNWEB_FORMAT) && idStudent.toString().matches(SIGNWEB_FORMAT)) {
                        responseAsString = groupService.addStudentInGroup(Long.parseLong(idGroup), Long.parseLong(idStudent.toString()));
                    } else {
                        responseAsString = getError();
                    }

                    sendOk(exchange, responseAsString);
                }
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
