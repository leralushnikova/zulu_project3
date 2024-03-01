package com.kupreychik.service.impl;

import com.kupreychik.dto.request.TimeTableRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.TimeTableResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.mapper.TimeTableMapper;
import com.kupreychik.middleware.DateTimeMiddleWare;
import com.kupreychik.middleware.Middleware;
import com.kupreychik.model.Group;
import com.kupreychik.model.Student;
import com.kupreychik.model.Teacher;
import com.kupreychik.model.TimeTable;
import com.kupreychik.repository.GroupRepository;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.repository.TeacherRepository;
import com.kupreychik.repository.TimeTableRepository;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.TimeTableService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kupreychik.consts.RegexConsts.DATE_FORMAT;

@Slf4j
public class TimeTableServiceImpl implements TimeTableService {
    private final String error = "Ошибка. Расписание не найдено";
    private final TimeTableRepository timeTableRepository;
    private final TimeTableMapper timeTableMapper;
    private final JsonParseService jsonParseService;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    private final Middleware middleware = Middleware.link(
            new DateTimeMiddleWare()
    );

    public TimeTableServiceImpl(TimeTableRepository timeTableRepository, JsonParseService jsonParseService, TimeTableMapper timeTableMapper, TeacherRepository teacherRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableMapper = timeTableMapper;
        this.jsonParseService = jsonParseService;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @SneakyThrows
    public String getTimeTableByNumberOfGroup(Long groupNumber){
        log.info("Поиск расписания по номеру группы");
        try {
            Group group = groupRepository.getGroupByNumber(groupNumber);
            var listTimeTableByGroupNumber = timeTableRepository.getTimeTablesByNumberOfGroup(group);
            log.info("Расписание найдено");
            return jsonParseService.writeToJson(listTimeTableByGroupNumber);
        } catch (ModelNotFound e) {
            log.info(error);
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found timetable by groupNumber"));
        }
    }


    @Override
    @SneakyThrows
    public String getTimeTableBySurnameOfStudent(String surname) {
        log.info("Поиск расписания по фамилии студента");
        try {
            Student student = studentRepository.getStudentBySurname(surname);
            Group group = groupRepository.getGroupBySurnameOfStudent(student);
            var listTimeTableByGroupNumber = timeTableRepository.getTimeTablesByNumberOfGroup(group);
            log.info("Расписание найдено");
            return jsonParseService.writeToJson(listTimeTableByGroupNumber);
        } catch (Exception e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found timetable for groupNumber"));
        }
    }
    @SneakyThrows
    @Override
    public String getTimeTableBySurnameOfTeacher(String surname){
        log.info("Поиск расписания по фамилии учителя");
        try {
            Teacher teacher = teacherRepository.getTeacherBySurname(surname);
            var listTimeTableByGroupNumber = timeTableRepository.getTimeTablesByTeacher(teacher);
            log.info("Расписание найдено");
            return jsonParseService.writeToJson(listTimeTableByGroupNumber);
        } catch (Exception e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found timetable for teacher"));
        }
    }

    @SneakyThrows
    @Override
    public String getTimeTableByDate(String date){
        log.info("Поиск расписания по дате");
        try {
            var listTimeTableByGroupNumber = timeTableRepository.getTimeTablesByDate(parseUrlDateToFormat(date));
            log.info("Расписание найдено");
            return jsonParseService.writeToJson(listTimeTableByGroupNumber);
        } catch (ParseException e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found timetable by date"));
        }
    }

    @Override
    public String changeTimeTableByDate(String changeHours, String date) throws JsonParseException {
        log.info("Изменения расписания по дате");
        try {
            var listTimeTableByGroupNumber = timeTableRepository.getTimeTableByDate(changeHours, parseUrlDateToFormat(date));
            log.info("Расписание найдено");
            return jsonParseService.writeToJson(listTimeTableByGroupNumber);
        } catch (ParseException | ModelNotFound e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found timetable by date"));
        }
    }

    @Override
    @SneakyThrows
    public String save(TimeTableRequest timeTableRequest) {
        log.info("saveTimeTable() method call with value {}", timeTableRequest);

        if (!middleware.check(timeTableRequest) || !checkGroupId(timeTableRequest)
        || !checkTeacherId(timeTableRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse("Cannot create timetable"));
        }

        TimeTable timeTableToSave = timeTableMapper.mapToModelRequest(timeTableRequest);
        var result = timeTableRepository.save(timeTableToSave);
        return jsonParseService.writeToJson(timeTableMapper.mapToResponse(result));

    }


    @Override
    public List<TimeTableResponse> getTimeTables() {
        log.info("getStudents() method call");
        return timeTableRepository.getAllTimeTable()
                .stream()
                .map(timeTableMapper::mapToResponse)
                .toList();
    }

    private boolean checkGroupId(TimeTableRequest timeTableRequest) {
        log.debug("Проверка groupId {}", timeTableRequest);
        try{
            groupRepository.getGroupById(timeTableRequest.getGroupId());
            log.debug("Проверка пройдена. teacherId найдено");
            return true;
        } catch (ModelNotFound e) {
            log.error("Error. groupId не найдено");
            return false;
        }
    }

    private boolean checkTeacherId(TimeTableRequest timeTableRequest) {
        log.debug("Проверка teacherId {}", timeTableRequest);
        try{
            teacherRepository.getTeacherById(timeTableRequest.getTeacherId());
            log.debug("Проверка пройдена. teacherId найдено");
            return true;
        } catch (ModelNotFound e) {
            log.error("Error. teacherId не найдено");
            return false;
        }
    }

    private String parseUrlDateToFormat(String dateString) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyy");
        Date date = dt.parse(dateString);
        SimpleDateFormat dt1 = new SimpleDateFormat(DATE_FORMAT);
        return dt1.format(date);
    }

}
