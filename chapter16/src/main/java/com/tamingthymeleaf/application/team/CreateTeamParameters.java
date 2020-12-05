package com.tamingthymeleaf.application.team;

import com.tamingthymeleaf.application.user.UserId;

import java.util.Set;

public class CreateTeamParameters {
    private final String name;
    private final UserId coachId;
    private final Set<TeamPlayerParameters> players;

    public CreateTeamParameters(String name, UserId coachId, Set<TeamPlayerParameters> players) {
        this.name = name;
        this.coachId = coachId;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public UserId getCoachId() {
        return coachId;
    }

    public Set<TeamPlayerParameters> getPlayers() {
        return players;
    }
}
