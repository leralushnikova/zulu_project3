package com.kupreychik.controller;

import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.service.GroupService;
import com.kupreychik.service.JsonParseService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.kupreychik.consts.WebConsts.GET;
import static com.kupreychik.consts.WebConsts.GROUPS_PATH;


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
            /*case POST -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                //если записывать на кириллице, то ответа не будет, нужно понять, где charset=UTF-8 нужно прописать?
                sendOk(exchange, studentService.save((StudentRequest) studentRequest));
            }*/

            case GET -> {
                String responseAsString;

                String url = String.valueOf(exchange.getRequestURI());

                if (url.equals(GROUPS_PATH)) {
                    var response = groupService.getStudents();
                    responseAsString = jsonParseService.writeToJson(response);
                } else responseAsString = getError();

                sendOk(exchange, responseAsString);
            }

            /*case DELETE -> {
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
            }*/
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
