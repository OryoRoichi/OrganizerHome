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

    private static final String SEQ_NAME = "account_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
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
