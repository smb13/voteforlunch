package ru.mshamanin.voteforlunch.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public final class DateTimeUtil {
    public static final LocalTime TIME_TO_CHANGE_MIND = LocalTime.of(11, 0);

    public static boolean inTime() {
        LocalTime localTime = LocalTime.now(ClockHolder.getClock());
        return localTime.isBefore(TIME_TO_CHANGE_MIND);
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now(ClockHolder.getClock());
    }
}
