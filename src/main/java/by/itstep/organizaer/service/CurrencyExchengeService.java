package by.itstep.organizaer.service;

import by.itstep.organizaer.model.entity.enums.Currency;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyExchengeService {
    private float tmp = 2;

    public Float exchange(Float ammount, Currency from, Currency to) {
        if (from == to) {
            return ammount;
        }
        return ammount * tmp;
    }
}
