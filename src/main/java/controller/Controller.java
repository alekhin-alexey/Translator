package controller;

import constants.Constants;
import model.FileModel;
import model.FileTask;
import model.LangTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import service.api.Translator;
import service.api.TranslatorFactory;
import service.api.TypesAPI;
import view.MainForm;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Map;

public class Controller {
    private boolean isErrorFind = false;
    private static final Logger logger = LoggerFactory.getLogger(MainForm.class);

    /**
     * ... Main steps ...
     * 1. get file list (.xml) and loop
     * 2. get content from current file
     * 3. translate content
     * 4. set content into result file
     */
    public void translateFiles(
            final String userLangFrom,
            final String userLangTo,
            JTextField textDir,
            Map<String, String> mapLanguages

    ) {

        LangTask langUser = new LangTask(
                Languages.getInstance().getLangSuffix(userLangFrom),
                Languages.getInstance().getLangSuffix(userLangTo));

        FileHandler fileHandler = new FileHandler(textDir.getText(), langUser, mapLanguages);

        // ... get file list (.xml) ...
        Map<String, FileModel> files = fileHandler.listFiles(Constants.XML_FILE_EXT);
        if (!(files == null)) {
            //pb.setMaximum(files.size());

            //setTextLog("Start translation files from " +
            //        userLangFrom + " to " +
            //        userLangTo + "\n");

            final int[] countFiles = {1};

            files.forEach((k, v) ->
                    {
                        //setTextLog("    ...file: " + v.getName() + "\n");
                        // ... current file ...
                        String fileSrc = fileHandler.getFileSrc(v.getName());
                        // ... result file ...
                        String fileDst = fileHandler.getFileDst(k, Constants.XML_FILE_EXT);


                        FileParser parser = new FileParser(
                                fileSrc,
                                Constants.XML_TAG_NAME_ROOT,
                                Constants.XML_TAG_NAME_PROP,
                                Constants.XML_TAG_NAME_PROP_ATTRIBUTE);

                        // ... get content from current file ...
                        FileTask data = null;
                        try {
                            data = new FileTask(v, parser.getFileContent());
                        } catch (ParserConfigurationException | SAXException | IOException e2) {
                            e2.printStackTrace();
                            logger.error("Error parse file (e2) !", e2);
                            //setTextLog("Error parsing! Check ");
                        }

                        // ... translate content ...
                        if (data != null) {
                            TranslatorFactory factory = new TranslatorFactory();
                            Translator translator = factory.getTranslator(TypesAPI.MICROSOFT);

                            LangTask lang = new LangTask(v.getLang(), langUser.getLangTo());

                            FileTranslator translated = new FileTranslator(translator, lang, data);

                            try {
                                data = translated.translateSentence(Constants.JSON_NODE_TEXT);
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                logger.error("Error parse file (e3) !", e3);
                                //setTextLog("Error parsing translated! Check ");
                            }

                            // ... set content into result file ...
                            try {
                                parser.setFileContent(data, fileDst);
                            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e4) {
                                logger.error("Error parse and save file (e4) !", e4);
                                e4.printStackTrace();
                            }
                            //setTextLog("    ✓");
                        }
                        //setProgress(countFiles[0]);
                        //countFiles[0]++;
                    }
            );
            //setTextLog("Total files translated: " + (countFiles[0] - 1) + "\n");
        } else {
            isErrorFind = true;
            //setTextLog("Selected directory is empty!\n");
            JOptionPane.showMessageDialog(
                    null,
                    "There are no files in the selected directory!",
                    Constants.SYS_INFORMATION_MESSAGE_TITLE,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

}
