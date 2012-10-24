package com.deepshiftlabs.nerrvana;

import org.w3c.dom.*;

/**
 * Encapsulates information about user
 * {@link <a href="https://nerrvana.com/docs/spaces">space in Nerrvana</a>}
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class SpaceData {
	public String id;
	public String name;
	public String ftp_path;

	public SpaceData() {
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFtppath() {
		return ftp_path;
	}

	/**
	 * Maps Nerrvana space data in the XML document into SpaceData object 
	 * @param doc XML document with space data nodes
	 * @return mapped object
	 * @throws Exception
	 */
	public static SpaceData parseSpaceData(Document doc) throws Exception {
		NodeList list = null;
		list = doc.getElementsByTagName("space");
		if (list == null || list.getLength() == 0)
			throw new Exception("Invalid space data XML");
		Node nodeSpace = list.item(0);
		list = nodeSpace.getChildNodes();
		SpaceData spd = new SpaceData();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getNodeName().equals("id")) {
				spd.id = Utils.nodeValue(n);
			} else if (n.getNodeName().equals("name")) {
				spd.name = Utils.nodeValue(n);
			} else if (n.getNodeName().equals("ftp_path")) {
				spd.ftp_path = Utils.nodeValue(n);
			}
		}
		if (spd.id == null || spd.ftp_path == null)
			throw new Exception("Invalid Space data");
		return spd;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t---BEGIN SPACE#").append(id).append("---\n")
				.append("\t\tName: ").append(name).append("\n")
				.append("\t\tPath: ").append(ftp_path).append("\n");
		sb.append("\t-----END SPACE#").append(id).append("---\n\n");
		return sb.toString();
	}
}