package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeacherRepository {

    private final CopyOnWriteArrayList<Teacher> teachers;

    public TeacherRepository() {
        teachers = new CopyOnWriteArrayList<>();
    }

    public TeacherRepository(CopyOnWriteArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Teacher> getAllStudents(){
        return new ArrayList<>(teachers);
    }


    public Teacher getStudentBySurname(String surname, String name) throws ModelNotFound {
        return teachers.stream()
                .filter(el -> (surname.equals(el.getSurname()) && name.equals(el.getName())))
                .findFirst()
                .orElseThrow();
    }

    public Teacher getStudentById(Long id) throws ModelNotFound {
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
}
