package com.tamingthymeleaf.application.team.web;

import com.tamingthymeleaf.application.team.CreateTeamParameters;
import com.tamingthymeleaf.application.team.TeamPlayerParameters;
import com.tamingthymeleaf.application.user.UserId;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateTeamFormData {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private UserId coachId;

    @NotNull
    @Size(min = 1)
    @Valid
    private TeamPlayerFormData[] players; //<.>

    public CreateTeamFormData() {
        this.players = new TeamPlayerFormData[]{new TeamPlayerFormData()}; //<.>
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserId getCoachId() {
        return coachId;
    }

    public void setCoachId(UserId coachId) {
        this.coachId = coachId;
    }

    public TeamPlayerFormData[] getPlayers() {
        return players;
    }

    public void setPlayers(TeamPlayerFormData[] players) {
        this.players = players;
    }

    public CreateTeamParameters toParameters() {
        return new CreateTeamParameters(name, coachId, getTeamPlayerParameters());
    }

    @Nonnull
    protected Set<TeamPlayerParameters> getTeamPlayerParameters() {
        return Arrays.stream(players)
                     .map(teamPlayerFormData -> new TeamPlayerParameters(teamPlayerFormData.getPlayerId(),
                                                                         teamPlayerFormData.getPosition()))
                     .collect(Collectors.toSet());
    }

    // tag::removeEmptyTeamPlayerForms[]
    public void removeEmptyTeamPlayerForms() {
        setPlayers(Arrays.stream(players)
                         .filter(this::isNotEmptyTeamPlayerForm)
                         .toArray(TeamPlayerFormData[]::new));
    }

    private boolean isNotEmptyTeamPlayerForm(TeamPlayerFormData formData) {
        return formData != null
                && formData.getPlayerId() != null
                && formData.getPosition() != null;
    }
    // end::removeEmptyTeamPlayerForms[]
}
