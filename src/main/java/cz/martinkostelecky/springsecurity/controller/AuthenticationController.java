package cz.martinkostelecky.springsecurity.controller;

import cz.martinkostelecky.springsecurity.exception.EmailAlreadyTakenException;
import cz.martinkostelecky.springsecurity.model.LoginRequest;
import cz.martinkostelecky.springsecurity.model.LoginResponse;
import cz.martinkostelecky.springsecurity.service.AuthenticationService;
import cz.martinkostelecky.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @RequestMapping(value = "/register", method = POST)
    public void register(@RequestBody LoginRequest request) throws EmailAlreadyTakenException {
        userService.saveUser(request);
    }

    @RequestMapping(value = "/login", method = POST)
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authenticationService.attemptLogin(request.getEmail(), request.getPassword());
    }

}
