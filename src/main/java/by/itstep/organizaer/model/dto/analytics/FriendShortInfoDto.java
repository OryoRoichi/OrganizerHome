package by.itstep.organizaer.model.dto.analytics;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FriendShortInfoDto {
    Long id;
    String name;
}
