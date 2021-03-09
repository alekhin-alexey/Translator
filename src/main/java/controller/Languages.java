package controller;

import constants.Constants;
import service.api.Translator;
import service.api.TranslatorFactory;
import service.api.TypesAPI;

import java.util.Map;

public class Languages {
    private static final Languages languages = new Languages();
    private Map<String, String> mapLanguages;

    private Languages() {
    }

    public static Languages getInstance() {
        return languages;
    }

    public synchronized Map<String, String> getLanguages(TypesAPI typesAPI) {
        if (mapLanguages == null) {
            TranslatorFactory factory = new TranslatorFactory();
            Translator translator = factory.getTranslator(typesAPI); //TypesAPI.MICROSOFT
            mapLanguages = translator.getLanguages();
        }
        return mapLanguages;
    }

    public String getLangSuffix(String name) {
        String suffix = Constants.LANG_SUFFIX_DEFAULT;
        for (Map.Entry entry : mapLanguages.entrySet()) {
            if (entry.getValue().equals(name))
                suffix = entry.getKey().toString();
        }
        return suffix;
    }

}
