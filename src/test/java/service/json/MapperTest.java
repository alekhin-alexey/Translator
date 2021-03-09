package service.json;

import constants.Constants;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {

    @Test
    void parseValue() throws IOException {
        final String expected = "Test value";
        String json = "{\"text\":\"Test value\"}";

        String result = Mapper.parseValue(Constants.JSON_NODE_TEXT, json);

        assertEquals(expected, result);

        System.out.println(result);
    }

    @Test
    void createJson() throws IOException {
        final String expected = "{\"text\":\"Test value\"}";
        final String value = "Test value";
        final String result = Mapper.createJson(Constants.JSON_NODE_TEXT, value);

        assertEquals(expected, result);

        System.out.println(result);
    }
}