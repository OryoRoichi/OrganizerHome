package by.itstep.organizaer.model.mapping;

import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.model.entity.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, imports = FriendMapper.class)
public interface TransactionMapper {

    @Mapping(target = "sourceAccountId", source = "tx.sourceAccount.id")
    @Mapping(target = "sourceAccountName", source = "tx.sourceAccount.name")
    @Mapping(target = "targetAccountId", source = "tx.targetAccount.id")
    @Mapping(target = "targetAccountName", source = "tx.targetAccount.name")
    TxDto toDto(Transaction tx);
}
