package cz.martinkostelecky.springbootsecurity.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Config class for use of my JwtAuthenticationfilter
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    //@Bean makes this method an instance that is managed by Spring container
    //Spring container is a part of Spring core. It manages all beans and does Dependency Injection
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // method is used to configure Cross-Site Request Forgery (CSRF) protection in a Spring Security-enabled
                // application. CSRF is an attack that tricks the victim into submitting a malicious request.
                // To prevent this type of attack, Spring Security provides built-in support for CSRF protection.
                .csrf()
                //here disabled
                .disable()
                .authorizeHttpRequests()
                // list of patterns or endpoints that does contain only methods dealing with authentication
                .requestMatchers("/api/v1/auth/**")
                // permission for this list
                .permitAll()
                // any other requests will be authenticated
                .anyRequest()
                .authenticated()
                .and()
                // session is stateless as we don´t want to store authentication state.
                // Authentication is made for every request
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
