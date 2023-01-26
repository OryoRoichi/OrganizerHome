package by.itstep.organizaer.model.entity;

import by.itstep.organizaer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    String name;

    Float ammount;

    @Enumerated (value = EnumType.STRING)
    Currency currency;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;
}
