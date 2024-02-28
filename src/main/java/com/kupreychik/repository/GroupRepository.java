package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Group;
import com.kupreychik.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupRepository {

    private final StudentRepository studentRepository;
    private Long id;

    public GroupRepository(StudentRepository studentRepository, Long id) {
        this.studentRepository = studentRepository;
        this.id = id;
    }

    public List<Student> getAllStudents(){
        List<Student> list = studentRepository.getAllStudents();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId((long) i + 1);
            list.get(i).setGroupId(id);
        }
        return list;
    }

    /*public Student getStudentBySurname(String surname, String name){
        return students.stream()
                .filter(el -> (surname.equals(el.getSurname()) && name.equals(el.getName())))
                .findFirst()
                .orElseThrow();
    }*/

    /*public Group getStudentById(Long id) throws ModelNotFound {
        return students.stream()
                .filter(el -> id.equals(el.getId()))
                .findFirst()
                .orElseThrow(ModelNotFound::new);
    }*/

    /*public Student save(Student student){
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
    }*/
}
