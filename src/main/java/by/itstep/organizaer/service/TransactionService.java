package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.*;
import by.itstep.organizaer.model.dto.CreateTxRequestDto;
import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Friend;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.TransactionMapper;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import by.itstep.organizaer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionService {

    TransactionRepository repository;

    TransactionMapper mapper;

    FriendRepository friendRepository;

    AccountRepository accountRepository;

    public TxDto getTx(final Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public TxDto doTransact(CreateTxRequestDto request) {
        if (request.getAmount() == null) {
            throw new BadRequestException("Не указана сумма транзакции");
        }
        return doTransferTx(request);
    }

    private TxDto doTransferTx(CreateTxRequestDto request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Account sourceAccount = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(id -> accountRepository.findByIdAndUser(request.getSourceAccountId(), currentUser))
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = Optional.ofNullable(request.getTargetAccountId())
                .flatMap(id -> accountRepository.findById(request.getTargetAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        return Optional.of(sourceAccount)
                .filter(account -> account.getAmmount() >= request.getAmount())
                .map(account -> {
                    account.setAmmount(account.getAmmount() - request.getAmount());
                    targetAccount.setAmmount(targetAccount.getAmmount() + request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);
                    return Optional.ofNullable(request.getFriendId())
                            .flatMap(friendRepository::findById)
                            .map(friend -> mapper.toDto(createTransaction(request, friend, account, targetAccount)))
                            .orElseGet(() -> mapper.toDto(createTransaction(request, null, account, targetAccount)));

                })
                .orElseThrow(() -> new NotEnoughFoundsException(sourceAccount.getName()));
    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account sourceAccount, Account targetAccount) {
        return repository.save(Transaction
                .builder()
                .amount(request.getAmount())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .dateTime(LocalDateTime.now())
                .friend(friend)
                .build());
    }

    private Friend getFriend(Long id) {
        return Optional.ofNullable(id)
                .flatMap(friendRepository::findById)
                .orElseThrow(() -> new FriendNotFoundException("Не верный идентификатор друга"));
    }

}
