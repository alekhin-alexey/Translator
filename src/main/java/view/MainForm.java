package view;

import constants.Constants;
import controller.FileHandler;
import controller.Parser;
import controller.Translator;
import model.FileData;
import model.FileModel;
import model.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import service.api.Languages;
import service.files.FilePropertiesUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainForm extends JFrame {
    private JTextField textDir;
    private JComboBox<String> cbLangFrom;
    private JComboBox<String> cbLangTo;
    private JTextArea textLog;
    private Map<String, String> mapLang;
    private JProgressBar pb;
    private JButton btTranslate;
    private JButton btCancel;

    private String userLangFrom = Constants.LANG_NAME_DEFAULT;
    private String userLangTo = Constants.LANG_NAME_DEFAULT;

    private boolean isErrorFind = false;

    private SwingWorker<Void, String> swingWorker;

    private static final Logger logger = LoggerFactory.getLogger(MainForm.class);

    public MainForm() {
        super("Translator");
        getLangFromApi();
        initUI();
        readProperties();
    }

    private void initUI() {
        JPanel pnFixTop = new JPanel(new GridLayout(3, 1));

        JPanel pnDir = new JPanel();
        JPanel pnLang = new JPanel();
        JPanel pnAction = new JPanel();

        pnFixTop.add(pnDir);
        pnFixTop.add(pnLang);
        pnFixTop.add(pnAction);
        getContentPane().add(pnFixTop);

        JPanel pnLog = new JPanel(new BorderLayout());
        getContentPane().add(pnLog, BorderLayout.CENTER);

        componentLogging(pnLog);
        componentAction(pnAction);
        componentLang(pnLang);
        componentDir(pnDir);

        formSettings();
    }

    private void setTextLog(String text) {
        try {
            textLog.getDocument().insertString(0, text, null);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
            logger.error("Error set text to TextArea (e1) !", e1);
        }
    }

    /**
     * ... Main steps ...
     * 1. get file list (.xml) and loop
     * 2. get content from current file
     * 3. translate content
     * 4. set content into result file
     */
    private void translateFiles() {
        btTranslate.setEnabled(false);
        btCancel.setEnabled(true);
        isErrorFind = false;

        //textLog.selectAll();
        //textLog.replaceSelection("");

        swingWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                Lang langUser = new Lang(userLangFrom, userLangTo, mapLang);
                FileHandler fileTreat = new FileHandler(textDir.getText(), langUser);
                // ... get file list (.xml) ...
                Map<String, FileModel> files = fileTreat.listFiles(Constants.XML_FILE_EXT);
                if (!(files == null)) {
                    pb.setMaximum(files.size());

                    setTextLog("Start translation files from " +
                            langUser.getLangFrom() + " to " +
                            langUser.getLangTo() + "\n");

                    final int[] countFiles = {1};

                    files.forEach((k, v) ->
                            {
                                setTextLog("    ...file: " + v.getFileName() + "\n");
                                // ... current file ...
                                String fileSrc = fileTreat.getFileSrc(v.getFileName());
                                // ... result file ...
                                String fileDst = fileTreat.getFileDst(k, Constants.XML_FILE_EXT);

                                Parser parser = new Parser(
                                        fileSrc,
                                        Constants.XML_TAG_NAME_ROOT,
                                        Constants.XML_TAG_NAME_PROP,
                                        Constants.XML_TAG_NAME_PROP_ATTRIBUTE);

                                // ... get content from current file ...
                                FileData data = null;
                                try {
                                    data = new FileData(v, parser.getFileContent());
                                } catch (ParserConfigurationException | SAXException | IOException e2) {
                                    e2.printStackTrace();
                                    logger.error("Error parse file (e2) !", e2);
                                    setTextLog("Error parsing! Check ");
                                }

                                // ... translate content ...
                                if (data != null) {
                                    Translator translated = new Translator(langUser, data);

                                    try {
                                        data = translated.translateSentence();
                                    } catch (IOException e3) {
                                        e3.printStackTrace();
                                        logger.error("Error translation file (e3) !", e3);
                                    }

                                    // ... set content into result file ...
                                    try {
                                        parser.setFileContent(data, fileDst);
                                    } catch (ParserConfigurationException | SAXException | IOException | TransformerException e4) {
                                        logger.error("Error parse and save file (e4) !", e4);
                                        e4.printStackTrace();
                                    }
                                    setTextLog("    ✓");
                                }
                                setProgress(countFiles[0]);
                                countFiles[0]++;
                            }
                    );
                    setTextLog("Total files translated: " + (countFiles[0] - 1) + "\n");
                } else {
                    isErrorFind = true;
                    setTextLog("Selected directory is empty!\n");
                    JOptionPane.showMessageDialog(
                            null,
                            "There are no files in the selected directory!",
                            Constants.SYS_INFORMATION_MESSAGE_TITLE,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                return null;
            }

            @Override
            protected void done() {
                writeProperties();
                pb.setValue(0);
                btTranslate.setEnabled(true);
                btCancel.setEnabled(false);

                if (isCancelled()) {
                    setTextLog("Action is canceled by user!\n");
                } else {
                    if (!isErrorFind) {
                        setTextLog("Done!\n");
                        JOptionPane.showMessageDialog(
                                null,
                                "Files translated successfully!",
                                Constants.SYS_INFORMATION_MESSAGE_TITLE,
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        };

        swingWorker.execute();

        swingWorker.addPropertyChangeListener(propertyChangeEvent -> {
            if ("progress".equals(propertyChangeEvent.getPropertyName())) {
                pb.setValue((Integer) propertyChangeEvent.getNewValue());
            }
        });
    }

    private void cancel() {
        btTranslate.setEnabled(true);
        btCancel.setEnabled(false);
        swingWorker.cancel(true);
    }

    private void componentLogging(JPanel pnLog) {
        textLog = new JTextArea(2, 1);
        textLog.setColumns(60);
        textLog.setEditable(false);

        textLog.setFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.ITALIC, 10));
        textLog.setForeground(Color.GRAY);
        textLog.setBackground(pnLog.getBackground());

        TitledBorder title = BorderFactory
                .createTitledBorder(BorderFactory
                        .createEtchedBorder(EtchedBorder.LOWERED), " Current process:");
        title.setTitleFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.ITALIC, 10));
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        textLog.setBorder(title);

        JScrollPane scroll = new JScrollPane(textLog,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnLog.add(scroll);
    }

    private void componentAction(JPanel pnAction) {
        pb = new JProgressBar(0, 100);
        pb.setValue(0);
        pb.setBorder(BorderFactory.createEmptyBorder());
        pb.setFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.ITALIC, 10));
        pb.setStringPainted(true);
        pb.setPreferredSize(new Dimension(230, 33));
        pnAction.add(pb);

        btTranslate = new JButton("Translate");
        btTranslate.setPreferredSize(new Dimension(100, 33));
        pnAction.add(btTranslate);
        btTranslate.addActionListener(e -> {

            if (textDir.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Select directory!",
                        Constants.SYS_INFORMATION_MESSAGE_TITLE,
                        JOptionPane.INFORMATION_MESSAGE
                );
                selectDirectory(textDir);
            }

            if (userLangFrom.equals(userLangTo)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Language selection error! The translated language are the same as the original!",
                        Constants.SYS_INFORMATION_MESSAGE_TITLE,
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                translateFiles();
            }

        });

        btCancel = new JButton("  Cancel  ");
        btCancel.setPreferredSize(new Dimension(100, 33));
        btCancel.setEnabled(false);
        btCancel.addActionListener(e -> cancel());
        pnAction.add(btCancel);
    }

    private void componentLang(JPanel pnLang) {

        TitledBorder title = BorderFactory
                .createTitledBorder(BorderFactory
                        .createEtchedBorder(EtchedBorder.LOWERED), " Select languages:");
        title.setTitleFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.ITALIC, 10));
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        Border margin = new EmptyBorder(0, 20, 0, 20);
        pnLang.setBorder(title);
        Border border = pnLang.getBorder();
        pnLang.setBorder(new CompoundBorder(border, margin));


        JLabel laLanguageFrom = new JLabel("        from ");
        laLanguageFrom.setFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.BOLD, 11));
        pnLang.add(laLanguageFrom);

        cbLangFrom = setLanguages();
        cbLangFrom.setPreferredSize(new Dimension(150, 25));
        cbLangFrom.addActionListener(
                actionEvent -> userLangFrom = Objects.requireNonNull(cbLangFrom.getSelectedItem()).toString()
        );
        pnLang.add(cbLangFrom);

        JLabel laLanguageTo = new JLabel("   to   ");
        laLanguageTo.setFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.BOLD, 11));
        pnLang.add(laLanguageTo);
        cbLangTo = setLanguages();
        cbLangTo.setPreferredSize(new Dimension(150, 25));
        cbLangTo.addActionListener(
                actionEvent -> userLangTo = Objects.requireNonNull(cbLangTo.getSelectedItem()).toString()
        );
        pnLang.add(cbLangTo);

        if (!(mapLang == null) && !(mapLang.isEmpty())) {
            setTextLog("Getting list of languages from API is successfully!\n");
        } else {
            logger.error("Error getting list of languages from API! Check your internet connection!");
            setTextLog("Error getting list of languages from API! Check your internet connection!\n");
        }

    }

    private void componentDir(JPanel pnDir) {
        JButton btOpenDir = new JButton("Open...");
        btOpenDir.setFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.BOLD, 11));
        btOpenDir.setPreferredSize(new Dimension(100, 33));
        btOpenDir.setAlignmentX(CENTER_ALIGNMENT);
        btOpenDir.addActionListener(e -> selectDirectory(textDir));
        btOpenDir.setToolTipText("Select directory");
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass()
                    .getClassLoader()
                    .getResource("Open_16x16.png")));
            btOpenDir.setIcon(new ImageIcon(img));
        } catch (IOException ignored) {
        }
        pnDir.add(btOpenDir);

        textDir = new JTextField(32);
        textDir.setEditable(false);

        TitledBorder title = BorderFactory
                .createTitledBorder(BorderFactory
                        .createEtchedBorder(EtchedBorder.LOWERED), "Directory path:");
        title.setTitleFont(new Font(Constants.SYS_FONT_TITLE_UI, Font.ITALIC, 10));
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        textDir.setBorder(title);

        pnDir.add(textDir);
    }

    private void writeProperties() {
        try {
            Map<String, String> propVal = new HashMap<>();
            propVal.put(Constants.PROP_DIR, textDir.getText());
            propVal.put(Constants.PROP_LANG_FROM, userLangFrom);
            propVal.put(Constants.PROP_LANG_TO, userLangTo);
            FilePropertiesUtils.writePropertiesFile(Constants.PROP_FILE_NAME, propVal);
        } catch (IOException e5) {
            e5.printStackTrace();
            logger.error("Error write properties file (e5) !");
        }
    }

    private void readProperties() {
        try {
            Map<String, String> props = FilePropertiesUtils.readPropertiesFile(Constants.PROP_FILE_NAME);
            String textDirProp = props.get(Constants.PROP_DIR);
            if (!textDirProp.isEmpty()) textDir.setText(textDirProp);
            if (!(mapLang == null) && !(mapLang.isEmpty())) {
                String langFromProp = props.get(Constants.PROP_LANG_FROM);
                if (!langFromProp.isEmpty()) cbLangFrom.setSelectedItem(langFromProp);

                String langFromTo = props.get(Constants.PROP_LANG_TO);
                if (!langFromTo.isEmpty()) cbLangTo.setSelectedItem(langFromTo);
            }
        } catch (Exception e6) {
            e6.printStackTrace();
            logger.error("Error read properties file (e6) !");
        }
    }

    private void formSettings() {
        setLayout(new GridLayout(2, 1));
        setSize(370, 160);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        btTranslate.requestFocus();
    }

    private void selectDirectory(JTextField textDir) {
        JFileChooser dirOpen = new JFileChooser();
        dirOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirOpen.setAcceptAllFileFilterUsed(false);
        int ret = dirOpen.showDialog(null, "Select directory");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = dirOpen.getSelectedFile();
            textDir.setText(file.getPath());
        }
    }

    private void getLangFromApi() {
        try {
            Languages mapLanguages = Languages.getInstance();
            mapLang = mapLanguages.getLanguages();
        } catch (IOException e7) {
            e7.printStackTrace();
            logger.error("Error get languages from API (e7) !");
        }

    }

    private JComboBox<String> setLanguages() {
        DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<>();
        JComboBox<String> cbLanguage = new JComboBox<>(cbModel);
        if (!(mapLang == null) && !(mapLang.isEmpty())) {
            SortedSet<String> sortedValues = new TreeSet<>(mapLang.values());
            for (String elem : sortedValues) {
                cbModel.addElement(elem);
            }
        } else {
            cbModel.addElement(Constants.LANG_NAME_DEFAULT);
        }
        cbModel.setSelectedItem(Constants.LANG_NAME_DEFAULT);
        return cbLanguage;
    }
}
