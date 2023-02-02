package by.itstep.organizaer.model.dto;

import by.itstep.organizaer.model.entity.Contacts;
import by.itstep.organizaer.model.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    Long id;

    String login;

    String name;

    Contacts contacts;

    UUID uuid;

    LocalDate birthDay;

    List<Friend> friendList;
}
