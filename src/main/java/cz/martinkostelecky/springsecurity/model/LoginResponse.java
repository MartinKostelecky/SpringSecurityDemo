package cz.martinkostelecky.springsecurity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    // @JsonProperty annotation allows to specify the name of the property as it should appear in the JSON data.
    @JsonProperty("token")
    private String accessToken;
}
