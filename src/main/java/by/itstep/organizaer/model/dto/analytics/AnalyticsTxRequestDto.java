package by.itstep.organizaer.model.dto.analytics;

import by.itstep.organizaer.model.dto.enums.ArchiveStatsType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AnalyticsTxRequestDto {

    Long accountId;

    LocalDateTime dateFrom;

    LocalDateTime dateTo;

    ArchiveStatsType type;

    Float greaterThan;

    Float lessThan;

    List<Long> friendsIdList;
}
