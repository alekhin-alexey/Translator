package controller;

import constants.Constants;
import model.FileModel;
import model.LangTask;
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
     * languages for translate "from" and "to"
     */
    private LangTask lang;

    /**
     * map of languages
     */
    private Map<String, String> mapLanguages;

    public FileHandler(String path, LangTask lang, Map<String, String> mapLanguages) {
        this.path = path;
        this.lang = lang;
        this.mapLanguages = mapLanguages;
    }

    /**
     * Get a list of files needed for translation
     *
     * @param fileExt - file extension (ex: ".xml")
     * @return listFiles - ist of files for further processing
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

                if (mapLanguages.containsKey(fileLangSuffix)) {
                    if (lang.getLangFrom().equals(mapLanguages.get(fileLangSuffix))) {
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
     *
     * @param fileName - original file name
     * @return - Full path to the file, including the name
     */
    public String getFileSrc(final String fileName) {
        return path + "/" + fileName;
    }

    /**
     * Translated data file
     *
     * @param fileName - the name of the original file to be translated
     * @param fileExt  - file extension (ex: ".xml")
     * @return - Full path to the file, including the name
     */
    public String getFileDst(final String fileName, final String fileExt) {
        StringBuilder fileTo = new StringBuilder(fileName);
        fileTo.insert(fileTo.length() - fileExt.length(), "_" + lang.getLangTo());

        return path + "/" + fileTo.toString();
    }

}
