package com.tamingthymeleaf.application.team;

import io.github.wimdeblauwe.jpearl.UniqueIdGenerator;

import java.util.UUID;

public class TeamRepositoryImpl implements TeamRepositoryCustom {
    private final UniqueIdGenerator<UUID> generator;

    public TeamRepositoryImpl(UniqueIdGenerator<UUID> generator) {
        this.generator = generator;
    }

    @Override
    public TeamId nextId() {
        return new TeamId(generator.getNextUniqueId());
    }

    // tag::nextPlayerId[]
    @Override
    public TeamPlayerId nextPlayerId() {
        return new TeamPlayerId(generator.getNextUniqueId());
    }
    // end::nextPlayerId[]
}
