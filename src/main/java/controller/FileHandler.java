package controller;

import constants.Constants;
import model.FileModel;
import model.Lang;
import service.files.FileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileHandler {
    /**
     * The path to the directory with files
     */
    private String path;
    /**
     * languages for translate
     */
    private Lang lang;

    public FileHandler(String path, Lang lang) {
        this.path = path;
        this.lang = lang;
    }

    /**
     * Get a list of files needed for translation
     * <p>
     * 1. Get the names of all files ("files") in the selected directory ("path") with extension ("fileExt")
     * <p>
     * 2. Analyzing the list of received files ("files"):
     * there are two kinds of files:
     * - original (without localization suffixes - target language)
     * - translated (with a localization suffix of the form: "_fr", "_de", "_es", etc.)
     * looking for the translation language suffix ("fileLangSuffix") by the last found character "_" in the file name
     * Example:
     * "example_file_name_fr.xml" - the localization suffix "fr" is defined, i.e. file content language "French"
     * "example.xml", "example_file_name.xml" - no localization suffix defined - these are original files!
     * if the suffix is not found, then the default language of the file content is English (English)
     * select files
     * with suffixes - map "listLangFiles" (target language "langContent" (From - source language))
     * without suffixes - map "listFiles" (translation language "langContent" - by default (English))
     * merge map "listLangFiles" with map "listFiles", because original files can be added by users!
     * <p>
     * FileModel
     * - fileName - current file name
     * - langContent - data language in the current file
     *
     * @param fileExt - file extension (ex: ".xml")
     * @return listFiles - ist of files for further processing
     * Example:
     * originalFileName: demo.xml;                ModelFile{fileName='demo.xml', langContent='en'}
     * originalFileName: demo_messages_new.xml;   ModelFile{fileName='demo_messages_new_fr.xml', langContent='fr'}
     * originalFileName: demo_messages.xml;       ModelFile{fileName='demo_messages_fr.xml', langContent='fr'}
     */
    public Map<String, FileModel> listFiles(final String fileExt) {

        Set<String> files = FileUtils.getListFiles(path, fileExt);

        if (files.isEmpty()) {
            return null;
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
            listLangFiles.forEach(listFiles::put);
            return listFiles;
        }
    }

    /**
     * Initial data file
     * <p>
     * Example:
     * "example_file_name.xml" - file with data in English (by default without the suffix at the end of the file name)
     * "example_file_name_fr.xml" - file with data of French language
     *
     * @param fileName - original file name
     * @return - Full path to the file, including the name
     */
    public String getFileSrc(final String fileName) {
        return path + "\\" + fileName;
    }

    /**
     * Translated data file
     * <p>
     * Example:
     * French source file name "example_file_name_fr.xml"
     * If translate the contents of the file into German
     * The resulting file will be "example_file_name_de.xml"
     *
     * @param fileName - the name of the original file to be translated
     * @param fileExt  - file extension (ex: ".xml")
     * @return - Full path to the file, including the name
     */
    public String getFileDst(final String fileName, final String fileExt) {
        StringBuilder fileTo = new StringBuilder(fileName);
        fileTo.insert(fileTo.length() - fileExt.length(), "_" + lang.getLangSuffixTo());
        return path + "\\" + fileTo.toString();
    }

}
