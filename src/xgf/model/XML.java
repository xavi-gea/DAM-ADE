package xgf.model;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Xavi
 */
public class XML {
	
	private static Path workPath = Paths.get("." + File.separator + "xml");

	/**
	 * Create and XML File in the workPath and return it's content
	 * @param fileName Name of the XML File
	 * @param lineValues Values that it will have
	 * @param tags Tags that will contain the values
	 * @return Content of the XML
	 * @throws IOException When the directory with the XML cannot be created
	 * @throws ParserConfigurationException When the XML file cannot be instantiated
	 * @throws TransformerException When the XML generated cannot be transformed into a File or String
	 */
	public static String createAndReturnFile(String fileName, String[] lineValues, String[] tags) throws IOException, ParserConfigurationException, TransformerException {
		
		if (Files.notExists(workPath)) {

			Files.createDirectory(workPath);
		}
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		Document xmlDocument = dBuilder.newDocument();
		
		Element xmlRoot = xmlDocument.createElement(tags[0]);
		
		xmlRoot.setAttribute("name", fileName);
		
		Element xmlTag;
		
		for (int i = 1; i < tags.length; i++) {
			
			xmlTag = xmlDocument.createElement(tags[i]);
			xmlTag.appendChild(xmlDocument.createTextNode(lineValues[i]));
			
			xmlRoot.appendChild(xmlTag);
		}
		
		xmlDocument.appendChild(xmlRoot);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				
		File outputFile = new File(workPath.toString() + File.separator + fileName + ".xml");
		
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(outputFile));
		
		StringWriter outputString = new StringWriter();
		
		transformer.transform(new DOMSource(xmlDocument), new StreamResult(outputString));
		
		return outputString.toString();
	}

	/**
	 * Return every XML file found in the previously specified workPath
	 * @return
	 */
	public static List<File> getCreatedFiles() {
		
		List<File> foundFiles = new ArrayList<File>();
		
		for (File element : workPath.toFile().listFiles()) {
			
			if (element.isFile()) {
				
				if (element.canRead()) {
					
					foundFiles.add(element);
				}
			}
		}
		
		return foundFiles;
	}

	/**
	 * From the provided XML File, return it's content
	 * @param file File to get it's content
	 * @return Content found inside the File parameter
	 * @throws ParserConfigurationException
	 * @throws SAXException When the contents of the file cannot be parsed
	 * @throws IOException When the file cannot be read
	 */
	public static List<String> getAttributesAndValues(File file) throws ParserConfigurationException, SAXException, IOException {
		
		List<String> fileContent = new ArrayList<String>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		Document xmlDocument = dBuilder.parse(file);
		
		Element xmlRoot = xmlDocument.getDocumentElement();
		
		fileContent.add(xmlRoot.getAttribute("name"));
		
		NodeList nodeList = xmlRoot.getChildNodes();
		
		Node node;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			node = nodeList.item(i);
			
			if (node instanceof Element) {
				
				fileContent.add(node.getTextContent());
			}
		}
		
		return fileContent;
	}
}
