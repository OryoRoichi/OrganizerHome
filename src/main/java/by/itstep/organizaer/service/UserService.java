package by.itstep.organizaer.service;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.model.User;
import by.itstep.organizaer.model.dto.UserDto;
import by.itstep.organizaer.model.mapping.UserMapper;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    ProjectConfiguration projectConfiguration;

    public UserDto createUser(UserDto user) {
        User userToSave = userMapper.toEntity(user);
        userRepository.save(userToSave);
        return userMapper.toDto(userToSave);
    }
}
