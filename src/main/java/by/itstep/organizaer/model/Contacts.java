package by.itstep.organizaer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contacts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String address;

    @ElementCollection
    List<String> phones;

    @ElementCollection
    List<String> email;

    @ElementCollection
    List<String> messengers;
}
