package by.anabios13.authorizationService.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JWTTokenEncoderProxy implements PasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    public JWTTokenEncoderProxy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
