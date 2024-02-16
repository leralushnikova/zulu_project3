package com.kupreychik.service;

import com.kupreychik.model.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentServiceFromMyComputer {
    private final List<Student> list= new ArrayList<>();

    public StudentServiceFromMyComputer(String fileName) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            Scanner scanner;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                Student student = new Student();
//                student.setId((long) list.size() + 1);
                scanner = new Scanner(line);
                scanner.useDelimiter(" ");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    switch (index) {
                        case 0 -> student.setName(data);
                        case 1 -> student.setSurname(data);
                        case 2 -> student.setBirthday(LocalDate.parse(data));
                        case 3 -> student.setPhoneNumber(data);
                    }
                    index++;
                }
                index = 0;
                list.add(student);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> listStudents() {
        return list;
    }
}