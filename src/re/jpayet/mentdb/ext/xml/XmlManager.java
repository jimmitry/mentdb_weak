/**
 * Project: MentDB
 * License: GPL v_3
 * Description: Mentalese Database Engine
 * Website: https://www.mentdb.org
 * Twitter: https://twitter.com/mentalese_db
 * Facebook: https://www.facebook.com/mentdb
 * Author: Jimmitry Payet
 * Mail: contact@mentdb.org
 * Locality: Reunion Island (French)
 */

package re.jpayet.mentdb.ext.xml;

import java.util.Map.Entry;
import java.io.StringWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;

public class XmlManager {

	public static String escape_10(String str) throws Exception {
		
		//Initialization
		return StringEscapeUtils.escapeXml10(str);
		
	}

	public static String escape_11(String str) throws Exception {
		
		//Initialization
		return StringEscapeUtils.escapeXml11(str);
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {
		
		//Initialization
		JSONArray loaded = new JSONArray();
		
		//Get all keys
		for(String key : env.xmlObj.keySet()) {
			
			loaded.add(key);
			
		}
		
		return loaded;
		
	}

	public static String doc(EnvManager env, String docId) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Try to get the document
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			return nodeToString(doc);

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String load(EnvManager env, String docId, String xmlStrDoc) throws Exception {

		//Generate an error if docId is null or empty
		if (docId==null || docId.equals("")) {

			throw new Exception("Sorry, the document id cannot be null or empty.");

		}

		//Generate an error if XML document is null or empty
		if (xmlStrDoc==null || xmlStrDoc.equals("")) {

			throw new Exception("Sorry, the XML document cannot be null or empty.");

		}

		//Try to load the document
		try {

			//Load the document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new java.io.ByteArrayInputStream(xmlStrDoc.getBytes()));

			//Save the document
			env.xmlObj.put(docId, doc);

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

		return "1";

	}

	public static String exist(EnvManager env, String docId) throws Exception {

		if (env.xmlObj.containsKey(docId)) {

			return "1";

		} else {
			
			return "0";
			
		}

	}

	public static String count(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to count
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} else {

				return ""+nodeList.getLength();

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String select_node(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to select
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return null;

			} if (nodeList.getLength()==0) {

				return null;

			} else {

				Node node = nodeList.item(0);
				return nodeToString(node);

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String select_text(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist (xml.select_text).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty (xml.select_text).");

		}

		//Try to select
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return null;

			} if (nodeList.getLength()==0) {

				return null;

			} else {

				Node node = nodeList.item(0);
				return nodeToTextString(node);

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String select_attribute(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to select
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();
			
			String val = (String) (xpath.compile(xPath).evaluate(doc, XPathConstants.STRING));

			return val;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray xpath(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to get xpath
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return null;

			} if (nodeList.getLength()==0) {

				return null;

			} else {
				
				String result = "";
				for(int i=0;i<nodeList.getLength();i++) {

					Node node = nodeList.item(i);
					
					if (node.getNodeType()==Node.ELEMENT_NODE) {
						result += "\n" + getXPathAttribute(node, getXPath(node), "["+indexOfNode(node)+"]");
					}

					result += getXPathChilds(node);

				}

				if (!result.equals("")) result = result.substring(1);

				JSONArray r = new JSONArray();
				
				String[] rtab = result.split("\n", -1);
				for(int i=0;i<rtab.length;i++) {
					
					r.add(rtab[i]);
					
				}
				
				return r;

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray fields(EnvManager env, String docId, String xPath) throws Exception {
		
		JSONArray result = new JSONArray();

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to get field names
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList!=null && nodeList.getLength()!=0) {
				
				for(int i=0;i<nodeList.getLength();i++) {

					Node node = nodeList.item(i);
					
					NodeList child = node.getChildNodes();
					
					for(int j=0;j<child.getLength();j++) {

						Node n = child.item(j);
						
						String name = n.getNodeName();

						if (!name.equals("#text")) {
							result.add(n.getNodeName());
						}

					}

				}

			}
			
			return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	private static String getXPathChilds(Node node) {

		//Initialization
		String result = "";
		
		try {

			NodeList nodeList = node.getChildNodes();
			for(int i=0;i<nodeList.getLength();i++) {
	
				Node nodeTmp = nodeList.item(i);
				
				try {
					if (nodeTmp.getNodeType()==Node.ELEMENT_NODE) {
						result += "\n" + getXPathAttribute(nodeTmp, getXPath(nodeTmp), "["+indexOfNode(nodeTmp)+"]");
					}
				} catch (Exception e) {};
	
				result += getXPathChilds(nodeTmp);
	
			}
			
		} catch (Exception e) {};

		return result;

	}

	private static String getXPath(Node node) {

		//Initialization
		String result = "", nodeName = node.getNodeName();

		if (AtomFx.size(nodeName, ":").equals("2")) {

			nodeName = AtomFx.get(nodeName, "2", ":");

		}

		if (node.getParentNode()==null || node.getParentNode().getNodeType()==Node.DOCUMENT_NODE) {

			result = "/"+nodeName;

		} else {

			result = getXPath(node.getParentNode()) + "/"+nodeName;

		}

		return result;

	}

	private static String getXPathAttribute(Node node, String result, String position) {

		//Initialization
		String strTmp = "";

		strTmp = result;
		if (!position.equals("[1]")) result = result+position;

		//All attributes
		String tmpAttributes = "";
		try {
			for(int i=0;i<node.getAttributes().getLength();i++) {

				try {
					tmpAttributes += " and @"+node.getAttributes().item(i).getNodeName()+"='"+node.getAttributes().item(i).getNodeValue().replace("'", "\\'")+"'";
				} catch (Exception f) {};

			}
		} catch (Exception e) {};

		if (!tmpAttributes.equals("")) {

			tmpAttributes = tmpAttributes.substring(5);
			result += "\n"+strTmp+"["+tmpAttributes+"]";

		}

		return result;

	}

	//Get the node position
	private static int indexOfNode(Node node) {

		//Initialization
		int index;
		Node sibling, firstNode = node;

		//Count
		index = 1;
		while ((sibling = node.getPreviousSibling()) != null) {
			node = sibling;
			if (node.getNodeType()==Node.ELEMENT_NODE && firstNode.getNodeName().equals(node.getNodeName())) ++index;
		}

		return index;

	}

	public static String insert_text(EnvManager env, String docId, String xPath, String nodeName, String value) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the node name is null or empty
		if (nodeName==null || nodeName.equals("")) {

			throw new Exception("Sorry, the node name cannot be null or empty.");

		}

		//Try to insert
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {

				Node node = nodeList.item(0);
				Node newNode = doc.createElement(nodeName);
				newNode.appendChild(doc.createTextNode(value));
				node.appendChild(newNode);

				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String insert_nodes(EnvManager env, String docId, String xPath, String xml) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to insert
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document docToAdd = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes()));
				
				Node node = nodeList.item(0);
				NodeList nodeListXml = ((NodeList) xpath.compile("/"+docToAdd.getDocumentElement().getNodeName()).evaluate(docToAdd, XPathConstants.NODESET));
				for (int i = 0; i < nodeListXml.getLength(); i++) {
					Node n = nodeListXml.item(i);
					Node firstDocImportedNode = doc.importNode(n, true);
					node.appendChild(firstDocImportedNode);				
				}
				
				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String update_text(EnvManager env, String docId, String xPath, String value) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to update the value
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {

				Node node = nodeList.item(0);
				node.setTextContent(value);

				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String insert_attribute(EnvManager env, String docId, String xPath, String attr, String value) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to set the attribute
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {

				Node node = nodeList.item(0);
				NamedNodeMap attributes = node.getAttributes();
				Node attNode = node.getOwnerDocument().createAttribute(attr);
				attNode.setNodeValue(value);
				attributes.setNamedItem(attNode);

				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String delete_attribute(EnvManager env, String docId, String xPath, String attr) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to delete the attribute
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {

				Node node = nodeList.item(0);
				NamedNodeMap attributes = node.getAttributes();
				attributes.removeNamedItem(attr);

				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	public static String delete_node(EnvManager env, String docId, String xPath) throws Exception {

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Try to delete the node
		try {

			Document doc = ((Document) env.xmlObj.get(docId));

			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = ((NodeList) xpath.compile(xPath).evaluate(doc, XPathConstants.NODESET));

			if (nodeList==null) {

				return "0";

			} if (nodeList.getLength()==0) {

				return "0";

			} else {

				Node node = nodeList.item(0);
				node.getParentNode().removeChild(node);

				return "1";

			}

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ... "+e.getMessage()+".");

		}

	}

	//Convert a node to string representation
	public static String nodeToString(Node node) throws Exception {

		//Initialization
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(node);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;

	}

	//Convert a node to string representation
	private static String nodeToTextString(Node node) throws TransformerFactoryConfigurationError, TransformerException {

		//Initialization
		StringWriter sw = new StringWriter();

		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "text");
		t.transform(new DOMSource(node), new StreamResult(sw));

		return sw.toString();

	}

	public static String unload(EnvManager env, String docId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the document id does not exist
		if (!env.xmlObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Close the document
		try {

			env.xmlObj.remove(docId);

		} catch (Exception e) {

			throw new Exception("Sorry, cannot close the document.");

		}

		return result;

	}

	public static String unloadall(EnvManager env) throws Exception {

		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to close
		for (Entry<String, Document> e : env.xmlObj.entrySet()) {

			allKeysToDelete.add(e.getKey());

		}

		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {

			try {

				//Close the document
				unload(env, allKeysToDelete.get(i));
				nbClosed++;

			} catch (Exception e) {

				//Nothing to do

			}

		}

		return ""+nbClosed;

	}

}