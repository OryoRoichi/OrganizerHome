package by.itstep.organizaer.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTxRequestDto {

    Long sourceAccountId;

    Long targetAccountId;

    Float amount;

    Boolean isAutoConverted = false;
}
