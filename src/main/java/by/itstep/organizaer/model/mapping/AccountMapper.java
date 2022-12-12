package by.itstep.organizaer.model.mapping;

import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.model.entity.Account;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto accountDto);

    AccountDto toDto(Account account);

}
