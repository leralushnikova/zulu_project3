package com.kupreychik.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Group {
    @Setter
    private List<Student> students;
    private final long groupId;
    {
        groupId = ++Stat.id;
    }

    public Group(List<Student> students) {
        this.students = students;
    }

    private static class Stat{
        private static long id = 0;
    }
}