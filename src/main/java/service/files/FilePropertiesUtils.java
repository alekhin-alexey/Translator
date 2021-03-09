package service.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilePropertiesUtils {

    public static void writePropertiesFile(final String fileName, Map<String, String> propVal) throws IOException {
        File file = new File(fileName);
        Properties props = new Properties();
        propVal.forEach(props::put);
        FileWriter writer = new FileWriter(file);
        props.store(writer, "User settings");
        writer.close();
    }

    public static Map<String, String> readPropertiesFile(final String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            Map<String, String> propVal = new HashMap<>();
            Properties props = new Properties();
            props.load(new FileInputStream(new File(fileName)));
            props.forEach((key, value) -> propVal.put(key.toString(), value.toString()));
            return propVal;
        } else {
            return null;
        }
    }

}
