package by.itstep.organizaer.exceptions;

public class NotEnoughFoundsException extends RuntimeException {

    public NotEnoughFoundsException(String accountName) {
        super(String.format("Не достаточно средств на счете %s", accountName));
    }
}
