package controller;

import constants.Constants;
import model.FileData;
import model.FileModel;
import model.Lang;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import service.api.Languages;
import service.api.Translate;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TranslatorTest {

    @Test
    void translateSentence() throws IOException, ParserConfigurationException, SAXException {
        // ... translate by each sentence ...

        String fileName = "C:\\Users\\Alexey\\Documents\\_application\\demo_messages.xml";

        Map<String, String> dictionary = new HashMap<>();

        Languages mapLanguages = Languages.getInstance();
        Map<String, String> mapLang = mapLanguages.getLanguages();
        Lang userLang = new Lang("English", "French", mapLang);

        FileModel model = new FileModel("demo_messages.xml", "en");

        Parser parser = new Parser(
                fileName,
                Constants.XML_TAG_NAME_ROOT,
                Constants.XML_TAG_NAME_PROP,
                Constants.XML_TAG_NAME_PROP_ATTRIBUTE);
        FileData fileData = new FileData(model, parser.getFileContent());

        for (Map.Entry<String, String> item : fileData.getContent().entrySet()) {
            try {
                //System.out.println(item.getValue());
                //System.out.println(userLang.getLangSuffixFrom());
                //System.out.println(userLang.getLangSuffixTo());
                Translate translateData = new Translate(item.getValue(), model.getLangContent(), userLang);
                Map<String, String> result = translateData.translate();
                if (!result.values().isEmpty()) {
                    //System.out.println(result);
                    //System.out.println(result.get(item.getValue()));
                    dictionary.put(item.getValue(), result.get(item.getValue()));
                    fileData.getContent().put(item.getKey(), result.get(item.getValue()));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(fileData);
        //System.out.println(dictionary);

        dictionary.forEach((k, v)-> System.out.println("original: " + k +"; translated: " + v));
    }
}