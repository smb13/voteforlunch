package ru.mshamanin.voteforlunch.error;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg);
    }
}