package by.itstep.organizaer.model.dto.analytics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultipleTypesAnalyticsResponseDto extends AbstractAnalyticsResponseDto{

    Float incomeAmount;

    Float spendAmount;
}
