package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {

    String login;

    String password;

    String name;

    LocalDate birthDay;

    @JsonIgnore
    List<Roles> roles;
}
