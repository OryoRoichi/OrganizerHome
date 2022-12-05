package by.itstep.organizaer.service;

import by.itstep.organizaer.model.Contacts;
import by.itstep.organizaer.model.User;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactService {

    UserRepository repository;

    public Contacts createUserContact(Long userId, Contacts contacts) {
        return repository.findById(userId)
                .map(user -> {
                    user.setContacts(contacts);
                    return repository.save(user);
                })
                .map(User::getContacts)
                .orElse(null);
    }

    public Contacts createFriendContact(Long friendId, Contacts contacts) {
        return null;
    }
}
