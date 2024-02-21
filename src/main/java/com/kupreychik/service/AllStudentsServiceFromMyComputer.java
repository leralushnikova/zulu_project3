package com.kupreychik.service;

import com.kupreychik.model.Student;

import java.util.ArrayList;
import java.util.List;

import static com.kupreychik.consts.FileConsts.FILE_STUDENTS_GROUP1;
import static com.kupreychik.consts.FileConsts.FILE_STUDENTS_GROUP2;

public class AllStudentsServiceFromMyComputer {
    public final StudentServiceFromMyComputer service1 = new StudentServiceFromMyComputer(FILE_STUDENTS_GROUP1);
    public final StudentServiceFromMyComputer service2 = new StudentServiceFromMyComputer(FILE_STUDENTS_GROUP2);

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        students.addAll(service1.listStudents());
        students.addAll(service2.listStudents());
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setId((long) i + 1);
        }
        return students;
    }
}