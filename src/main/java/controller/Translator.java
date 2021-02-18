package controller;

import model.FileData;
import model.Lang;
import service.api.Translate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Translator {
    private final Lang userLang;
    private FileData fileData;
    private Map<String, String> dictionary = new HashMap<>();

    public Translator(Lang userLang, FileData fileData) {
        this.userLang = userLang;
        this.fileData = fileData;
    }

    /**
     * Translation of the text for each sentence
     * <p>
     * translateData - service.api.Translate - json text translation API method
     * <p>
     * fileData  item.getKey()     - unique tag attribute parameter from file
     * fileData  item.getValue()   - original text
     * fileData.getLangContent()   - original language suffix
     * userLang.getLangSuffixTo()  - language suffix to translate
     * <p>
     * Example:
     * item.getKey() - key="param"
     * item.getValue() - "Hello"
     * fileData.getLangContent() - "en"
     * userLang.getLangSuffixTo() - "fr"
     * <p>
     * request  - [{"text" : "Hello"}]
     * response - [{"translations":[{"text":"Bonjour","to":"fr"}]}]
     *
     * @return fileData - map (key = translated text)
     * Example:
     * { key="param" = Bonjour }
     */
    public FileData translateSentence() throws IOException {
        for (Map.Entry<String, String> item : fileData.getContent().entrySet()) {
            Translate translateData = new Translate(item.getValue(), fileData.getLangContent(), userLang);
            Map<String, String> result = translateData.translate();
            if (!result.values().isEmpty()) {
                dictionary.put(item.getValue(), result.get(item.getValue()));
                fileData.getContent().put(item.getKey(), result.get(item.getValue()));
            }
        }
        return fileData;
    }

    /**
     * "dictionary" - map original text = translated text
     *
     * @return dictionary
     * Example:
     * { — account name=— nom du compte,
     * NEW!=Nouveau!
     * }
     */
    public Map<String, String> getDictionary() {
        return dictionary;
    }
}
