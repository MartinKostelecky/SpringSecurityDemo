package cz.martinkostelecky.springsecurity.service;

import cz.martinkostelecky.springsecurity.exception.EmailAlreadyTakenException;
import cz.martinkostelecky.springsecurity.model.LoginRequest;
import cz.martinkostelecky.springsecurity.user.User;
import cz.martinkostelecky.springsecurity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void saveUser(LoginRequest request) throws EmailAlreadyTakenException {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyTakenException("E-mail " + request.getEmail() + " is already taken.");
        }
        var newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(newUser);
        log.info("User id: " + newUser.getId() + " created.");

    }
}
