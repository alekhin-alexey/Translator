package service.api;

import com.fasterxml.jackson.databind.JsonNode;
import constants.Constants;
import model.Lang;
import okhttp3.*;
import service.json.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Translate {
    private final String textOriginal;
    private final String langSuffixFrom;
    private final Lang userLang;

    public Translate(String textOriginal, String langSuffixFrom, Lang userLang) {
        this.textOriginal = textOriginal;
        this.langSuffixFrom = langSuffixFrom;
        this.userLang = userLang;
    }

    // ... This function performs a POST request ...
    private String post() throws IOException {
        String url = Constants.API_ENDPOINT + "/translate?api-version=3.0" +
                "&from=" + langSuffixFrom +
                "&to=" + userLang.getLangSuffixTo();
        MediaType mediaType = MediaType.parse(Constants.API_PARSE_JSON);
        RequestBody body = RequestBody.create(mediaType, Mapper.createJson(textOriginal));
        Request request = new Request.Builder()
                .url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", Constants.API_KEY)
                .addHeader("Content-type", Constants.API_PARSE_JSON)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * Translation of text (textOriginal) from language (langSuffixFrom) to (userLang.getLangSuffixTo ())
     * Formation of Json string for Translate Service API (Request body)
     * Getting translated text in Json format - response
     *
     * @return Map<String, String> result
     * Example: [Hello=Bonjour]
     * @throws IOException - handled in MainForm
     */
    public Map<String, String> translate() throws IOException {
        String response = post();
        JsonNode node = Mapper.parseJson(response);
        Map<String, String> result = new HashMap<>();
        String textTranslated = node.findValue(Constants.JSON_NODE_TEXT).textValue();
        result.put(textOriginal, textTranslated);
        return result;
    }


}
