package cz.martinkostelecky.springsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Config class for use of JwtAuthenticationfilter
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailService customUserDetailService;

    //@Bean makes this method an instance that is managed by Spring container
    //Spring container is a part of Spring core. It manages all beans and does Dependency Injection
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // method is used to configure Cross-Site Request Forgery (CSRF) protection in a Spring Security-enabled
                // application. CSRF is an attack that tricks the victim into submitting a malicious request.
                // To prevent this type of attack, Spring Security provides built-in support for CSRF protection.
                //here disabled
                .csrf(AbstractHttpConfigurer::disable)
                //.cors(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                // list of patterns or endpoints that does contain only methods dealing with authentication, i.e. used to specify the types of requests that should be secured
                // permission for this list
                // any other requests will be authenticated
                .authorizeHttpRequests(a -> a.requestMatchers("/api/v1/auth/login", "/api/v1/auth/register")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // session is stateless as we don´t want to store authentication state.
                // Authentication is made for every request
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                //TODO authentication exception handling
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
