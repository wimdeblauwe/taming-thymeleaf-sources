package com.tamingthymeleaf.application.infrastructure.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

// tag::class-and-version[]
@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${application.version}") //<.>
    private String version;

    @ModelAttribute("version") //<.>
    public String getVersion() {
        return version;
    }
    // end::class-and-version[]

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class, ObjectOptimisticLockingFailureException.class})
    public ModelAndView handleConflict(HttpServletRequest request, Exception e) {
        ModelAndView result = new ModelAndView("error/409");
        result.addObject("url", request.getRequestURL());
        return result;
    }

    @InitBinder //<.>
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(false); //<.>
        binder.registerCustomEditor(String.class, stringtrimmer); //<.>
    }
}

