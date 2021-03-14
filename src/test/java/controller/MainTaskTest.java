package controller;

import org.junit.jupiter.api.Test;
import service.api.TypesAPI;

import javax.swing.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTaskTest {

    private final Map<String, String> mapLanguages = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);

    @Test
    void translateFiles() {
        String userLangTo = "Deutsch";
        String userLangFrom = "French";
        String filePath = "src/test/resources/xml/test_files/";

        MainTask task = new MainTask(filePath, userLangFrom, userLangTo, mapLanguages, null, null);
        task.translateFiles();

    }
}