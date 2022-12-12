package by.itstep.organizaer.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String name) {
        super(String.format("Аккаунт с именем %s уже существует", name));
    }
}
