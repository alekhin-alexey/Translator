package controller;

import constants.Constants;
import model.Lang;
import model.FileModel;
import org.junit.jupiter.api.Test;
import service.api.Languages;
import service.files.FileUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class FileHandlerTest {

    @Test
    void getListTreatedFiles() throws IOException {
        String path = "C:\\Users\\Alexey\\Documents\\_application\\";
        //String path = "C:\\Users\\Alexey\\Documents\\_empty\\";

        Languages mapLanguages = Languages.getInstance();
        Map<String, String> mapLang = mapLanguages.getLanguages();
        Lang lang = new Lang("French", "German", mapLang);

        Set<String> files = FileUtils.getListFiles(path, Constants.XML_FILE_EXT);

        if (files.isEmpty()) {
            System.out.println("empty dir");
            //return null;
        } else {

            Map<String, FileModel> listFiles = new HashMap<>();
            Map<String, FileModel> listLangFiles = new HashMap<>();

            for (String file : files) {

                String fileLangSuffix = file.substring(file.lastIndexOf("_") + 1)
                        .replace(Constants.XML_FILE_EXT, "");

                if (lang.getLanguages().containsKey(fileLangSuffix)) {
                    if (lang.getLangFrom().equals(lang.getLanguages().get(fileLangSuffix))) {
                        String fileOriginal = file.substring(0, file.lastIndexOf("_"))
                                .concat(Constants.XML_FILE_EXT);

                        listLangFiles.put(fileOriginal, new FileModel(file, fileLangSuffix));
                    }
                } else {
                    listFiles.put(file, new FileModel(file, Constants.LANG_SUFFIX_DEFAULT));
                }
            }

            //listLangFiles.forEach((k, v)-> System.out.println("originalFileName: " + k +";   " + v));
            //System.out.println("\n");
            listLangFiles.forEach(listFiles::put);
            listFiles.forEach((k, v)-> System.out.println("originalFileName: " + k +";   " + v));
        }
    }
}