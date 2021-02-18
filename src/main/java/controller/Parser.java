package controller;

import model.FileData;
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

public class Parser {
    private final String fileSrc;
    private final String rootTag;     // root tag element   <properties>
    private final String findTag;     // need tag element   <entry>
    private final String findTagAttr; // tag attribute name <entry key=>

    public Parser(String fileSrc, String rootTag, String findTag, String findTagAttr) {
        this.fileSrc = fileSrc;
        this.rootTag = rootTag;
        this.findTag = findTag;
        this.findTagAttr = findTagAttr;
    }

    private Document getDocument()
            throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileSrc);
    }

    /**
     * Get data from file to map - "fileContent" as:
     * key   - tag attribute "findTag" bu name "findTagAttrName"
     * value - text value of tag "findTag"
     * <p>
     * Can be implemented in several ways:
     * 1. Class Properties
     * Properties prop = new Properties();
     * prop.loadFromXML(new FileInputStream(fileName));
     * prop.forEach((key, value) -> fileContent.put(key.toString(), value.toString()));
     * <p>
     * - The file must be of a certain structure - Properties ! Otherwise loading error !
     * + No tag and attribute fields needed
     * <p>
     * 2. Use DOM Parser
     * fileContent = ParserDOM.ParseFile(fileName, rootTag, findTag, findTagAttr);
     * <p>
     * - We need tag and attribute fields to navigate the structure
     * + Data loading stability. The file inside can contain different tags, text, tag nesting levels
     * <p>
     * 3. Use SAX Parser
     * SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
     * XMLSAXParser handler = new XMLSAXParser(rootElement, findElement);
     * saxParser.parse(fileName, handler);
     * <p>
     * While the file size is small, there is no need to use a SAX parser
     * <p>
     * Conclusion: Let's use the DOM parser as more stable
     *
     * @return map - "fileContent" - file data
     * Example:
     * { key="new_find_demo.label.job_number"=— numéro d’emploi,
     * key="new_find_demo.label.account_name"=— nom du compte
     * }
     */
    public HashMap<String, String> getFileContent() throws ParserConfigurationException, SAXException, IOException {
        Document doc = getDocument();
        HashMap<String, String> fileContent = new HashMap<>();
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
    public void setFileContent(final FileData data,
                               final String fileDst) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = getDocument();
        HashMap<String, String> fileContent = data.getContent();
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

}
