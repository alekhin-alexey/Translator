package service.files;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilePropertiesUtils {

    public static void writePropertiesFile(String fileName, Map<String, String> propVal) throws IOException {
        File configFile = new File(fileName);
        Properties props = new Properties();
        FileReader reader = new FileReader(configFile);
        props.load(reader);
        propVal.forEach(props::put);
        FileWriter writer = new FileWriter(configFile);
        props.store(writer, "User settings");
        writer.close();
    }

    public static Map<String, String> readPropertiesFile(String fileName) throws IOException {
        Map<String, String> propVal = new HashMap<>();
        Properties props = new Properties();
        props.load(new FileInputStream(new File(fileName)));
        props.forEach((key, value) -> propVal.put(key.toString(), value.toString()));
        return propVal;
    }

}
