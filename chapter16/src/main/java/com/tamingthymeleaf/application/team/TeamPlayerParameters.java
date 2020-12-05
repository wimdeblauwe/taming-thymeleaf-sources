package com.tamingthymeleaf.application.team;

import com.tamingthymeleaf.application.user.UserId;

import javax.validation.constraints.NotNull;

public class TeamPlayerParameters {
    private final UserId playerId;
    private final PlayerPosition position;

    public TeamPlayerParameters(UserId playerId, PlayerPosition position) {
        this.playerId = playerId;
        this.position = position;
    }

    public UserId getPlayerId() {
        return playerId;
    }

    public PlayerPosition getPosition() {
        return position;
    }
}
