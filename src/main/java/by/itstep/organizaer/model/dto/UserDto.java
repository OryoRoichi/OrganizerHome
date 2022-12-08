package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.Contacts;
import by.itstep.organizaer.model.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    Long id;

    String login;

    String password;

    String name;

    Contacts contacts;

    LocalDate birthDay;

    List<Friend> friendList;
}
