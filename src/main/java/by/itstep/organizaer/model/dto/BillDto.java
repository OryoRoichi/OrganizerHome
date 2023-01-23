package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {


    String accountName;
    @NotNull
    Long id;
    @NotNull
    Currency currency;

    LocalDateTime transactionDate;

    @NotNull
    Float amount;
}
