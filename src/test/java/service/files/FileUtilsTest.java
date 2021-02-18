package service.files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    @Test
    void getListFiles() {

        String dir = "C:\\Users\\Alexey\\Documents\\_application\\";
        Set<String> filesXml = Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(file -> file.toString().endsWith(".xml"))
                .map(File::getName)
                .collect(Collectors.toSet());

        filesXml.forEach(System.out::println);
        System.out.println(filesXml.toString());
        assertEquals(filesXml.toString(), "[demo_messages_new_fr.xml, demo_messages_new.xml, demo_messages.xml, demo_messages_fr.xml]");
    }

/*
    // ... !!! because small files ...
    String content = null;
    try {
        content = new String(Files.readAllBytes(Paths.get(fileDst)));
    } catch(IOException e) {
        e.printStackTrace();
    }

    // ... replace translated ...
    for(Map.Entry<String, String> pair :data.getContent().entrySet()) {
        String tag = "</" + Constants.XML_TAG_NAME_PROP + ">";
        content = content
                .replace(
                        ">" + pair.getKey().replace("\\n", "") + tag,
                        ">" + pair.getValue()
                                .replaceAll("([\\[\\]])", "")
                                .replace("\\n", "") + tag);

        //content = content.replaceAll(">" + pair.getKey().replace("\\n","") + tag, ">" + pair.getValue().replaceAll("([\\[\\]\\n])", "")  + tag);
    }
    // ... write data ...
    try {
        FileUtils.writeFile(fileDst, content);
    } catch(IOException e) {
        e.printStackTrace();
    }
*/
}