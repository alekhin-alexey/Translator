package service.api;

import model.LangTask;

import java.util.Map;

public interface Translator {
    /**
     * Get languages from Translation API
     *
     * @return map [language suffix = language name] (ex: [en=English, ru=Russian, ...])
     */
    Map<String, String> getLanguages();

    /**
     * Get translated text from Translation API
     *
     * @param langTask - languages "from" and "to" (ex: from - "en", to - "ru")
     * @param text     - original text
     * @return - translated text
     */
    String translate(final LangTask langTask, final String text);
}
