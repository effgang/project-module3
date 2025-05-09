package com.efanov.repository;

import com.efanov.exception.ModelException;
import com.efanov.model.Group;
import com.efanov.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.GROUP_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.GROUP_NOT_FOUND_BY_NUMBER;

public class GroupRepository {
    private final CopyOnWriteArrayList<Group> groups = new CopyOnWriteArrayList<>();

    public List<Group> getAllGroups() {
        return new ArrayList<>(groups);
    }

    public Group getGroupBySurname(String surname) {
        for (Group group : getAllGroups()) {
            for (Student student : group.getStudentsInGroup()) {
                if (student.getSurname().equals(surname)) return group;
            }
        }
        return null;
    }

    public Group getGroupByNumber(Integer number) throws ModelException {
        return groups.stream()
                .filter(el -> el.getGroupNumber() == number)
                .findFirst()
                .orElseThrow(() -> new ModelException(GROUP_NOT_FOUND_BY_NUMBER, number.toString()));


    }

    public Group getGroupById(Long id) throws ModelException {
        return groups.stream()
                .filter(el -> el.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ModelException(GROUP_NOT_FOUND_BY_ID, id.toString()));
    }

    public Group save(Group group) {
        group.setId((long) (groups.size() + 1));
        groups.add(group);
        return group;
    }

    public Group update(Group group, Long id) throws ModelException {
        group.setId(id);
        groups.set(groups.indexOf(getGroupById(id)), group);

        return group;
    }

    public void delete(Long id) throws ModelException {
        groups.remove(getGroupById(id));
    }
}
