package controller;

import model.FileTask;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * use DOM Parser
 */
public class FileParser {
    private final String fileSrc;
    /**
     * root tag element   "<properties>"
     */
    private final String rootTag;
    /**
     * need tag element   "<entry>"
     */
    private final String findTag;
    /**
     * tag attribute name "<entry key=>"
     */
    private final String findTagAttr;

    public FileParser(final String fileSrc, final String rootTag, final String findTag, final String findTagAttr) {
        this.fileSrc = fileSrc;
        this.rootTag = rootTag;
        this.findTag = findTag;
        this.findTagAttr = findTagAttr;
    }

    /**
     * Get data from file to map - "fileContent" as key params = tag value
     *
     * @return - "fileContent" - file data
     * @throws ParserConfigurationException handled in MainForm
     * @throws SAXException                 handled in MainForm
     * @throws IOException                  handled in MainForm
     */
    public Map<String, String> getFileContent() throws ParserConfigurationException, SAXException, IOException {
        Map<String, String> fileContent = new HashMap<>();

        Document doc = getDocument();
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

        return fileContent;
    }

    /**
     * Parse xml file, find tags, find tag attributes, set tag text value
     *
     * @param data    Translated data
     *                key - key = "params..." (value of tag "<entry> file.xlm")
     *                value - Translated text
     * @param fileDst - файл для сохранения данных
     * @throws ParserConfigurationException handled in MainForm
     * @throws SAXException                 handled in MainForm
     * @throws IOException                  handled in MainForm
     * @throws TransformerException         handled in MainForm
     */
    public void setFileContent(final FileTask data,
                               final String fileDst)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Map<String, String> fileContent = data.getContent();

        Document doc = getDocument();
        Node properties = doc.getElementsByTagName(rootTag).item(0);
        NodeList list = properties.getChildNodes();    // loop the properties child node

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (findTag.equals(node.getNodeName())) {
                if ((node.getTextContent() != null && !node.getTextContent().isEmpty())) {
                    String key = node.getAttributes().getNamedItem(findTagAttr).toString();
                    if (fileContent.containsKey(key)) {
                        node.setTextContent(fileContent.get(key));
                    }
                }
            }
        }

        // write the content into xml file - fileDst
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileDst));
        assert transformer != null;
        transformer.transform(source, result);
    }

    private Document getDocument()
            throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileSrc);
    }

}
