package com.kupreychik.repository;

import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Group;
import com.kupreychik.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupRepository {
    private final CopyOnWriteArrayList<Group> groups;


    public GroupRepository() {
        groups = new CopyOnWriteArrayList<>();
    }

    public List<Group> getAllGroups(){
        return new ArrayList<>(groups);
    }

    public Group getGroupByNumber(Long number) throws ModelNotFound {
        return groups.stream()
                .filter(el -> number.equals(el.getNumber()))
                .findFirst()
                .orElseThrow(ModelNotFound::new);
    }

    public Group getGroupById(Long id) throws ModelNotFound {
        return groups.stream()
                .filter(el -> id.equals(el.getId()))
                .findFirst()
                .orElseThrow(ModelNotFound::new);
    }

    public Group getGroupBySurnameOfStudent(Student student){
        for (Group group : groups) {
            List<Student> listStudents = group.getStudents();
            for (Student listStudent : listStudents) {
                if (listStudent.equals(student)) {
                    return group;
                }
            }
        }
        return null;
    }


    public Group save(Group group){
        group.setId((long) groups.size() + 1);
        groups.add(group);
        return group;
    }

}
