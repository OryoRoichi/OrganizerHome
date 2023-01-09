package by.itstep.organizaer.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    UUID uuid;

    String name;

    @OneToOne
    Contacts contacts;

    LocalDateTime birthday;

    @ManyToOne
    User user;
}
