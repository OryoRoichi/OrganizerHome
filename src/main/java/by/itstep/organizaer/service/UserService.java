package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.UserAlreadyExistsException;
import by.itstep.organizaer.model.User;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.exists(Example.of(user))) {
            throw new UserAlreadyExistsException("Пользователь уже существует");
        }
        return userRepository.save(user);
    }
}
