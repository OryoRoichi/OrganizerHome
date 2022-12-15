package by.itstep.organizaer.model.mapping;

import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.model.entity.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, imports = FriendMapper.class)
public interface TransactionMapper {

    @Mapping(target = "accountId", source = "tx.account.id")
    @Mapping(target = "accountName", source = "tx.account.name")
    TxDto toDto(Transaction tx);
}
