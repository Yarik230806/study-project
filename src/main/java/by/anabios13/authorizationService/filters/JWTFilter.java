package by.anabios13.authorizationService.filters;

import by.anabios13.authorizationService.security.JWTUtil;
import by.anabios13.authorizationService.services.PersonDetailsService;
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
    private final PersonDetailsService personDetailsServiceService;

    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsServiceService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsServiceService = personDetailsServiceService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);
        if(token!=null && jwtUtil.validateTokenAndRetrieveClaim(token)!=null){
            Authentication authentication = jwtUtil.getAuthentication(token);
            if(authentication!=null){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}