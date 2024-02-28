package com.kupreychik;


import com.kupreychik.service.TeacherServiceFromMyComputer;


public class Solution {
    static TeacherServiceFromMyComputer teachers = new TeacherServiceFromMyComputer();
    public static void main(String[] args) {
        System.out.println(teachers.listTeachers());
    }
}
