package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    String name;

    Float ammount;

    Currency currency;
}
