package service.api;

import constants.Constants;
import controller.Languages;
import model.LangTask;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import service.json.Mapper;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MicrosoftTranslatorTest {

    @Test
    void getLanguages() {
        final String nameExpected = "English";
        final String suffixExpected = "de";

        Map<String, String> result = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);

        Assert.assertTrue(result.containsValue(nameExpected));
        Assert.assertTrue(result.containsKey(suffixExpected));
        Assert.assertFalse(result.containsKey("false"));

        System.out.println(result);
    }

    @Test
    void translate() throws IOException {
        final String langFrom = "en";
        final String langTo = "ru";
        final String original = "Press Ctrl + F to use quick search.";

        final String wrap_start = "[{\"translations\":[{\"text\":\"";
        final String wrap_end = "\",\"to\":\"" + langTo + "\"}]}]";
        final String translated = "Нажмите Ctrl и F, чтобы использовать быстрый поиск.";
        final String expected = wrap_start.concat(translated).concat(wrap_end);

        String parse_expected = Mapper.parseValue(Constants.JSON_NODE_TEXT, expected);

        LangTask langTask = new LangTask(langFrom, langTo);
        MicrosoftTranslator translator = new MicrosoftTranslator();
        String result = translator.translate(langTask, original);

        assertEquals(expected, result); // json
        assertEquals(parse_expected, translated); // value

        System.out.println(expected);
        System.out.println(result);
        System.out.println(parse_expected);
    }
}