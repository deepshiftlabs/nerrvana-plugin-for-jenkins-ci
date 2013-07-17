package com.deepshiftlabs.nerrvana;

import java.io.*;

import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.apache.http.HttpEntity; //import org.apache.http.util.EntityUtils;
import org.w3c.dom.*;

/**
 * Several kinds of utility functions: - XML parsers, helpers and cachers -
 * HttpClient helpers - some others
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class Utils {
	private static DocumentBuilderFactory _factory;
	private static Transformer _xformer;
	private static DocumentBuilder _builder;
	public static List<Document> parsedDocuments = new ArrayList<Document>();
	public static String lastXMLAsString;

	private static DocumentBuilder builder() throws Exception {
		if (_builder == null) {
			_factory = DocumentBuilderFactory.newInstance();
			_factory.setNamespaceAware(true);
			_xformer = TransformerFactory.newInstance().newTransformer();
			_builder = _factory.newDocumentBuilder();
		}
		return _builder;
	}

	private static Transformer xformer() throws Exception {
		builder();
		return _xformer;
	}

	public static void cacheDocument(Document doc) {
		parsedDocuments.add(doc);
		while (parsedDocuments.size() > 20)
			parsedDocuments.remove(0);
	}

	public static Document getEntityContentAsDocument(HttpEntity entity)
			throws Exception {
		InputStream in = entity.getContent();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int n = 0;
		byte[] ab = new byte[4096];
		while ((n = in.read(ab)) != -1) {
			out.write(ab, 0, n);
		}
		lastXMLAsString = out.toString("UTF-8");
		return bytes2xml(out.toByteArray());
	}

	public static Document string2xml(String s) throws Exception {
		return bytes2xml(s.getBytes("UTF-8"));
	}

	public static Document bytes2xml(byte[] ab) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(ab);
		Document doc = builder().parse(in);
		return doc;
	}

	public static Document file2xml(String filename) throws Exception {
		InputStream in = null;
		try {
			in = new FileInputStream(filename);
			Document doc = builder().parse(in);
			return doc;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	public static void xml2file(Document doc, File file) throws Exception {
		FileWriter sw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(sw);
		Source source = new DOMSource(doc);
		Result result = new StreamResult(pw);
		xformer().transform(source, result);
		pw.close();
	}

	public static String getTraceAsString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	public static String uniqueTagValue(String tagname, Document doc,
			boolean bMandatory) throws Exception {
		String result = null;
		NodeList list = doc.getElementsByTagName(tagname);
		if (list != null && list.getLength() > 0)
			result = nodeValue(list.item(0));
		else if (bMandatory)
			throw new Exception("Tag " + tagname + " not found in XML document");
		return result;
	}

	public static Node getChildNode(Node parent, String child_tagname) {
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node child = list.item(i);
			if (child.getNodeName().equalsIgnoreCase(child_tagname))
				return child;
		}
		return null;
	}

	public static String getUniqueChildNodeValue(Node parent,
			String child_tagname) throws Exception {
		Node n = getChildNode(parent, child_tagname);
		return nodeValue(n);
	}

	public static Document parseResponse(HttpEntity entity) throws Exception {
		Document doc = getEntityContentAsDocument(entity);
		cacheDocument(doc);
		if (doc.getDocumentElement().getNodeName().equals("errors"))
			throw new Exception(parseErrorResponseImpl(doc));
		else
			return doc;
	}

	public static String parseErrorResponse(HttpEntity entity) throws Exception {
		Document doc = getEntityContentAsDocument(entity);
		cacheDocument(doc);
		return parseErrorResponseImpl(doc);
	}

	private static String parseErrorResponseImpl(Document doc) throws Exception {
		NodeList list = doc.getElementsByTagName("description");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.getLength(); i++) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(Utils.nodeValue(list.item(i)));
		}
		return sb.toString();
	}

	public ByteArrayInputStream getResponseContent(HttpEntity entity)
			throws Exception {
		InputStream in = entity.getContent();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] ab = new byte[4096];
		int read = 0;
		while ((read = in.read(ab)) > 0) {
			bout.write(ab, 0, read);
		}
		ab = bout.toByteArray();
		return new ByteArrayInputStream(ab);
	}

	public static String xml2string(Document doc) throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		// ByteArrayOutputStream
		Source source = new DOMSource(doc);
		Result result = new StreamResult(pw);
		xformer().transform(source, result);
		return sw.toString();
	}

	public static String nodeValue(Node node) throws Exception {
		if (node == null)
			return null;
		String value = null;
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node child = nodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE
					|| child.getNodeType() == Node.CDATA_SECTION_NODE) {
				value = child.getNodeValue();
				if (value != null && value.trim().length() == 0)
					value = null;
				break;
			}
		}
		return value;
	}

	public static void setCDATAValue(Node node, String data) {
		CDATASection node_cdata = null;
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
				node_cdata = (CDATASection) list.item(i);
				break;
			}
		}
		if (data != null && data.trim().length() > 0) {
			if (node_cdata == null) {
				node_cdata = node.getOwnerDocument().createCDATASection(data);
				node.appendChild(node_cdata);
			} else
				node_cdata.setData(data);
		} else if (node_cdata != null)
			node.removeChild(node_cdata);
	}

	public static void setNodeTextValue(Node node, String text) {
		Text node_text = null;
		if (text == null)
			text = "";
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.TEXT_NODE) {
				node_text = (Text) list.item(i);
				break;
			}
		}
		if (text.trim().length() > 0) {
			if (node_text == null) {
				node_text = node.getOwnerDocument().createTextNode(text);
				node.appendChild(node_text);
			} else
				node_text.setData(text);
		} else if (node_text != null)
			node.removeChild(node_text);
	}

	public static void setChildNodeCDATAValue(Node parent, String child_name,
			String data) {
		Node child = Utils.getChildNode(parent, child_name);
		if (child == null)
			child = Utils.createChildElement(parent, child_name);
		Utils.setCDATAValue(child, data);
	}

	public static void setChildNodeTextValue(Node parent, String child_name,
			String data) {
		Node child = Utils.createChildElementIfNotExists(parent, child_name);
		if (child == null)
			child = Utils.createChildElement(parent, child_name);
		Utils.setNodeTextValue(child, data);
	}

	public static Node createChildElementIfNotExists(Node parent,
			String child_name) {
		Node child = Utils.getChildNode(parent, child_name);
		if (child == null)
			child = Utils.createChildElement(parent, child_name);
		return child;
	}

	public static Element createChildElement(Node parent, String tag) {
		Element child = parent.getOwnerDocument().createElement(tag);
		parent.appendChild(child);
		return child;
	}

	public static Element createChildElement(Node parent, String tag,
			String text) {
		Element child = parent.getOwnerDocument().createElement(tag);
		parent.appendChild(child);
		if (text != null)
			setNodeTextValue(child, text);
		return child;
	}

	public static void printSystemProperties(PrintStream out) {
		Vector<Object> unsorted = new Vector<Object>();
		Properties p = System.getProperties();
		for (Enumeration<Object> enm = p.keys(); enm.hasMoreElements();) {
			String key = enm.nextElement().toString();
			String val = p.getProperty(key);
			unsorted.addElement(key + " = " + val);
		}
		Vector<Object> sorted = new Vector<Object>();
		for (int i = 0; i < unsorted.size(); i++) {
			boolean bAdded = false;
			String s1 = unsorted.elementAt(i).toString();
			for (int j = 0; j < sorted.size(); j++) {
				String s2 = sorted.elementAt(j).toString();
				if (s2.compareTo(s1) >= 0) {
					sorted.insertElementAt(s1, j);
					bAdded = true;
					break;
				}
			}
			if (!bAdded)
				sorted.addElement(s1);
		}
		for (int i = 0; i < sorted.size(); i++) {
			out.println(sorted.elementAt(i).toString());
		}
	}
}