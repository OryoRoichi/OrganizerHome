package by.itstep.organizaer.service;

import by.itstep.organizaer.model.User;
import by.itstep.organizaer.model.dto.UserDto;
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

    public UserDto createUser(UserDto user) {
        User userToSave = User.builder()
                .login(user.getLogin())
                .name(user.getName())
                .password(user.getPassword())
                .birthDay(user.getBirthDay())
                .contacts(user.getContacts())
                .friendList(user.getFriendList())
                .build();
        User savedUser = userRepository.save(userToSave);
        return UserDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .login(savedUser.getLogin())
                .birthDay(savedUser.getBirthDay())
                .contacts(savedUser.getContacts())
                .friendList(savedUser.getFriendList())
                .build();
    }
}
