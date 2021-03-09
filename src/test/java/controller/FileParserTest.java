package controller;

import constants.Constants;
import model.FileModel;
import model.FileTask;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileParserTest {
    private final String keyTest = "key=\"new_find_demo.label.job_number\"";
    private final String valueTest = "— job number";
    private String fileSrc = "src/test/resources/xml/test_files/demo_messages.xml";

    @Test
    void getFileContent() throws IOException, SAXException, ParserConfigurationException {
        FileParser parser = getFileParser(fileSrc);
        Map<String, String> result = parser.getFileContent();

        Assert.assertTrue(result.containsValue(valueTest));
        Assert.assertTrue(result.containsKey(keyTest));

        result.forEach((key, value) -> System.out.println(key + " " + value));
    }

    @Test
    void setFileContent() throws ParserConfigurationException, TransformerException, SAXException, IOException {
        final String fileDst = "src/test/resources/xml/test_files/test_set_file_content.xml";
        final String expectedValue = "rewrite by new value";

        // delete file
        Path fileToDeletePath = Paths.get(fileDst);
        Files.delete(fileToDeletePath);

        FileParser parser = getFileParser(fileSrc);
        Map<String, String> content = new HashMap<>();
        content.put(keyTest, expectedValue);

        FileModel fileModel = new FileModel("demo_messages.xml", "en");
        FileTask fileTask = new FileTask(fileModel, content);

        parser.setFileContent(fileTask, fileDst);

        FileParser parser_dst = getFileParser(fileDst);
        Map<String, String> result = parser_dst.getFileContent();

        StringBuilder sb = new StringBuilder();
        result.forEach((key, value) -> {
            if (keyTest.equals(key)) {
                sb.append(value);
            }
        });
        String resultValue = sb.toString();

        assertEquals(expectedValue, resultValue);
        Assert.assertFalse(result.containsValue(valueTest));
        Assert.assertTrue(result.containsValue(expectedValue));

        result.forEach((key, value) -> System.out.println(key + " " + value));
    }

    @NotNull
    private FileParser getFileParser(final String fileSrc) {
        return new FileParser(
                fileSrc,
                Constants.XML_TAG_NAME_ROOT,
                Constants.XML_TAG_NAME_PROP,
                Constants.XML_TAG_NAME_PROP_ATTRIBUTE);
    }
}