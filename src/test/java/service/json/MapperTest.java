package service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {
    //private String langFromAPI = "{\"translation\": \"test\"}";
    //private String langFromAPI = "{\"translation\":{\"af\":{\"name\":\"Afrikaans\",\"nativeName\":\"Afrikaans\",\"dir\":\"ltr\"}}}";
    private String langFromAPI = "{\"translation\":{\n" +
            "  \"af\":{\"name\":\"Afrikaans\",\"nativeName\":\"Afrikaans\",\"dir\":\"ltr\"},\n" +
            "  \"ar\":{\"name\":\"Arabic\",\"nativeName\":\"العربية\",\"dir\":\"rtl\"},\n" +
            "  \"as\":{\"name\":\"Assamese\",\"nativeName\":\"Assamese\",\"dir\":\"ltr\"}}}";

    @Test
    void parseJson() throws IOException {
        JsonNode node = Mapper.parseJson(langFromAPI);
        JsonNode jsonText = node.findValue("translation");
        System.out.println(jsonText);
        //jsonText.forEach(x -> System.out.println("x" + "=" + x.findValue("name").toString()));


        Iterator<Map.Entry<String, JsonNode>> nodes = node.findValue("translation").fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();

            System.out.println(entry.getKey().toUpperCase() + "=" + entry.getValue().findValue("name").toString());
        }
        //System.out.println(jsonText.);

        //LangDataAPI langAPI = ParseJSON.fromJson(jsonText, LangDataAPI.class);
    }

    @Test
    void createJson() throws JsonProcessingException {
        String src = "Hello";
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode node = objectMapper.createObjectNode();
        node.put("text", src);
        System.out.println("[" + objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(node) +
                "]");

        //System.out.println("[".concat(objectMapper.writeValueAsString(node)).concat("]"));

    }

    @Test
    void parseSimple() throws IOException {
        JsonNode node = Mapper.parseJson(langFromAPI);

        //System.out.println(node.get("translation").asText());
        System.out.println(node.findValue("translation").toString());
        assertEquals(node.get("translation").asText(), "translation");

    }

    /*
    public static <T> T fromJson(JsonNode node, Class<T> clazz) throws IOException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    public static String prettyPrint(JsonNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    private static String generateString(JsonNode node, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(node);
    }
 */
}