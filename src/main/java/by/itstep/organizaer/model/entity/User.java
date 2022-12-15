package by.itstep.organizaer.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "org_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID uuid;

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
