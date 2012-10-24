package com.deepshiftlabs.nerrvana;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * User messages returned by API have following structure: {@code 
 * <messages>
 *     <trace>
 *         <message>
 *             <txt><![CDATA[Selenium started.]]></txt>
 *             <created_time>1334253721</created_time>
 *         </message>
 *     </trace>
 *     <debug>
 *         <message>
 *             <txt><![CDATA[OK - word Selenium typed to input, snapshotted to 02_typed_entire.png]]></txt>
 *             <created_time>1334253722</created_time>
 *         </message>
 *     </debug>
 *     <info>
 *         <message>
 *             <txt><![CDATA[OK - clicked, snapshotted to 03_searched_entire.png]]></txt>
 *             <created_time>1334253753</created_time>
 *         </message>
 *     </info>
 * </messages>
 * }
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class UserMessage {
	public UserMessageLevel level;
	public String txt;
	public Date created_time;

	public UserMessage(Node node, UserMessageLevel level) throws Exception {
		txt = Utils.getUniqueChildNodeValue(node, "txt");
		String s = Utils.getUniqueChildNodeValue(node, "created_time");
		try {
			created_time = new java.util.Date(Long.parseLong(s) * 1000);
		} catch (Exception e) {
			Logger.infoln("Invalid 'message.created_time' value: " + s);
		}
		this.level = level;
	}

	public UserMessage(String txt, Date created_time) throws Exception {
		this.txt = txt;
		this.created_time = created_time;
	}

	public String getTxt() {
		return txt;
	}

	public String getCreatedTime() {
		return created_time == null ? "<unknown time>" : created_time
				.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("USER-MESSAGE[");
		if (created_time == null)
			sb.append("<unknown time>\n");
		else
			sb.append("<").append(created_time.toString()).append(">\n");
		sb.append(txt.trim());
		sb.append("\n]END\n");
		return sb.toString();
	}

	/**
	 * Collects all messages in the document and converts them into map of the following form:
	 * [key - UserMessageLevel] => [value - list of the UserMessage objects of the given level]
	 *  
	 * @param doc execution results document
	 * @return map of messages sorted by level
	 * @throws Exception
	 */
	public static SortedMap<UserMessageLevel, List<UserMessage>> getAllUserMessages(
			Document doc) throws Exception {
		SortedMap<UserMessageLevel, List<UserMessage>> messageMap = new TreeMap<UserMessageLevel, List<UserMessage>>();
		for (UserMessageLevel level : UserMessageLevel.values()) {
			List<UserMessage> listOfMessages = null;
			// get list of all message containers of the certain level - e.g.
			// all <error> nodes, all <fatal> nodes, all <info> nodes, etc.
			NodeList listOfMessageContainers = doc.getElementsByTagName(level
					.name().toLowerCase());
			if (listOfMessageContainers != null
					&& listOfMessageContainers.getLength() > 0) {
				for (int i = 0; i < listOfMessageContainers.getLength(); i++) {
					// get single node (<error, <info>, etc)
					Node messageContainer = listOfMessageContainers.item(i);
					if (messageContainer.getChildNodes() != null
							&& messageContainer.getChildNodes().getLength() > 0) {
						// get list of actual messages in this node
						NodeList listOfChildNodes = messageContainer
								.getChildNodes();
						for (int j = 0; j < listOfChildNodes.getLength(); j++) {
							Node message = listOfChildNodes.item(j);
							if (message.getNodeName().equalsIgnoreCase(
									"message")) {
								if (listOfMessages == null)
									listOfMessages = new ArrayList<UserMessage>();
								listOfMessages.add(new UserMessage(message,
										level));
							}
						}
					}
				}
			}
			if (listOfMessages != null)
				messageMap.put(level, listOfMessages);
		}
		return messageMap;
	}

	/**
	 * Collects all messages for the given platform and converts them into map of the following form:
	 * [key - UserMessageLevel] => [value - list of the UserMessage objects of the given level]
	 * @param nodePlatform parent node of the messages
	 * @return sorted map of messages for this platform 
	 * @throws Exception
	 */
	public static SortedMap<UserMessageLevel, List<UserMessage>> getPlatformUserMessages(
			Node nodePlatform) throws Exception {
		SortedMap<UserMessageLevel, List<UserMessage>> messageMap = new TreeMap<UserMessageLevel, List<UserMessage>>();
		Node messageContainer = Utils.getChildNode(nodePlatform, "messages");
		if (messageContainer != null && messageContainer.hasChildNodes()) {
			for (UserMessageLevel level : UserMessageLevel.values()) {
				List<UserMessage> listOfMessages = null;
				String levelName = level.name().toLowerCase();
				NodeList listOfMessageByTypeContainers = messageContainer
						.getChildNodes();
				for (int i = 0; i < listOfMessageByTypeContainers.getLength(); i++) {
					Node messageByTypeContainer = listOfMessageByTypeContainers
							.item(i);
					if (messageByTypeContainer.getNodeName().equalsIgnoreCase(
							levelName)) {
						NodeList listOfChildNodes = messageByTypeContainer
								.getChildNodes();
						for (int j = 0; j < listOfChildNodes.getLength(); j++) {
							Node message = listOfChildNodes.item(j);
							if (message.getNodeName().equalsIgnoreCase(
									"message")) {
								if (listOfMessages == null)
									listOfMessages = new ArrayList<UserMessage>();
								listOfMessages.add(new UserMessage(message,
										level));
							}
						}
					}
				}
				if (listOfMessages != null)
					messageMap.put(level, listOfMessages);
			}
		}
		return messageMap;
	}
}
