package by.itstep.organizaer.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleArchiveStatsDto extends ArchiveStatsDto{

    Float ammount;
}
