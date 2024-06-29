package cz.martinkostelecky.springsecurity.authentication;

import cz.martinkostelecky.springsecurity.exception.EmailAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(@RequestBody cz.martinkostelecky.springsecurity.model.LoginRequest request) throws EmailAlreadyTakenException {
        authenticationService.saveUser(request);
    }

    @PostMapping("/login")
    public cz.martinkostelecky.springsecurity.model.LoginResponse login(@RequestBody @Validated cz.martinkostelecky.springsecurity.model.LoginRequest request) {
        return authenticationService.attemptLogin(request.getEmail(), request.getPassword());
    }

}
