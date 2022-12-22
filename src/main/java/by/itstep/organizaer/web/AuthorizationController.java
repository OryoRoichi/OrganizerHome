package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.LoginRequest;
import by.itstep.organizaer.model.dto.RegistrationRequest;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.entity.Roles;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.security.JwtUtil;
import by.itstep.organizaer.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizationController {

    UserService userService;

    AuthenticationManager authenticationManager;

    JwtUtil jwtUtil;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.USER));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<UserDto> registerAdmin(@RequestBody RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ADMIN));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtUtil.generateToken(user))
                .body(userMapper.toDto(user));
    }

}
