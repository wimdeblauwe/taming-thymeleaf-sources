package com.tamingthymeleaf.application.team;

import com.tamingthymeleaf.application.user.User;
import com.tamingthymeleaf.application.user.UserId;
import com.tamingthymeleaf.application.user.UserNotFoundException;
import com.tamingthymeleaf.application.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final TeamRepository repository;
    private final UserService userService;

    public TeamServiceImpl(TeamRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamSummary> getTeams(Pageable pageable) {
        return repository.findAllSummary(pageable);
    }

    // tag::create-edit[]
    @Override
    public Team createTeam(CreateTeamParameters parameters) {
        String name = parameters.getName();
        User coach = getUser(parameters.getCoachId());
        LOGGER.info("Creating team {} with coach {} ({})", name, coach.getUserName().getFullName(), coach.getId());
        Team team = new Team(repository.nextId(), name, coach);
        Set<TeamPlayerParameters> players = parameters.getPlayers();
        for (TeamPlayerParameters player : players) {
            team.addPlayer(new TeamPlayer(repository.nextPlayerId(), getUser(player.getPlayerId()), player.getPosition()));
        }
        return repository.save(team);
    }

    @Override
    public Team editTeam(TeamId teamId, EditTeamParameters parameters) {
        Team team = getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        if (team.getVersion() != parameters.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(User.class, team.getId().asString());
        }

        team.setName(parameters.getName());
        team.setCoach(getUser(parameters.getCoachId()));
        team.setPlayers(parameters.getPlayers().stream()
                                  .map(teamPlayerParameters -> new TeamPlayer(repository.nextPlayerId(), getUser(teamPlayerParameters.getPlayerId()), teamPlayerParameters.getPosition()))
                                  .collect(Collectors.toSet()));

        return team;
    }
    // end::create-edit[]

    @Override
    public Optional<Team> getTeam(TeamId teamId) {
        return repository.findById(teamId);
    }

    @Override
    public Optional<Team> getTeamWithPlayers(TeamId teamId) {
        return repository.findTeamWithPlayers(teamId);
    }

    @Override
    public void deleteTeam(TeamId teamId) {
        repository.deleteById(teamId);
    }

    @Override
    public void deleteAllTeams() {
        repository.deleteAll();
    }

    // end::addPlayer[]

    private User getUser(UserId userId) {
        return userService.getUser(userId)
                          .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
