package com.tamingthymeleaf.application.team;

public interface TeamRepositoryCustom {
    TeamId nextId();

    TeamPlayerId nextPlayerId(); //<.>
}
