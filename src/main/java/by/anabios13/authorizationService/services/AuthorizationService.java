package by.anabios13.authorizationService.services;

import by.anabios13.authorizationService.dto.AuthenticationDTO;
import by.anabios13.authorizationService.repository.UserRepository;
import by.anabios13.authorizationService.security.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class AuthorizationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthorizationService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> performLogin(AuthenticationDTO authenticationDTO) throws BadCredentialsException {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(),
                    authenticationDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authenticationDTO.getLogin());
            String authorityForResponse = authentication.getAuthorities().toString();
            ResponseCookie responseCookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(24*60*60)
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(Map.of("Role", new String(authorityForResponse.replace("[", "").replace("]", "")) ,
                            "token",token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Incorrect credentials"));
        }
    }
}