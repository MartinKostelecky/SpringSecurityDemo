package cz.martinkostelecky.springsecurity.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginRequest {

    private String email;

    private String password;

    private String role;
}
