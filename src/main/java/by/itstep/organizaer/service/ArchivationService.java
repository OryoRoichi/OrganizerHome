package by.itstep.organizaer.service;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.model.entity.Archive;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.ArchiveRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
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

    @Async
    @Transactional
    @Scheduled(cron = "${project.business.sheduling.evening-cron}")
    public void archiveEvening() {
        archivate();
    }

    @Async
    @Transactional
    @Scheduled(cron = "${project.business.sheduling.morning-cron}")
    public void archivate() {
        System.out.println("" + LocalTime.now() + " ПРОЦЕДУРА АРХИВИРОВАНИЯ ЗАПУЩЕНА");
        LocalDateTime before = LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        transactionRepository.deleteAll(
                accountRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), account -> transactionRepository.findByAccount(account, before)))
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            Float spendAmount = entry.getValue()
                                    .stream()
                                    .filter(tx -> tx.getSourceAccount().getId().equals(entry.getKey().getId()))
                                    .map(Transaction::getAmount)
                                    .reduce(Float::sum)
                                    .orElse(0F);
                            Float incomeAmount = entry.getValue()
                                    .stream()
                                    .filter(tx -> tx.getTargetAccount().getId().equals(entry.getKey().getId()))
                                    .map(Transaction::getAmount)
                                    .reduce(Float::sum)
                                    .orElse(0F);
                            archiveRepository.save(Archive.builder()
                                    .account(entry.getKey())
                                    .spend(spendAmount)
                                    .income(incomeAmount)
                                    .build());
                            return entry.getValue();
                        })
                        .flatMap(list -> list.stream())
                        .collect(Collectors.toList()));
        System.out.println("" + LocalTime.now() + " ПРОЦЕДУРА АРХИВИРОВАНИЯ ЗАВЕРШЕНА");
    }
}
