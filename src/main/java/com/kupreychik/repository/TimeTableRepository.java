package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Group;
import com.kupreychik.model.Teacher;
import com.kupreychik.model.TimeTable;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.consts.RegexConsts.DATETIME_FORMAT;

public class TimeTableRepository {
    private final CopyOnWriteArrayList<TimeTable> timeTables;

    public TimeTableRepository() {
        timeTables = new CopyOnWriteArrayList<>();
    }

    public List<TimeTable> getAllTimeTable(){
        return new ArrayList<>(timeTables);
    }

    public List<TimeTable> getTimeTablesByNumberOfGroup(Group group) {
        List<TimeTable> list = new ArrayList<>();
        for (TimeTable timeTable : timeTables) {
            if (timeTable.getGroupId().equals(group.getId())) {
                list.add(timeTable);
            }
        }
        return list;
    }

    public List<TimeTable> getTimeTablesByTeacher(Teacher teacher) {
        List<TimeTable> list = new ArrayList<>();
        for (TimeTable timeTable : timeTables) {
            if (timeTable.getTeacherId().equals(teacher.getId())) {
                list.add(timeTable);
            }
        }
        return list;
    }

    public List<TimeTable> getTimeTablesByDate(String dateString) throws ParseException {
        List<TimeTable> list = new ArrayList<>();
        for (TimeTable timeTable : timeTables) {
            String dateTimeTable = timeTable.getStartDateTime().toString();
            if (dateTimeTable.contains(dateString)) {
                list.add(timeTable);
            }
        }
        return list;
    }

    public TimeTable getTimeTableByDate(String changeHours, String dateString) throws ParseException, ModelNotFound {
        TimeTable timeTable = getTimeTablesByDate(dateString).stream()
                .findFirst()
                .orElseThrow(ModelNotFound::new);
        String resultDate = dateString + " " + changeHours;
        LocalDateTime start = LocalDateTime.parse(resultDate, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        LocalDateTime end = start.plusHours(2);
        timeTable.setStartDateTime(start);
        timeTable.setEndDateTime(end);
        return timeTable;
    }

    public TimeTable save(TimeTable timeTable){
        timeTables.add(timeTable);
        return timeTable;
    }


}
