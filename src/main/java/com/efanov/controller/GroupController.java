package com.efanov.controller;

import com.efanov.dto.group.GroupRequest;
import com.efanov.service.GroupService;
import com.efanov.service.JsonParseService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static com.efanov.constant.WebConstant.*;

public class GroupController extends UtilityController implements HttpHandler {
    private final JsonParseService jsonParseService;
    private final GroupService groupService;

    public GroupController(JsonParseService jsonParseService, GroupService groupService) {
        this.jsonParseService = jsonParseService;
        this.groupService = groupService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var groupRequest = jsonParseService.readObject(exchange.getRequestBody(), GroupRequest.class);
                var save = groupService.save((GroupRequest) groupRequest);
                sendStatus(exchange, save.getEntity(), save.getStatusCode());
            }
            case GET -> {
                var requestParam = exchange.getRequestURI().getQuery();
                var queryMap = queryToMap(requestParam);

                if (requestParam == null) {
                    if (getIdFromPath(exchange) != 0) {
                        var response = groupService.getGroupById(getIdFromPath(exchange));
                        sendStatus(exchange, response.getEntity(), response.getStatusCode());
                    }

                    var response = groupService.getAllGroups();
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                } else if (requestParam.contains(NUMBER)) {
                    var number = queryMap.get(NUMBER);
                    var response = groupService.getGroupByNumber(Integer.valueOf(number));
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                } else if (requestParam.contains(SURNAME)) {
                    var surname = queryMap.get(SURNAME);
                    var response = groupService.getGroupBySurname(surname);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                }
            }
            case PUT -> {
                var groupRequest = jsonParseService.readObject(exchange.getRequestBody(), GroupRequest.class);
                var id = getIdFromPath(exchange);
                var update = groupService.update((GroupRequest) groupRequest, id);
                sendStatus(exchange, update.getEntity(), update.getStatusCode());
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
                var delete = groupService.delete(id);
                sendStatus(exchange, delete.getEntity(), delete.getStatusCode());
            }
        }

    }
}
