package org.example.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONObject;

public class SerializeHelper {

    @SneakyThrows
    public static <T> T extractAs(Class<T> clazz, String response) {
        JSONObject jsonObject = new JSONObject(response);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(jsonObject.toMap(), clazz);
    }
}
