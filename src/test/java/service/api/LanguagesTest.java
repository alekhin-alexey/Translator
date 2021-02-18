package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import constants.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import service.json.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

class LanguagesTest {

    @Test
    void getLanguages() throws IOException {
        HashMap<String, String> mapLanguages;
        OkHttpClient client = new OkHttpClient();

        final String url = Constants.API_ENDPOINT + "/languages?api-version=3.0&scope=translation";
        Request request = new Request.Builder()
                .url(url).get()
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        mapLanguages = new HashMap<>();

        JsonNode node = Mapper.parseJson(Objects.requireNonNull(response.body()).string());
        Iterator<Map.Entry<String, JsonNode>> nodes = node.findValue(Constants.JSON_NODE_ROOT).fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            mapLanguages.put(
                    entry.getKey(),
                    entry.getValue()
                            .findValue(Constants.JSON_NODE_NAME).toString()
                            .replace("\"", ""));
        }

        System.out.println(mapLanguages);
    }
}