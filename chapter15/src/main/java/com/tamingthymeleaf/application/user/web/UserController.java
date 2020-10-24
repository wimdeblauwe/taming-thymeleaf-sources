package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.web.EditMode;
import com.tamingthymeleaf.application.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model,
                        @SortDefault.SortDefaults({
                                @SortDefault("userName.lastName"),
                                @SortDefault("userName.firstName")}) Pageable pageable) {
        model.addAttribute("users", service.getUsers(pageable));
        return "users/list";
    }

    // tag::create-get[]
    @GetMapping("/create")
    @Secured("ROLE_ADMIN")
    public String createUserForm(Model model) {
        model.addAttribute("user", new CreateUserFormData());
        model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
        model.addAttribute("possibleRoles", List.of(UserRole.values())); //<.>
        model.addAttribute("editMode", EditMode.CREATE);
        return "users/edit";
    }
    // end::create-get[]

    // tag::create-post[]
    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public String doCreateUser(@Validated(CreateUserValidationGroupSequence.class) @ModelAttribute("user") CreateUserFormData formData,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
            model.addAttribute("possibleRoles", List.of(UserRole.values()));
            model.addAttribute("editMode", EditMode.CREATE);
            return "users/edit";
        }

        service.createUser(formData.toParameters());

        return "redirect:/users";
    }
    // end::create-post[]

    // tag::edit-get[]
    @GetMapping("/{id}") //<.>
    public String editUserForm(@PathVariable("id") UserId userId, //<.>
                               Model model) {
        User user = service.getUser(userId)
                           .orElseThrow(() -> new UserNotFoundException(userId)); //<.>
        model.addAttribute("user", EditUserFormData.fromUser(user)); //<.>
        model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
        model.addAttribute("possibleRoles", List.of(UserRole.values()));
        model.addAttribute("editMode", EditMode.UPDATE); //<.>
        return "users/edit"; //<.>
    }
    // end::edit-get[]

    // tag::edit-post[]
    @PostMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String doEditUser(@PathVariable("id") UserId userId,
                             @Validated(EditUserValidationGroupSequence.class) @ModelAttribute("user") EditUserFormData formData, //<.>
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER));
            model.addAttribute("possibleRoles", List.of(UserRole.values()));
            model.addAttribute("editMode", EditMode.UPDATE);
            return "users/edit";
        }

        service.editUser(userId, formData.toParameters());

        return "redirect:/users";
    }
    // end::edit-post[]

    // tag::delete-post[]
    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String doDeleteUser(@PathVariable("id") UserId userId,
                               RedirectAttributes redirectAttributes) { //<.>
        User user = service.getUser(userId)
                           .orElseThrow(() -> new UserNotFoundException(userId)); //<.>

        service.deleteUser(userId);

        redirectAttributes.addFlashAttribute("deletedUserName",
                                             user.getUserName().getFullName()); //<.>

        return "redirect:/users";
    }
    // end::delete-post[]
}
