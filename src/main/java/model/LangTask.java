package model;

public class LangTask {
    /**
     * name or suffix of language from
     */
    private String langFrom;
    /**
     * name or suffix of language to translate
     */
    private String langTo;

    public LangTask(String langFrom, String langTo) {
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public String getLangFrom() {
        return langFrom;
    }

    public String getLangTo() {
        return langTo;
    }
}
