package service.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;

import java.io.IOException;

public class Mapper {
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultObjectMapper;
    }

    public static JsonNode parseJson(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    public static String createJson(String src) throws IOException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put(Constants.JSON_NODE_TEXT, src);
        return "[".concat(objectMapper.writeValueAsString(node)).concat("]");
    }
}
