package by.anabios13.authorizationService.controllers;

import by.anabios13.authorizationService.dto.AuthenticationDTO;
import by.anabios13.authorizationService.dto.PersonDTO;
import by.anabios13.authorizationService.models.Person;
import by.anabios13.authorizationService.models.Role;
import by.anabios13.authorizationService.pojo.ResponseWithMessage;
import by.anabios13.authorizationService.security.JWTUtil;
import by.anabios13.authorizationService.services.PersonService;
import by.anabios13.authorizationService.services.RegistrationService;
import by.anabios13.authorizationService.services.RoleService;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTUtil jwtUtil;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final PersonService personService;

    private final PasswordEncoder passwordEncoder;


    public AuthController(JWTUtil jwtUtil, RegistrationService registrationService, RoleService roleService, PersonService personService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWithMessage performRegistration(@RequestBody PersonDTO personDTO) {
        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getFirstName());
        person.setLogin(personDTO.getLogin());
        person.setPassword(personDTO.getPassword());
        Role role = roleService.searchById(personDTO.getRoleId());
        person.setRole(role);
        registrationService.register(person, role);
        String token = jwtUtil.generateToken(person.getLogin());
        return new ResponseWithMessage(Map.of("jwt-token", token));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWithMessage performLogin(@RequestBody AuthenticationDTO authenticationDTO) throws AuthException {
        return registrationService.performLogin(authenticationDTO);
    }
}
