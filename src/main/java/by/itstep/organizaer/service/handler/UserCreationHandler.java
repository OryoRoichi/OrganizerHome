package by.itstep.organizaer.service.handler;

import by.itstep.organizaer.exceptions.UserAlreadyExistsException;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCreationHandler {

    UserRepository userRepository;

    EntityManager entityManager;

    @Transactional
    public User doCreate(final User userToSave) {
        try {
            User user = userRepository.saveAndFlush(userToSave);
            entityManager.clear();
            return user;
        } catch (Exception e) {
            throw new UserAlreadyExistsException(String.format("Логин %s уже занят", userToSave.getLogin()));
        }
    }

}
