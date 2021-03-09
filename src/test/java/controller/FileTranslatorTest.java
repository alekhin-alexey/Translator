package controller;

import constants.Constants;
import model.FileModel;
import model.FileTask;
import model.LangTask;
import org.junit.jupiter.api.Test;
import service.api.MicrosoftTranslator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTranslatorTest {

    @Test
    void translateSentence() throws IOException {
        final String langFrom = "en";
        final String langTo = "ru";
        final String keyTest = "key=\"new_find_demo.label.job_number\"";
        final String valueTest = "— job number";
        final String expectedValue = "- номер работы";

        LangTask langTask = new LangTask(langFrom, langTo);
        MicrosoftTranslator msTranslator = new MicrosoftTranslator();

        Map<String, String> content = new HashMap<>();
        content.put(keyTest, valueTest);

        FileModel fileModel = new FileModel("demo_messages.xml", "en");
        FileTask fileTask = new FileTask(fileModel, content);

        FileTranslator translator = new FileTranslator(msTranslator, langTask, fileTask);
        FileTask translated = translator.translateSentence(Constants.JSON_NODE_TEXT);

        Map<String, String> result = translated.getContent();

        StringBuilder sb = new StringBuilder();
        result.forEach((key, value) -> {
            if (keyTest.equals(key)) {
                sb.append(value);
            }
        });
        String resultValue = sb.toString();

        assertEquals(expectedValue, resultValue);

        System.out.println(expectedValue);
        System.out.println(resultValue);

    }
}