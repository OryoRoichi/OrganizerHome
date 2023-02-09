package by.itstep.organizaer.model.dto.analytics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class EmptyAnalyticsResponseDto extends AbstractAnalyticsResponseDto {

    String message;
}
