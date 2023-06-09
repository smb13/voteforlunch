package ru.mshamanin.voteforlunch.util;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public final class ClockHolder {

    protected final Logger log = LoggerFactory.getLogger(ClockHolder.class);

    private static final AtomicReference<Clock> clockReference = new AtomicReference<>(Clock.systemDefaultZone());

    @Nonnull
    public static Clock getClock() {
        return clockReference.get();
    }

    @Nonnull
    public static Clock setClock(@Nonnull final Clock newClock) {
        Objects.requireNonNull(newClock, "newClock cannot be null");
        final Clock oldClock = clockReference.getAndSet(newClock);
        log.info("Set new clock {}. Old clock is {}", newClock, oldClock);
        return oldClock;
    }
}