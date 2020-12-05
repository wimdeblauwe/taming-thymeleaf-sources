package com.tamingthymeleaf.application.team.web;

import com.tamingthymeleaf.application.team.PlayerPosition;
import com.tamingthymeleaf.application.team.TeamPlayer;
import com.tamingthymeleaf.application.user.UserId;

import javax.validation.constraints.NotNull;

public class TeamPlayerFormData {
    @NotNull
    private UserId playerId;
    @NotNull
    private PlayerPosition position;

    public UserId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UserId playerId) {
        this.playerId = playerId;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public static TeamPlayerFormData fromTeamPlayer(TeamPlayer player) {
        TeamPlayerFormData result = new TeamPlayerFormData();
        result.setPlayerId(player.getPlayer().getId());
        result.setPosition(player.getPosition());
        return result;
    }
}
