package by.itstep.organizaer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "org_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String login;

    String password;

    String name;

    @OneToOne(cascade = CascadeType.ALL)
    Contacts contacts;

    LocalDateTime birthDay;

    @OneToMany(mappedBy = "user")
    List<Friend> friendList;
}
