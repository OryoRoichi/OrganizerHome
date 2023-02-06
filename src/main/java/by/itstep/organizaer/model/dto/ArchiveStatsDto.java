package by.itstep.organizaer.model.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@SuperBuilder
@Data
public class ArchiveStatsDto {

    String accountName;

    LocalDate dateFrom;

    LocalDate dateTo;
}
