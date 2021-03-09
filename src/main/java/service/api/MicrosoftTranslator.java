package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import constants.Constants;
import model.LangTask;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.json.Mapper;
import view.MainForm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MicrosoftTranslator implements Translator {
    private Map<String, String> mapLanguages;
    private static final Logger logger = LoggerFactory.getLogger(MainForm.class);

    private String API_ENDPOINT = "https://api.cognitive.microsofttranslator.com/";
    private String API_PARSE_JSON = "application/json";

    @Override
    public Map<String, String> getLanguages() {
        try {
            mapLanguages = parseLanguages(getLanguagesFromApiJson());
        } catch (IOException e1) {
            logger.error("Error get or parse languages from Microsoft API (e1) !", e1);
            e1.printStackTrace();
        }
        return mapLanguages;
    }

    @Override
    public String translate(LangTask langTask, final String originalText) {
        String url = API_ENDPOINT + "/translate?api-version=3.0" +
                "&from=" + langTask.getLangFrom() +
                "&to=" + langTask.getLangTo();
        MediaType mediaType = MediaType.parse(API_PARSE_JSON);

        String response = null;
        try {
            String jsonText = "[" + Mapper.createJson(Constants.JSON_NODE_TEXT, originalText) + "]";
            RequestBody body = RequestBody.create(jsonText, mediaType);

            String API_KEY = "1b103f78822349e4a6590091aa6603a2";

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", API_KEY)
                    .addHeader("Content-type", API_PARSE_JSON)
                    .build();

            OkHttpClient client = new OkHttpClient();
            response = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e2) {
            logger.error("Error translate text (e2) !", e2);
            e2.printStackTrace();
        }
        return response;
    }

    /**
     * This function performs a GET request to Microsoft API
     *
     * @return - json string
     * @throws IOException handled in getLanguages method
     */
    private String getLanguagesFromApiJson() throws IOException {
        final String url = API_ENDPOINT + "/languages?api-version=3.0&scope=translation";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-type", API_PARSE_JSON)
                .build();

        Response response = new OkHttpClient().newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    private static Map<String, String> parseLanguages(final String src) throws IOException {
        Map<String, String> result = new HashMap<>();
        JsonNode node = Mapper.getObjectMapper().readTree(src);

        Iterator<Map.Entry<String, JsonNode>> nodes = node.findValue(Constants.JSON_NODE_ROOT).fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            result.put(
                    entry.getKey(),
                    entry.getValue()
                            .findValue(Constants.JSON_NODE_NAME).toString()
                            .replace("\"", ""));
        }
        return result;
    }
}
