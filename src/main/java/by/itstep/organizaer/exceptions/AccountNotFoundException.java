package by.itstep.organizaer.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long id) {
        super(String.format("Счет с id = %d не найден",id));
    }
}
