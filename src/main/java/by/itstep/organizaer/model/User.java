package by.itstep.organizaer.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "org_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String login;

    String password;

    String name;

    @OneToOne(cascade = CascadeType.ALL)
    Contacts contacts;

    LocalDate birthDay;

    @OneToMany(mappedBy = "user")
    List<Friend> friendList;
}
