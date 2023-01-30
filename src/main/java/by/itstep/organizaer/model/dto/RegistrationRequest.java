package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {

    @Size(min = 5, max = 12)
    @NotBlank
    String login;

    @Size(min = 8, max = 20)
    @NotBlank
    String password;

    @Pattern(regexp = "^\\w+@\\w+\\.\\w+$")
    String email;

    String name;

    @NotBlank
    @Pattern(regexp = "^(\\+)+\\d+$")
    @Size(max = 16, min = 6)
    String phone;

    LocalDate birthDay;

    @JsonIgnore
    List<Roles> roles;
}
