package cz.martinkostelecky.springsecurity.controller;

import cz.martinkostelecky.springsecurity.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @RequestMapping (value = "/secured", method = GET)
    //@AuthenticationPrincipal needed to retrieve principal data
    public String secured(@AuthenticationPrincipal UserPrincipal principal) {
        return "This can only be seen by a logged in user. Your Email is: "
                + principal.getEmail() + " your ID: " + principal.getUserId();
    }
}
