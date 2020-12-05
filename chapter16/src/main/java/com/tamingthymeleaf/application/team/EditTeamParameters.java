package com.tamingthymeleaf.application.team;

import com.tamingthymeleaf.application.user.UserId;

import java.util.Set;

public class EditTeamParameters extends CreateTeamParameters {
    private final long version;

    public EditTeamParameters(long version, String name, UserId coachId, Set<TeamPlayerParameters> players) {
        super(name, coachId, players);
        this.version = version;
    }

    public long getVersion() {
        return version;
    }
}
