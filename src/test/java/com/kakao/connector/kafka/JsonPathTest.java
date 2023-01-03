package com.kakao.connector.kafka;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JsonPathTest {

    @Test
    void validTest() {
        String data = "{\"data\":{\"type\":\"company\",\"name\":\"kakao\",\"time\":\"20221128 120524 645\"}}";
        String jsonPath = "$.[?(@.data.name == 'kakao')]";
        String expectedData = "[]";
        String result = JsonPath.read(data, jsonPath).toString();
        assertNotEquals(result, expectedData);
    }
}