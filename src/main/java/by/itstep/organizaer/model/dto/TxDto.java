package by.itstep.organizaer.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxDto {

    Long id;

    Long sourceAccountId;

    String sourceAccountName;

    Long targetAccountId;

    String targetAccountName;

    Float amount;

    LocalDateTime dateTime;

    FriendDto friend;
}
