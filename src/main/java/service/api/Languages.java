package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import constants.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import service.json.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Languages {
    private static final Languages languages = new Languages();
    private HashMap<String, String> mapLanguages;
    private OkHttpClient client = new OkHttpClient();

    private Languages() {
    }

    public static Languages getInstance() {
        return languages;
    }

    public HashMap<String, String> getLanguages() throws IOException {
        if (mapLanguages == null) {
            mapLanguages = initLanguages();
        }
        return mapLanguages;
    }

    private HashMap<String, String> initLanguages() throws IOException {
        mapLanguages = new HashMap<>();
        JsonNode node = Mapper.parseJson(getLangFromApiJson());
        Iterator<Map.Entry<String, JsonNode>> nodes = node.findValue(Constants.JSON_NODE_ROOT).fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            mapLanguages.put(
                    entry.getKey(),
                    entry.getValue()
                            .findValue(Constants.JSON_NODE_NAME).toString()
                            .replace("\"", ""));
        }
        return mapLanguages;
    }

    // ... This function performs a GET request to API ...
    private String getLangFromApiJson() throws IOException {
        final String url = Constants.API_ENDPOINT + "/languages?api-version=3.0&scope=translation";
        Request request = new Request.Builder()
                .url(url).get()
                .addHeader("Content-type", Constants.API_PARSE_JSON)
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
