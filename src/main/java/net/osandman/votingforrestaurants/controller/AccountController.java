package net.osandman.votingforrestaurants.controller;

import net.osandman.votingforrestaurants.dto.AuthUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthUser getAccount(@AuthenticationPrincipal AuthUser authUser) {
        return authUser;
    }
}
