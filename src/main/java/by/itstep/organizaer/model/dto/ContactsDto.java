package by.itstep.organizaer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class ContactsDto {

    String address;

    @NotBlank
    @Pattern(regexp = "^(\\+)+\\d+$")
    @Size(max = 16, min = 6)
    String phone;


    List<String> email;


    List<String> messengers;
}
