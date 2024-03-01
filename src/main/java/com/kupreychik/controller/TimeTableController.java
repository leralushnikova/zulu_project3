package com.kupreychik.controller;

import com.kupreychik.dto.request.TimeTableRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.TimeTableService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.kupreychik.consts.RegexConsts.SIGNWEB_FORMAT;
import static com.kupreychik.consts.WebConsts.*;


public class TimeTableController implements HttpHandler {
    private final TimeTableService timeTableService;
    private final JsonParseService jsonParseService;


    public TimeTableController(TimeTableService timeTableService, JsonParseService jsonParseService) {
        this.timeTableService = timeTableService;
        this.jsonParseService = jsonParseService;
    }




    @Override
    @SneakyThrows
    public void handle(HttpExchange exchange){
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var timeTableRequest = jsonParseService.readObject(exchange.getRequestBody(), TimeTableRequest.class);
                //если записывать на кириллице, то ответа не будет, нужно понять, где charset=UTF-8 нужно прописать?
                sendOk(exchange, timeTableService.save((TimeTableRequest) timeTableRequest));
            }

            case GET -> {
                String responseAsString;

                String url = String.valueOf(exchange.getRequestURI());

                if (url.equals(TIMETABLE_PATH)) {
                    var response = timeTableService.getTimeTables();
                    responseAsString = jsonParseService.writeToJson(response);
                } else {
                    String param = url.substring((url.indexOf("=") + 1));
                    if(url.contains("groupNumber")){
                        if(param.matches(SIGNWEB_FORMAT)) {
                            responseAsString = timeTableService.getTimeTableByNumberOfGroup(Long.parseLong(param));
                        } else responseAsString = getError();
                    } else if(url.contains("studentSurname")){
                        responseAsString = timeTableService.getTimeTableBySurnameOfStudent(param);
                    } else if(url.contains("teacherSurname")){
                        responseAsString = timeTableService.getTimeTableBySurnameOfTeacher(param);
                    } else if(url.contains("date")){
                        responseAsString = timeTableService.getTimeTableByDate(param);

                    } else responseAsString = getError();
                }

                sendOk(exchange, responseAsString);
            }


            case PUT -> {
                try (InputStreamReader inputStream = new InputStreamReader(exchange.getRequestBody());
                     BufferedReader bufferedReader = new BufferedReader(inputStream)){
                    int b;
                    StringBuilder changeHours = new StringBuilder();
                    while ((b = bufferedReader.read()) != -1) {
                        changeHours.append((char) b);
                    }
                    String responseAsString;
                    String url = String.valueOf(exchange.getRequestURI());
                    String param = url.substring((url.indexOf("=") + 1));

                    responseAsString = timeTableService.changeTimeTableByDate(changeHours.toString(), param);

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
