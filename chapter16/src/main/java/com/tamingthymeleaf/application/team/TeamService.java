package com.tamingthymeleaf.application.team;

import com.tamingthymeleaf.application.user.User;
import com.tamingthymeleaf.application.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeamService {
    Page<TeamSummary> getTeams(Pageable pageable);

    // tag::create-edit[]
    Team createTeam(CreateTeamParameters parameters);

    Team editTeam(TeamId teamId, EditTeamParameters parameters);
    // end::create-edit[]

    Optional<Team> getTeam(TeamId teamId);

    Optional<Team> getTeamWithPlayers(TeamId teamId);

    void deleteTeam(TeamId teamId);

    void deleteAllTeams();
}
