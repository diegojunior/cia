package br.com.totvs.cia.importacao.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	public static void main(final String[] args)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		InputStream file = Main.class.getResourceAsStream("teste.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		document.getDocumentElement().normalize();

		System.out.println("Root Name: " + document.getDocumentElement().getNodeName());

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expression = xpath.compile("/Document/BizFileHdr/Xchg/BizGrp/Document/SttlmRpt/RptParams");
		NodeList nodes = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
		System.out.println(nodes.getLength());
		String campo = "Frqcy/xpto/RptNb/@name";
		String campo2 = "Frqcy/xpto/ActvtyInd";

		/*
		 * String campo = "RptNb"; String campo2 = "ActvtyInd";
		 */

		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			System.out.println(nodeToString(node));
			XPathFactory xpathFactory1 = XPathFactory.newInstance();
			XPath xpath1 = xpathFactory1.newXPath();
			XPathExpression expression1 = xpath1.compile(campo);
			NodeList nodes1 = (NodeList) expression1.evaluate(node, XPathConstants.NODESET);

			System.out.println(printNode(nodes1, campo));
		}

	}

	private static String printNode(final NodeList nodes, final String item) {
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			Node parentNode = node.getParentNode();
			String extrairUltimoElemento = extrairUltimoElemento(item);
			if (extrairUltimoElemento.equals(node.getNodeName())) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.hasChildNodes()) {
						return printNode(node.getChildNodes(), item);
					}
				}
				if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					if (node.hasChildNodes()) {
						return printNode(node.getChildNodes(), item);
					}
				}
			} else if (extrairUltimoElemento.equals(parentNode.getNodeName())) {
				if (node.getNodeType() == Node.TEXT_NODE) {
					String value = node.getNodeValue().trim();
					if (!value.isEmpty()) {
						return value;
					}
				}
			} else if (node.hasChildNodes()) {
				return printNode(node.getChildNodes(), item);
			}
		}
		return null;
	}

	private static String extrairUltimoElemento(final String item) {

		List<String> elementos = Arrays.asList(item.split("/"));

		return elementos.get(elementos.size() - 1).replaceAll("@", "");
	}

	private static String nodeToString(final Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}

}
