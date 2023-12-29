package cz.martinkostelecky.springbootsecurity.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Repository or @Service also works as they include @Component
@Component
//creates constructor with any final field
@RequiredArgsConstructor
//JWT = json web token
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request,response);
                return;
            }
            jwtToken = authHeader.substring(7);
            //we want to extract email using Spring getUsername method
            userEmail = jwtService.extractUsername(jwtToken);
            //if we have user email but no authentication
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //get userDetails from DB
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                //if user is valid
                if(jwtService.isValidToken(jwtToken,userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    //set details of authentication token based on http request
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    //update of authentication token
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            //always call filter chain
            filterChain.doFilter(request,response);
    }
}
