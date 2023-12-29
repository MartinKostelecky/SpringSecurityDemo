package cz.martinkostelecky.springbootsecurity.authentication;

import cz.martinkostelecky.springbootsecurity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    // Field of Enum type
    private Role role;
}
