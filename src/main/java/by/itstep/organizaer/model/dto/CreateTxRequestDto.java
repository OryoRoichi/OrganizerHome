package by.itstep.organizaer.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTxRequestDto {

    @NotNull
    Long sourceAccountId;

    @NotNull
    Long targetAccountId;

    @NotNull
    Float amount;

    Boolean isAutoConverted = false;
}
