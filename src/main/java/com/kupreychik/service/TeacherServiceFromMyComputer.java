package com.kupreychik.service;

import com.kupreychik.model.Items;
import com.kupreychik.model.Teacher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.kupreychik.consts.FileConsts.FILE_TEACHERS;

/**
 * @author Valeriya Lushnikova
 * get list Teacher from file.txt
 */
public class TeacherServiceFromMyComputer{

    private final List<Teacher> teachers = new ArrayList<>();


    public TeacherServiceFromMyComputer() {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_TEACHERS))){
            String line;
            Scanner scanner;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                Teacher teacher = new Teacher();
                teacher.setId((long) teachers.size() + 1);

                scanner = new Scanner(line);
                scanner.useDelimiter(" ");

                while (scanner.hasNext()) {
                    String data = scanner.next();
                    Items item = null;

                    switch (index) {
                        case 0 -> teacher.setName(data);
                        case 1 -> teacher.setSurname(data);
                        case 2 -> {
                            switch (data) {
                                case "MATH" -> item = Items.MATH;
                                case "HISTORY" -> item = Items.HISTORY;
                                case "PE" -> item = Items.PE;
                                case "ENGLISH" -> item = Items.ENGLISH;
                                case "BIOLOGY" -> item = Items.BIOLOGY;
                            }
                            teacher.setItem(item);
                        }
                        case 3 -> teacher.setExperience(Integer.parseInt(data));
                        case 4 -> teacher.setBirthday(LocalDate.parse(data));
                        case 5 -> teacher.setPhone(data);
                    }
                    index++;
                }
                index = 0;

                teachers.add(teacher);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Teacher> listTeachers() {
        return teachers;
    }

}