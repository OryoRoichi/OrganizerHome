package by.itstep.organizaer.model.mapping;

import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto dto);

    UserDto toDto(User user);
}
