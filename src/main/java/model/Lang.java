package model;

import constants.Constants;

import java.util.Map;

public class Lang {
    private final String langFrom;
    private final String langTo;
    private final Map<String, String> languages;

    public Lang(String langFrom, String langTo, Map<String, String> mapLang) {
        this.langFrom = langFrom;
        this.langTo = langTo;
        this.languages = mapLang;
    }

    private String getLangSuffix(String langName) {
        String langKey = Constants.LANG_SUFFIX_DEFAULT;
        for (Map.Entry entry : languages.entrySet()) {
            if (entry.getValue().equals(langName))
                langKey = entry.getKey().toString();
        }
        return langKey;
    }

    public String getLangSuffixTo() {
        return getLangSuffix(langTo);
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public String getLangFrom() {
        return langFrom;
    }

    public String getLangTo() {
        return langTo;
    }
}
