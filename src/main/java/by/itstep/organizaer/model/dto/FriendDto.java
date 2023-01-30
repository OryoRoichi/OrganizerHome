package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.Contacts;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendDto {

    @NotBlank
    String name;

    LocalDate birthday;

    @NotNull
    ContactsDto contacts;

    UUID uuid;
}
