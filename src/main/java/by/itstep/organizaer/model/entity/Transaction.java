package by.itstep.organizaer.model.entity;

import by.itstep.organizaer.model.entity.enums.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Long id;

    @Enumerated (value = EnumType.STRING)
    TransactionType transactionType;

    @ManyToOne (cascade = CascadeType.REFRESH)
    Account account;

    Float ammount;

    LocalDateTime dateTime;
}
