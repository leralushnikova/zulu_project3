package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Items;
import com.kupreychik.model.Teacher;
import com.kupreychik.service.TeacherServiceFromMyComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kupreychik.model.Items.*;
import static com.kupreychik.model.Items.ECONOMIC;

public class TeacherRepository {

    private final CopyOnWriteArrayList<Teacher> teachers;

    public TeacherRepository() {
        TeacherServiceFromMyComputer teacherServiceFromMyComputer = new TeacherServiceFromMyComputer();
        teachers = new CopyOnWriteArrayList<>(teacherServiceFromMyComputer.listTeachers());
    }


    public List<Teacher> getAllTeachers(){
        return new ArrayList<>(teachers);
    }


    public Teacher getTeacherBySurname(String surname) {
        return teachers.stream()
                .filter(el -> (surname.equals(el.getSurname())))
                .findFirst()
                .orElseThrow();
    }

    public Teacher getTeacherById(Long id) throws ModelNotFound {
        return teachers.stream()
                .filter(el -> id.equals(el.getId()))
                .findFirst()
                .orElseThrow(ModelNotFound::new);
    }

    public Teacher save(Teacher teacher){
        teacher.setId((long)(teachers.size() + 1));
        teachers.add(teacher);
        return teacher;
    }

    public Teacher delete(Teacher teacher){
        teachers.remove(teacher);
        for (int i = 0; i < teachers.size(); i++) {
            teachers.get(i).setId((long) (i + 1));
        }
        return teacher;
    }

    public Teacher change(Long id, Teacher teacher, String stringItem){
        for (Teacher value : teachers) {
            if (id.equals(value.getId())) {
                Items item = null;
                switch (stringItem) {
                    case "MATH" -> item = MATH;
                    case "HISTORY" -> item = HISTORY;
                    case "PE" -> item = PE;
                    case "ENGLISH" -> item = ENGLISH;
                    case "BIOLOGY" -> item = BIOLOGY;
                    case "ECONOMIC" -> item = ECONOMIC;
                }
                if (item != null) {
                    List<Items> listItems = teacher.getItems();
                    listItems.add(item);
                    teacher.setItems(listItems);
                }
            }
        }
        return teacher;
    }
}
