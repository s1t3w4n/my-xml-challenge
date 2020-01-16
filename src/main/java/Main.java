import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        final String filePath = "groups.xml";
        htmlDecoder decoder = new htmlDecoder();

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);

            Node root = document.getDocumentElement();

            NodeList groups = root.getChildNodes();

            int count = 0;

            for (int i = 0; i < groups.getLength(); i++) {
                Node group = groups.item(i);
                if (group.getNodeName().equalsIgnoreCase("GROUP")) {
                    NodeList settings = group.getChildNodes();
                    for (int j = 0; j < settings.getLength(); j++) {
                        Node setting = settings.item(j);
                        if (setting.getNodeName().equalsIgnoreCase("NAME")) {
                            String name = setting.getTextContent();
                            setting.setTextContent(decoder.code(name));
                            System.out.println(++count + ": " + decoder.code(name));
                        }
                    }
                }
            }
            writeDocument(document);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("normalGroups.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }

}
