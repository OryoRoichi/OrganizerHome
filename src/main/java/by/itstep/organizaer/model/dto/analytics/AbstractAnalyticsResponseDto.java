package by.itstep.organizaer.model.dto.analytics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public abstract class AbstractAnalyticsResponseDto {

    String accountName;

    LocalDateTime dateFrom;

    LocalDateTime dateTo;
}
