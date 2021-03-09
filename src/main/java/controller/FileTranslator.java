package controller;

import model.FileTask;
import model.LangTask;
import service.api.Translator;
import service.json.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTranslator {
    private Translator translator;
    private final LangTask lang;
    private FileTask fileData;
    private Map<String, String> dictionary = new HashMap<>();

    public FileTranslator(Translator translator, LangTask lang, FileTask fileData) {
        this.translator = translator;
        this.lang = lang;
        this.fileData = fileData;
    }

    /**
     * Translation of the text for each sentence
     * @param noteText
     * @return fileData - map (key = translated text)
     * @throws IOException handled in MainForm
     */
    public FileTask translateSentence(final String noteText) throws IOException {
        for (Map.Entry<String, String> item : fileData.getContent().entrySet()) {
            String result = Mapper.parseValue(noteText, translator.translate(lang, item.getValue()));
            if (!result.isEmpty()) {
                dictionary.put(item.getValue(), result);
                fileData.getContent().put(item.getKey(), result);
            }
        }
        return fileData;
    }

    /**
     * "dictionary" - map original text = translated text
     *
     * @return dictionary
     */
    public Map<String, String> getDictionary() {
        return dictionary;
    }

}
