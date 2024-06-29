package cz.martinkostelecky.springsecurity.authentication;

import cz.martinkostelecky.springsecurity.exception.EmailAlreadyTakenException;
import cz.martinkostelecky.springsecurity.model.LoginRequest;
import cz.martinkostelecky.springsecurity.security.UserPrincipal;
import cz.martinkostelecky.springsecurity.user.User;
import cz.martinkostelecky.springsecurity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import cz.martinkostelecky.springsecurity.model.LoginResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final cz.martinkostelecky.springsecurity.security.JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void saveUser(LoginRequest request) throws EmailAlreadyTakenException {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyTakenException("E-mail " + request.getEmail() + " již patří jinému uživateli.");
        }
        var newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(newUser);
        log.info("User id: " + newUser.getId() + " created.");

    }

    public cz.martinkostelecky.springsecurity.model.LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = jwtIssuer.issue(cz.martinkostelecky.springsecurity.security.JwtIssuer.Request.builder()
                .userId(principal.getUserId())
                .email(principal.getEmail())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build());

        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }
}
