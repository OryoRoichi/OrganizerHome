package by.itstep.organizaer.service;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Archive;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.ArchiveRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArchivationService {

    TransactionRepository transactionRepository;

    AccountRepository accountRepository;

    ArchiveRepository archiveRepository;

    ProjectConfiguration projectConfiguration;

    public void archivate() {
        LocalDateTime before = LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        accountRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(), account -> transactionRepository.findByAccount(account, before)))
                .forEach((key, value) -> {
                    Float spendAmount = value
                            .stream()
                            .filter(tx -> tx.getSourceAccount().getId().equals(key.getId()))
                            .map(Transaction::getAmount)
                            .reduce(Float::sum)
                            .orElse(0F);
                    Float incomeAmount = value
                            .stream()
                            .filter(tx -> tx.getTargetAccount().getId().equals(key.getId()))
                            .map(Transaction::getAmount)
                            .reduce(Float::sum)
                            .orElse(0F);
                    archiveRepository.save(Archive.builder()
                            .account(key)
                            .spend(spendAmount)
                            .income(incomeAmount)
                            .build());
                });
    }
}
