package by.itstep.organizaer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@Builder
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
