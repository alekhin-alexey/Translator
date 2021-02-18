package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import service.json.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslateTest {

    @Test
    void translate() throws IOException {
        String langFrom = "EN";
        String langTo = "FR";
        String textOriginal = "Press Ctrl + F to use quick search.";
        //String textOriginal = "Hello";
        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode node = objectMapper.createObjectNode();
        node.put("text", textOriginal);
        String json = "[" + objectMapper.writeValueAsString(node) + "]";
        System.out.println(json);

        String url = Constants.API_ENDPOINT + "/translate?api-version=3.0&from=" + langFrom + "&to=" + langTo;
        //String textJson = Mapper.create(textOriginal);
        MediaType mediaType = MediaType.parse(Constants.API_PARSE_JSON);
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", Constants.API_KEY)
                .addHeader("Content-type", Constants.API_PARSE_JSON)
                .build();
        Response response = client.newCall(request).execute();

        String ss = Objects.requireNonNull(response.body()).string();
        //System.out.println(ss);

        JsonNode node1 = Mapper.parseJson(ss);
        //System.out.println(node1.findValue(Constants.JSON_NODE_TEXT).textValue());

        Map<String, String> dictionary = new HashMap<>();
        dictionary.put(textOriginal, node1.findValue(Constants.JSON_NODE_TEXT).textValue());
        dictionary.forEach((k, v)-> System.out.println("original: " + k +"; translated: " + v));

        assertEquals(json, "[{\"text\":\"Press Ctrl + F to use quick search.\"}]");
        assertEquals(dictionary.toString(), "{Press Ctrl + F to use quick search.=Appuyez sur Ctrl + F pour utiliser la recherche rapide.}");
    }
}