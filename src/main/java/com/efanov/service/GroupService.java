package com.efanov.service;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.group.GroupRequest;

public interface GroupService {
    DecoratorResponse<String> getAllGroups();

    DecoratorResponse<String> getGroupByNumber(Integer number);

    DecoratorResponse<String> getGroupBySurname(String surname);

    DecoratorResponse<String> getGroupById(Long id);

    DecoratorResponse<String> delete(Long id);

    DecoratorResponse<String> update(GroupRequest groupRequest, Long id);

    DecoratorResponse<String> save(GroupRequest groupRequest);

}
