package controller;

import constants.Constants;
import model.FileModel;
import model.LangTask;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import service.api.TypesAPI;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHandlerTest {
    private final String path = "src/test/resources/xml/test_files";
    private final String fileName = "demo_messages.xml";
    private Map<String, String> mapLanguages = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);
    private final String langFrom = "en";
    private final String langTo = "ru";

    @Test
    void listFiles() {
        final String expected = fileName + "=ModelFile{name='" + fileName + "', lang='" + langFrom + "'}";
        LangTask langTask = new LangTask(langFrom, langTo);
        FileHandler fileHandler = new FileHandler(path, langTask, mapLanguages);
        Map<String, FileModel> files = fileHandler.listFiles(Constants.XML_FILE_EXT);
        final String result = files.toString();

        //Assert.assertTrue(result.contains(fileName));
        Assert.assertTrue(result.contains(expected));

        System.out.println(result);
    }

    @Test
    void getFileSrc() {
        final String expected = path + "/demo_messages.xml";
        LangTask langTask = new LangTask(langFrom, langTo);
        FileHandler fileHandler = new FileHandler(path, langTask, mapLanguages);
        final String result = fileHandler.getFileSrc(fileName);

        assertEquals(expected, result);

        System.out.println(result);
    }

    @Test
    void getFileDst() {
        final String expected = path + "/demo_messages_" + langTo + ".xml";
        LangTask langTask = new LangTask(langFrom, langTo);
        FileHandler fileHandler = new FileHandler(path, langTask, mapLanguages);
        final String result = fileHandler.getFileDst(fileName, Constants.XML_FILE_EXT);

        assertEquals(expected, result);

        System.out.println(result);
    }
}