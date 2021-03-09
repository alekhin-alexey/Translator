package service.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class Mapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public static String parseValue(final String key, final String value) throws IOException {
        return objectMapper
                .readTree(value)
                .findValue(key)
                .textValue();
    }

    public static String createJson(final String key, final String value) throws IOException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put(key, value);
        return objectMapper
                .writeValueAsString(node);
    }
}
