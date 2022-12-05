package by.itstep.organizaer.web;

import by.itstep.organizaer.model.Contacts;
import by.itstep.organizaer.service.ContactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactsController {

    ContactService contactService;

    @PostMapping("/createUserContact")
    public ResponseEntity<Contacts> create(@RequestParam Long userId, @RequestBody Contacts contacts) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contactService.createUserContact(userId, contacts));
    }
}
