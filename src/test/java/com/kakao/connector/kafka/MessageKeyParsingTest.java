package com.kakao.connector.kafka;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageKeyParsingTest {

    @Test
    void parsingMessageKeyTest() {

        // 필드 : name : kakao
        String data = "{\"data\":{\"type\":\"company\",\"name\":\"kakao\",\"time\":\"20221128 120524 645\"}}";
        String keyField = "@.data.name";
        StringBuilder messageKey = new StringBuilder();
        List<String> fields = Arrays.asList(keyField.split(","));
        for (String field : fields) {
            messageKey.append(JsonPath.read(data, field).toString());
        }
        assertEquals("kakao", messageKey.toString());
    }

    @Test
    void parsingMessageKeysTest() {

        // 필드 : @.data.type : company
        // 필드 : @.data.name : kakao
        String data = "{\"data\":{\"type\":\"company\",\"name\":\"kakao\",\"time\":\"20221128 120524 645\"}}";
        String keysField = "@.data.type,@.data.name";
        StringBuilder messageKey = new StringBuilder();
        List<String> fields = Arrays.asList(keysField.split(","));
        for (String field : fields) {
            messageKey.append(JsonPath.read(data, field).toString());
        }
        assertEquals("companykakao", messageKey.toString());
    }
}
