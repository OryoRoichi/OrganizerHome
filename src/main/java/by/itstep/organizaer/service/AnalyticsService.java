package by.itstep.organizaer.service;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.exceptions.AccountNotFoundException;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.analytics.*;
import by.itstep.organizaer.model.dto.enums.ArchiveStatsType;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import by.itstep.organizaer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.PrimitiveIterator;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnalyticsService {

    TransactionRepository txRepository;

    EntityManager entityManager;
    AccountRepository accountRepository;
    ProjectConfiguration projectConfiguration;


    @Transactional
    public AbstractAnalyticsResponseDto getTxAnalytics(AnalyticsTxRequestDto requestDto) throws AuthenticationException {
        User user = SecurityUtil.getCurrentUser().orElseThrow(() -> new AuthenticationException());

        Account account = accountRepository.findByIdAndUser(requestDto.getAccountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(requestDto.getAccountId()));

        String query = "select * from transaction where ";
        if (requestDto.getType() == ArchiveStatsType.INCOME) {
            query = query.concat("target_account = ").concat(requestDto.getAccountId().toString()).concat(" ");
        }
        if (requestDto.getType() == ArchiveStatsType.SPEND) {
            query = query.concat("source_account = ").concat(requestDto.getAccountId().toString()).concat(" ");
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateFrom())) {
            query = query.concat("and date_time > ").concat(requestDto.getDateFrom().toString()).concat(" ");
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateTo())) {
            query = query.concat("and date_time < ").concat(requestDto.getDateTo().toString()).concat(" ");
        }
        if (ObjectUtils.isNotEmpty(requestDto.getLessThan())) {
            query = query.concat("and amount < ").concat(requestDto.getLessThan().toString()).concat(" ");
        }
        if (ObjectUtils.isNotEmpty(requestDto.getGreaterThan())) {
            query = query.concat("and amount > ").concat(requestDto.getGreaterThan().toString()).concat(" ");
        }
        query = query.concat(";");
        Query entityQuery = entityManager.createNativeQuery(query, Transaction.class);
        List<Transaction> transactions = entityQuery.getResultList();

        if (ObjectUtils.isEmpty(transactions)) {
            EmptyAnalyticsResponseDto.EmptyAnalyticsResponseDtoBuilder builder = EmptyAnalyticsResponseDto.builder();
            buildDate(requestDto, builder);
            builder.message("данные не найдены");
            return builder.accountName(account.getName()).build();
        }
        if (requestDto.getType() != ArchiveStatsType.ALL) {
            SingleTypeAnalyticsResponseDto.SingleTypeAnalyticsResponseDtoBuilder builder = SingleTypeAnalyticsResponseDto.builder().accountName(account.getName())
                    .ammount(transactions.stream().map((tx) -> tx.getAmount()).reduce(Float::sum).orElse(0f));
            buildDate(requestDto,builder);
            return builder.build();

        }
        MultipleTypesAnalyticsResponseDto.MultipleTypesAnalyticsResponseDtoBuilder builder = MultipleTypesAnalyticsResponseDto.builder()
                .incomeAmount(transactions.stream().filter((tx)-> tx.getTargetAccount()= account).map((tx)-> tx.getAmount()).reduce(Float::sum).orElse(0f))


        return SingleTypeAnalyticsResponseDto.builder()
                .ammount(transactions.stream().map(Transaction::getAmount).reduce(Float::sum).orElse(0F))
                .accountName(transactions.stream().map(tx -> tx.getTargetAccount().getName()).findAny().orElse(null))
                .build();
    }

    private <T extends AbstractAnalyticsResponseDto.AbstractAnalyticsResponseDtoBuilder> void buildDate(AnalyticsTxRequestDto requestDto, T builder) {
        if (ObjectUtils.isNotEmpty(requestDto.getDateFrom()) && ObjectUtils.isNotEmpty(requestDto.getDateTo())) {
            builder.dateFrom(requestDto.getDateFrom()).dateTo(requestDto.getDateTo());
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateFrom()) && ObjectUtils.isEmpty(requestDto.getDateTo())) {
            builder.dateFrom(requestDto.getDateFrom()).dateTo(LocalDateTime.now());
        }
        if (ObjectUtils.isEmpty(requestDto.getDateFrom()) && ObjectUtils.isNotEmpty(requestDto.getDateTo())) {
            builder.dateFrom(LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays())).dateTo(requestDto.getDateTo());
        }
        if (ObjectUtils.isEmpty(requestDto.getDateFrom()) && ObjectUtils.isEmpty(requestDto.getDateTo())) {
            builder.dateFrom(LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays())).dateTo(LocalDateTime.now());
        }

    }

}
