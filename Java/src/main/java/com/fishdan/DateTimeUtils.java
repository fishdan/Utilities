package com.fishdan;
import java.time.*;
import java.time.temporal.Temporal;


public class DateTimeUtils {
    public static Instant toInstant(Temporal temporal) {
        if (temporal instanceof LocalDateTime) {
            // Assuming the system default zone; adjust as necessary
            return ((LocalDateTime) temporal).atZone(ZoneId.systemDefault()).toInstant();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toInstant();
        } else if (temporal instanceof Instant) {
            return (Instant) temporal;
        }
        throw new IllegalArgumentException("Unsupported Temporal type: " + temporal.getClass().getName());
    }
}
