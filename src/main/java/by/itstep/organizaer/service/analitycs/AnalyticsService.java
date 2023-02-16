package by.itstep.organizaer.service.analitycs;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.exceptions.AccountNotFoundException;
import by.itstep.organizaer.exceptions.FriendNotFoundException;
import by.itstep.organizaer.model.dto.analytics.*;
import by.itstep.organizaer.model.dto.enums.ArchiveStatsType;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.FriendRepository;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnalyticsService {

    TransactionRepository txRepository;

    EntityManager entityManager;
    AccountRepository accountRepository;
    ProjectConfiguration projectConfiguration;
    private final FriendRepository friendRepository;


    @Transactional
    public List<AbstractAnalyticsResponseDto> getTxAnalytics(AnalyticsTxRequestDto requestDto) throws AuthenticationException {
        List<AbstractAnalyticsResponseDto> resultList = new ArrayList<>();

        String query = buildingQuery(requestDto);
        Query entityQuery = entityManager.createNativeQuery(query, Transaction.class);
        List<Transaction> transactions = entityQuery.getResultList();

        if (ObjectUtils.isEmpty(requestDto.getFriendsIdList())) {
            resultList.add(buildingAnaliticsResponseDto(requestDto, transactions, null));
            return resultList;
        } else {
            Map<Long, List<Transaction>> friendTxMap = new HashMap<>();

            for (Transaction transaction : transactions) {
                Long friendId = transaction.getFriend().getId();

                if (friendTxMap.containsKey(friendId)) {
                    List<Transaction> tmp = friendTxMap.get(friendId);
                    tmp.add(transaction);
                    friendTxMap.replace(friendId, tmp);
                } else {
                    List<Transaction> txList = new ArrayList<>();
                    txList.add(transaction);
                    friendTxMap.put(friendId, txList);
                }
            }
            for (Map.Entry<Long, List<Transaction>> entry : friendTxMap.entrySet()) {
                Long friendId = entry.getKey();
                String friendName = friendRepository.findById(friendId)
                        .orElseThrow(() -> new FriendNotFoundException("друг не найден"))
                        .getName();
                FriendShortInfoDto friendShortInfoDto = FriendShortInfoDto.builder().id(friendId).name(friendName).build();
                resultList.add(buildingAnaliticsResponseDto(requestDto, entry.getValue(), friendShortInfoDto));
            }
            return resultList;
        }
    }

    private String buildingQuery(AnalyticsTxRequestDto requestDto) {
        String query = "select * from transaction where ";
        if (requestDto.getType() == ArchiveStatsType.ALL) {
            query = query.concat("(target_account = ")
                    .concat(requestDto.getAccountId().toString())
                    .concat(" or ")
                    .concat("source_account = ")
                    .concat(requestDto.getAccountId().toString())
                    .concat(") ");
        }

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

        return query.concat(";");
    }

    private AbstractAnalyticsResponseDto buildingAnaliticsResponseDto(AnalyticsTxRequestDto requestDto, List<Transaction> transactions, FriendShortInfoDto friendShortInfoDto) throws AuthenticationException {
        User user = SecurityUtil.getCurrentUser().orElseThrow(() -> new AuthenticationException());

        Account account = accountRepository.findByIdAndUser(requestDto.getAccountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(requestDto.getAccountId()));

        if (ObjectUtils.isEmpty(transactions)) {
            return getEmptyResponseDto(requestDto, account, friendShortInfoDto);
        }
        if (requestDto.getType() != ArchiveStatsType.ALL) {
            return getSingleTypeResponseDto(requestDto, account, transactions, friendShortInfoDto);

        }
        return getAllTypesResponseDto(requestDto, account, transactions, friendShortInfoDto);
    }

    private MultipleTypesAnalyticsResponseDto getAllTypesResponseDto(AnalyticsTxRequestDto requestDto, Account account, List<Transaction> transactions, FriendShortInfoDto friendShortInfoDto) {
        MultipleTypesAnalyticsResponseDto.MultipleTypesAnalyticsResponseDtoBuilder builder = MultipleTypesAnalyticsResponseDto.builder()
                .incomeAmount(transactions.stream()
                        .filter((tx) -> tx.getTargetAccount().getId().equals(account.getId()))
                        .map((tx) -> tx.getAmount())
                        .reduce(Float::sum).orElse(0f))
                .spendAmount(transactions.stream()
                        .filter((tx) -> tx.getSourceAccount().getId().equals(account.getId()))
                        .map((tx) -> tx.getAmount())
                        .reduce(Float::sum).orElse(0f))
                .accountName(account.getName())
                .friend(friendShortInfoDto);
        buildDate(requestDto, builder);
        return builder.build();
    }

    private SingleTypeAnalyticsResponseDto getSingleTypeResponseDto(AnalyticsTxRequestDto requestDto, Account account, List<Transaction> transactions, FriendShortInfoDto friendShortInfoDto) {
        SingleTypeAnalyticsResponseDto.SingleTypeAnalyticsResponseDtoBuilder builder = SingleTypeAnalyticsResponseDto.builder()
                .accountName(account.getName())
                .ammount(transactions.stream()
                        .map((tx) -> tx.getAmount())
                        .reduce(Float::sum).orElse(0f))
                .friend(friendShortInfoDto);
        buildDate(requestDto, builder);
        return builder.build();
    }

    private EmptyAnalyticsResponseDto getEmptyResponseDto(AnalyticsTxRequestDto requestDto, Account account, FriendShortInfoDto friendShortInfoDto) {
        EmptyAnalyticsResponseDto.EmptyAnalyticsResponseDtoBuilder builder = EmptyAnalyticsResponseDto.builder();
        builder.accountName(account.getName());
        builder.message("данные не найдены");
        builder.friend(friendShortInfoDto);
        buildDate(requestDto, builder);
        return builder.build();
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
