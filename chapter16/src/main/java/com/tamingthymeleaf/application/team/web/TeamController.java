package com.tamingthymeleaf.application.team.web;

import com.tamingthymeleaf.application.infrastructure.web.EditMode;
import com.tamingthymeleaf.application.team.*;
import com.tamingthymeleaf.application.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Nonnull;
import javax.validation.Valid;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService service;
    private final UserService userService;

    public TeamController(TeamService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // tag::binder[]
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new RemoveUnusedTeamPlayersValidator(binder.getValidator()));
    }
    // end::binder[]

    @GetMapping
    public String index(Model model,
                        @SortDefault.SortDefaults(@SortDefault("name")) Pageable pageable) {
        model.addAttribute("teams", service.getTeams(pageable));
        return "teams/list";
    }

    // tag::createTeamForm[]
    @GetMapping("/create")
    @Secured("ROLE_ADMIN")
    public String createTeamForm(Model model) {
        model.addAttribute("team", new CreateTeamFormData());
        model.addAttribute("users", userService.getAllUsersNameAndId());
        model.addAttribute("positions", PlayerPosition.values()); //<.>
        return "teams/edit";
    }
    // end::createTeamForm[]

    // tag::doCreateTeam[]
    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public String doCreateTeam(@Valid @ModelAttribute("team") CreateTeamFormData formData,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("users", userService.getAllUsersNameAndId());
            model.addAttribute("positions", PlayerPosition.values());
            return "teams/edit";
        }

        service.createTeam(formData.toParameters());

        return "redirect:/teams";
    }

    // end::doCreateTeam[]

    // tag::editTeamForm[]
    @GetMapping("/{id}")
    public String editTeamForm(@PathVariable("id") TeamId teamId,
                               Model model) {
        Team team = service.getTeamWithPlayers(teamId) //<.>
                           .orElseThrow(() -> new TeamNotFoundException(teamId));
        model.addAttribute("team", EditTeamFormData.fromTeam(team));
        model.addAttribute("users", userService.getAllUsersNameAndId());
        model.addAttribute("positions", PlayerPosition.values()); //<.>
        model.addAttribute("editMode", EditMode.UPDATE);
        return "teams/edit";
    }
    // end::editTeamForm[]

    // tag::doEditTeam[]
    @PostMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String doEditTeam(@PathVariable("id") TeamId teamId,
                             @Valid @ModelAttribute("team") EditTeamFormData formData,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("users", userService.getAllUsersNameAndId());
            model.addAttribute("positions", PlayerPosition.values());
            return "teams/edit";
        }

        service.editTeam(teamId,
                         formData.toParameters());

        return "redirect:/teams";
    }
    // end::doEditTeam[]

    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String doDeleteTeam(@PathVariable("id") TeamId teamId,
                               RedirectAttributes redirectAttributes) {
        Team team = service.getTeam(teamId)
                           .orElseThrow(() -> new TeamNotFoundException(teamId));

        service.deleteTeam(teamId);

        redirectAttributes.addFlashAttribute("deletedTeamName",
                                             team.getName());

        return "redirect:/teams";
    }

    // tag::getEditTeamPlayerFragment[]
    @GetMapping("/edit-teamplayer-fragment")
    @Secured("ROLE_ADMIN")
    public String getEditTeamPlayerFragment(Model model,
                                            @RequestParam("index") int index) { //<.>
        model.addAttribute("index", index); //<.>
        model.addAttribute("users", userService.getAllUsersNameAndId()); //<.>
        model.addAttribute("positions", PlayerPosition.values()); //<.>
        model.addAttribute("teamObjectName", "dummyTeam"); //<.>
        model.addAttribute("dummyTeam", new DummyTeamForTeamPlayerFragment()); //<.>
        return "teams/edit-teamplayer-fragment :: teamplayer-form"; //<.>
    }

    private static class DummyTeamForTeamPlayerFragment {
        private TeamPlayerFormData[] players;

        public TeamPlayerFormData[] getPlayers() {
            return players;
        }

        public void setPlayers(TeamPlayerFormData[] players) {
            this.players = players;
        }
    }
    // end::getEditTeamPlayerFragment[]

    // tag::validator[]
    private static class RemoveUnusedTeamPlayersValidator implements Validator { //<.>
        private final Validator validator;

        private RemoveUnusedTeamPlayersValidator(Validator validator) { //<.>
            this.validator = validator;
        }

        @Override
        public boolean supports(@Nonnull Class<?> clazz) {
            return validator.supports(clazz);
        }

        @Override
        public void validate(@Nonnull Object target, @Nonnull Errors errors) {
            if (target instanceof CreateTeamFormData) { //<.>
                CreateTeamFormData formData = (CreateTeamFormData) target;
                formData.removeEmptyTeamPlayerForms(); //<.>
            }

            validator.validate(target, errors);
        }
    }
    // end::validator[]
}
