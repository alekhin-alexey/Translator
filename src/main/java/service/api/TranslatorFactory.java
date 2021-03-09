package service.api;

public class TranslatorFactory {
    public Translator getTranslator(TypesAPI type) {
        Translator translator = null;
        switch (type) {
            case MICROSOFT:
                translator = new MicrosoftTranslator();
                break;
            case GOOGLE:
                // translator = new GoogleTranslator();
                break;
            case YANDEX:
                // translator = new YandexTranslator();
                break;
        }
        return translator;
    }
}
