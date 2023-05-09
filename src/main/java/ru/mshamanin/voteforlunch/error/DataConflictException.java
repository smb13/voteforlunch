package ru.mshamanin.voteforlunch.error;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg);
    }
}