package io.example.javaboard.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @author : choi-ys
 * @date : 2021/09/22 6:11 오후
 */
public class LocalDateTimeUtils {

    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()
        );
    }
}
