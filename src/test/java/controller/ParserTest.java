package controller;

import constants.Constants;
import controller.Translator;
import model.FileData;
import model.FileModel;
import model.Lang;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import service.api.Languages;
import service.api.Translate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ParserTest {
    private String fileSrc = "C:\\Users\\Alexey\\Documents\\_application\\demo_messages.xml";
    private String fileDst = "C:\\Users\\Alexey\\Documents\\_application\\demo_messages_fr.xml";
    private String rootTag = Constants.XML_TAG_NAME_ROOT;
    private String findTag = Constants.XML_TAG_NAME_PROP;
    private String findTagAttr = Constants.XML_TAG_NAME_PROP_ATTRIBUTE;
    private HashMap<String, String> fileContent = new HashMap<>();

    @Test
    void getFileContent() throws ParserConfigurationException, IOException, SAXException {

        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileSrc);

        Node properties = doc.getElementsByTagName(rootTag).item(0);

        NodeList list = properties.getChildNodes();    // loop the properties child node
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (findTag.equals(node.getNodeName())) {
                if ((node.getTextContent() != null && !node.getTextContent().isEmpty())) {

                    fileContent.put(
                            node.getAttributes().getNamedItem(findTagAttr).toString(),
                            node.getTextContent()
                    );

                }
            }
        }
        fileContent.forEach((k, v) -> System.out.println(k + "; text: " + v));
    }

    @Test
    void setFileContent() throws ParserConfigurationException, IOException, SAXException {
        Languages mapLanguages = Languages.getInstance();
        Map<String, String> mapLang = mapLanguages.getLanguages();
        Lang langUser = new Lang("English", "French", mapLang);

        FileModel model = new FileModel("demo_messages.xml", "en");

        FileData fileData = new FileData(model, fileContent);


        for (Map.Entry<String, String> item : fileData.getContent().entrySet()) {
            try {
                Translate translateData = new Translate(item.getValue(), model.getLangContent(), langUser);
                Map<String, String> result = translateData.translate();
                if (!result.values().isEmpty()) {
                    fileData.getContent().put(item.getKey(), result.values().toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(fileContent);

        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileSrc);

        FileData data = null;
        Node properties = doc.getElementsByTagName(rootTag).item(0);

        NodeList list = properties.getChildNodes();    // loop the properties child node
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (findTag.equals(node.getNodeName())) {
                if ((node.getTextContent() != null && !node.getTextContent().isEmpty())) {

                    fileContent.put(
                            node.getAttributes().getNamedItem(findTagAttr).toString(),
                            node.getTextContent()
                    );

                }
            }
        }
        fileContent.forEach((k, v) -> System.out.println(k + "; text: " + v));

        Translator translated = new Translator(langUser, data);
        data = translated.translateSentence();

        HashMap<String, String> fileContent = data.getContent();
        Node properties1 = doc.getElementsByTagName(rootTag).item(0);

        NodeList list1 = properties1.getChildNodes();    // loop the properties child node
        for (int i = 0; i < list1.getLength(); i++) {
            Node node = list1.item(i);
            if (findTag.equals(node.getNodeName())) {
                if ((node.getTextContent() != null && !node.getTextContent().isEmpty())) {
                    String key = node.getAttributes().getNamedItem(findTagAttr).toString();
                    if (fileContent.containsKey(key)) {
                        node.setTextContent(fileContent.get(key));
                    }
                }
            }
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileDst));
        try {
            assert transformer != null;
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}