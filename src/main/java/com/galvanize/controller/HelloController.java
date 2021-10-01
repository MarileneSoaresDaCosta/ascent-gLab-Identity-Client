package com.galvanize.controller;

import com.galvanize.security.Principle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam(defaultValue = "there") String name){
        return String.format("Hello %s! Welcome to the gLab Security Client demo.", name);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    public Principle getTokenInfo(@AuthenticationPrincipal Principle user){
        return user;
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String sayHiToAdminByName(@AuthenticationPrincipal Principle user){
        String msg = String.format("Hello %s %s.  I see you are an admin.  Below is your data ...\n%s",
                user.getFirstName(), user.getLastName(), user);
        return msg;
    }
}
