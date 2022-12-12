package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.AccountAlreadyExistsException;
import by.itstep.organizaer.exceptions.AccountNotFoundException;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.mapping.AccountMapper;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;



@Service
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountService {

    AccountRepository accountRepository;

    AccountMapper accountMapper;

    UserRepository userRepository;

    public AccountDto createAccount(AccountDto accountDto){
        Account accountToSave = accountMapper.toEntity(accountDto);
        accountToSave.setUser(userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException(1L)));
        try {
            accountRepository.save(accountToSave);
        } catch (Exception ex) {
            throw new AccountAlreadyExistsException(accountDto.getName());
        }
        return accountMapper.toDto(accountToSave);
    }

    public AccountDto getAccountById(Long id) {
        return accountMapper.toDto(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }
}
