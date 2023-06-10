package by.anabios13.authorizationService.filters;

import by.anabios13.authorizationService.security.JWTUtil;
import by.anabios13.authorizationService.services.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public JWTFilter(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            String token = jwtUtil.resolveToken(request);
            if (token != null && jwtUtil.validateTokenAndRetrieveClaimLogin(token) != null) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = jwtUtil.validateTokenAndRetrieveClaimLogin(token);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
                    authentication = authenticationManager.authenticate(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
