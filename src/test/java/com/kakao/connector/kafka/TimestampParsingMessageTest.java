package com.kakao.connector.kafka;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimestampParsingMessageTest {

    @Test
    void parsingTimestampTest() {

        // 필드 : data > time
        // 포맷 : 20211128 120524 645
        // 포맷 : yyyyMMdd HHmmss SSS
        String data = "{\"data\":{\"type\":\"company\",\"name\":\"kakao\",\"time\":\"20221128 120524 645\"}}";

        String timestampField = "@.data.time";
        String timestampFormat = "yyyyMMdd HHmmss SSS";
        String timestamp = JsonPath.read(data, timestampField).toString();
        assertEquals("20221128 120524 645", timestamp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampFormat);
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        long timestamp2 = Timestamp.valueOf(dateTime).getTime();
        assertEquals(1669604724645L, timestamp2);
    }
}
