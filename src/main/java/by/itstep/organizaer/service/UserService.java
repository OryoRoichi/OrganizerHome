package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserAlreadyExistsException;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.RegistrationRequest;
import by.itstep.organizaer.model.entity.Authority;
import by.itstep.organizaer.model.entity.Contacts;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    FriendRepository friendRepository;

    UserMapper userMapper;

    @Transactional
    public UserDto createUser(RegistrationRequest request) {
        User userToSave = userMapper.registrationToEntity(request);
        userToSave.setAuthorities(request
                .getRoles()
                .stream()
                .map(role -> Authority
                        .builder()
                        .authority(role)
                        .orgUser(userToSave)
                        .build())
                .collect(Collectors.toList()));
        userToSave.setContacts(Contacts.builder()
                .phone(request.getPhone())
                .email(List.of(request.getEmail()))
                .build());
        return create(userToSave);
    }

    private UserDto create(User userToSave) {
        try {
            userRepository.save(userToSave);
            userRepository.findById(userToSave.getId())
                    .map(user -> {
                        friendRepository.findByPhone(user.getContacts().getPhone()).forEach((friend) -> {
                            friend.setUuid(user.getUuid());
                            friendRepository.save(friend);
                        });
                        return user;
                    })
                    .orElseThrow(() -> new UserNotFoundException(userToSave.getId()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
