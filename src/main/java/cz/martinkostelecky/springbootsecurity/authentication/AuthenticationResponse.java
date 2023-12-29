package cz.martinkostelecky.springbootsecurity.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Data annotation takes care of generating the getter and setter methods, as well as the equals, hashCode, and toString methods.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    // @JsonProperty annotation allows to specify the name of the property as it should appear in the JSON data.
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

}
