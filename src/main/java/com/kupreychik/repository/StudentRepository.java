package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StudentRepository {

    private final CopyOnWriteArrayList<Student> students;

    public StudentRepository(CopyOnWriteArrayList<Student> students) {
        this.students = students;
    }

    public List<Student> getAllStudents(){
        return new ArrayList<>(students);
    }


    public Student getStudentBySurnameAndName(String surname, String name){
        return students.stream()
                .filter(el -> (surname.equals(el.getSurname()) && name.equals(el.getName())))
                .findFirst()
                .orElseThrow();
    }

    public Student getStudentBySurname(String surname){
        return students.stream()
                .filter(el -> (surname.equals(el.getSurname())))
                .findFirst()
                .orElseThrow();
    }

    public Student getStudentById(Long id) throws ModelNotFound {
        return students.stream()
                .filter(el -> id.equals(el.getId()))
                .findFirst()
                .orElseThrow(ModelNotFound::new);
    }

    public Student save(Student student){
        student.setId((long)(students.size() + 1));
        students.add(student);
        return student;
    }

    public Student delete(Student student){
        students.remove(student);
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setId((long) (i + 1));
        }
        return student;
    }

    public Student change(Long id, Student student){
        for (int i = 0; i < students.size(); i++) {
            if (id.equals(students.get(i).getId())) {
                student.setId(id);
                students.set(i, student);
            }
        }
        return student;
    }
}
