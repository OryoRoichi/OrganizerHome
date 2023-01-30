package by.itstep.organizaer.model.mapping;

import by.itstep.organizaer.model.dto.FriendDto;
import by.itstep.organizaer.model.entity.Friend;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", imports = ContactMapper.class)
public interface FriendMapper {

    FriendDto toDto(Friend friend);

    Friend toEntity(FriendDto friendDto);
}
