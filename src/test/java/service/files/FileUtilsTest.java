package service.files;

import constants.Constants;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    // get list of xml files from user selected path
    @Test
    void ListFiles() {
        String dirResources = "src/test/resources/xml/test_files/";
        final String fileNameResult = "demo_messages.xml";
        Set<String> filesXml = FileUtils.getListFiles(dirResources, Constants.XML_FILE_EXT);

        Assert.assertTrue(filesXml.contains(fileNameResult));

        filesXml.forEach(System.out::println);
    }

    // if path empty
    @Test
    void listFilesEmpty() {
        String dirResources = "src/test/resources/xml/empty_dir/";
        Set<String> filesXml = FileUtils.getListFiles(dirResources, Constants.XML_FILE_EXT);

        final String result1 = "[]";
        assertEquals(filesXml.toString(), result1);
        final String result2 = "demo_messages.xml";
        Assert.assertFalse(filesXml.contains(result2));

        System.out.println(filesXml);

    }
}