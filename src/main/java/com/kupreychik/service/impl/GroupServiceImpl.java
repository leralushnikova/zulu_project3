package com.kupreychik.service.impl;

import com.kupreychik.dto.request.GroupRequest;
import com.kupreychik.dto.request.GroupRequestDouble;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.GroupResponse;
import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.mapper.GroupMapper;
import com.kupreychik.model.Group;
import com.kupreychik.model.Student;
import com.kupreychik.repository.GroupRepository;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.GroupService;
import com.kupreychik.service.JsonParseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.kupreychik.consts.RegexConsts.MAX_STUDENTS;
import static com.kupreychik.consts.RegexConsts.MIN_STUDENTS;

@Slf4j
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final JsonParseService jsonParseService;
    private final GroupMapper groupMapper;
    private final StudentRepository studentRepository;

    public GroupServiceImpl(GroupRepository groupRepositories, JsonParseService jsonParseService, GroupMapper groupMapper,
                            StudentRepository studentRepository) {
        this.groupRepository = groupRepositories;
        this.jsonParseService = jsonParseService;
        this.groupMapper = groupMapper;
        this.studentRepository = studentRepository;
    }

    @SneakyThrows
    @Override
    public String getGroupByNumber(Long number) {
        log.info("Выполняется поиск группы по номеру {}", number);
        try {
            Group group = groupRepository.getGroupByNumber(number);
            log.info("Группа найдена");
            return jsonParseService.writeToJson(groupMapper.mapToResponse(group));
        } catch (ModelNotFound e) {
            log.error("Ошибка. Группа не найдена");
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found group"));
        }
    }

    @Override
    @SneakyThrows
    public String getGroupBySurnameOfStudent(String surname){
        log.info( "Выполняется поиск студента по surname = {}", surname);
        try {
            Student student = studentRepository.getStudentBySurname(surname);
            Group group = groupRepository.getGroupBySurnameOfStudent(student);
            log.info("Студент нашелся");
            return jsonParseService.writeToJson(groupMapper.mapToResponse(group));
        } catch (Exception e) {
            log.error("Ошибка. Такой студент не найден");
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found student"));
        }
    }

    @Override
    @SneakyThrows
    public String addStudentInGroup(Long idGroup, Long idStudent){
        log.info("Добавления студента в группу");

        try {
            Group group = groupRepository.getGroupById(idGroup);
            Student student = studentRepository.getStudentById(idStudent);
            student.setGroupNumber(group.getNumber());
            List<Student> listStudents = group.getStudents();
            for (Student s : listStudents) {
                if(s.equals(student)) return jsonParseService.writeToJson(new ErrorResponse("This student is here"));
            }
            listStudents.add(student);
            group.setStudents(listStudents);
            return jsonParseService.writeToJson(groupMapper.mapToResponse(group));
        }catch (ModelNotFound e) {
            log.error("Ошибка. Либо не найден студент, либо не найдена группа");
            return jsonParseService.writeToJson(new ErrorResponse("Cannot found student or group"));
        }
    }

    @Override
    @SneakyThrows
    public String save(GroupRequestDouble groupRequestDouble) {
        log.info("saveGroup() method call with value {}", groupRequestDouble);

        try {

            List<Group> listGroups = groupRepository.getAllGroups();
            for (Group group : listGroups) {
                if (group.getNumber().equals(groupRequestDouble.getNumber())) {
                    return jsonParseService.writeToJson(new ErrorResponse("Change number group"));
                }
            }

            GroupRequest groupRequest = groupRequest(groupRequestDouble);

            Group groupToSave = groupMapper.mapToModelRequest(groupRequest);
            var result = groupRepository.save(groupToSave);
            return jsonParseService.writeToJson(groupMapper.mapToResponse(result));
        } catch (Exception e){
            return jsonParseService.writeToJson(new ErrorResponse("Cannot create group"));
        }
    }



    @Override
    public List<GroupResponse> getGroups() {
        log.info("getStudents() method call");
        return groupRepository.getAllGroups()
                .stream()
                .map(groupMapper::mapToResponse)
                .toList();
    }

    private GroupRequest groupRequest(GroupRequestDouble groupRequestDouble) {
        GroupRequest group = new GroupRequest();
        group.setNumber(groupRequestDouble.getNumber());
        List<Student> students = new ArrayList<>();
        List<Long> listNumberId = groupRequestDouble.getStudents();
        if(listNumberId.size() < MIN_STUDENTS  || listNumberId.size() > MAX_STUDENTS) return null;
        for (Long id : listNumberId) {
            for (Student student : studentRepository.getAllStudents()) {
                if(id.equals(student.getId()) && student.getGroupNumber() == null) {
                    student.setGroupNumber(groupRequestDouble.getNumber());
                    students.add(student);
                }
            }
        }
        group.setStudents(students);
        return group;
    }
}
