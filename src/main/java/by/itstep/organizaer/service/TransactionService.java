package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.*;
import by.itstep.organizaer.model.dto.CreateTxRequestDto;
import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Friend;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.model.mapping.TransactionMapper;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.getType() == null) {
            throw new UnsuppertedTransactionException("Не заполнен тип транзакции");
        }
        if (request.getAmount() == null) {
            throw new BadRequestException("Не указана сумма транзакции")
        }
        switch (request.getType()) {
            case INCOME:
                return doIncomeTx(request);
            case OUTCOME:
                return doOutcomeTx(request);
        }

    }

    private TxDto doIncomeTx(CreateTxRequestDto request) {
        final Friend friend = getFriend(request.getFriendId());
        return Optional.ofNullable(request.getTargetAccountId())
                .flatMap(accountRepository::findById)
                .map(account -> {
                    account.setAmmount(account.getAmmount() + request.getAmount());
                    accountRepository.save(account);
                    return mapper.toDto(createTransaction(request, friend, account));
                })
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
    }

    private TxDto doOutcomeTx(CreateTxRequestDto request) {
        final Friend friend = getFriend(request.getFriendId());
        final Account account = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(accountRepository::findById)
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        return Optional.of(account)
                .filter(acc -> acc.getAmmount() >= request.getAmount())
                .map(acc -> {
                    acc.setAmmount(acc.getAmmount() - request.getAmount());
                    accountRepository.save(acc);
                    return mapper.toDto(createTransaction(request, friend, acc));
                })
                .orElseThrow(() -> new NotEnoughFoundsException(account.getName()));
    }

    private TxDto doTransferTx(CreateTxRequestDto request){
        Account sourceAccount = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(id -> accountRepository.findById(request.getSourceAccountId()))
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
                    return mapper.toDto(createTransaction(request, null, account, targetAccount))
                })
                .orElseThrow(() -> new NotEnoughFoundsException(sourceAccount.getName()));

    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account account) {
        return repository.save(Transaction
                .builder()
                .transactionType(request.getType())
                .ammount(request.getAmount())
                .account(account)
                .friend(friend)
                .build());
    }

    private Friend getFriend(Long id) {
        return Optional.ofNullable(id)
                .flatMap(friendRepository::findById)
                .orElseThrow(() -> new FriendNotFoundException("Не верный идентификатор друга"));
    }

}
