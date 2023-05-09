package ru.mshamanin.voteforlunch.error;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}