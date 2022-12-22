package by.itstep.organizaer.service;

import by.itstep.organizaer.aspect.ExceptionHandlingAdvice;
import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.exceptions.UserAlreadyExistsException;
import by.itstep.organizaer.model.dto.RegistrationRequest;
import by.itstep.organizaer.model.entity.Authority;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    UserMapper userMapper;

    public UserDto createUser(RegistrationRequest request) {
        User userToSave = userMapper.registrationToEntity(request);
        userToSave.setAuthorities(request
                .getRoles()
                .stream()
                .map(role -> Authority
                        .builder()
                        .authority(role)
                        .user(userToSave)
                        .build())
                .collect(Collectors.toList()));
        return create(userToSave);
    }

    private UserDto create(User userToSave) {
        try {
            userRepository.save(userToSave);
        } catch (Exception e) {
            throw new UserAlreadyExistsException(String.format("Логин %s уже занят", userToSave.getLogin()));
        }
        return userMapper.toDto(userToSave);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не верное имя пользователя или пароль"));
    }

    public Optional<User> getById(final Long id) {
        return userRepository.findById(id);
    }
}
