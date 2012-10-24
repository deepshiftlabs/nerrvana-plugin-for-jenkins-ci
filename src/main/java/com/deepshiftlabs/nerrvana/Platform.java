package com.deepshiftlabs.nerrvana;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Parses and wraps information about platform where tests are executed in
 * Nerrvana.
 * 
 * @see HttpCommunicator#getExecutionStatus(NerrvanaExecution)</p>
 * @see NerrvanaExecution</p>
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class Platform {
	public String id;
	public String code;
	public String name;
	public String status;
	public String browse_url;
	public SortedMap<UserMessageLevel, List<UserMessage>> sortedMessages;
	public List<UserMessage> messages;

	public Platform() {
	}

	public Platform(String id, String code, String name, String status,
			String browse_url) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.status = status;
		this.browse_url = browse_url;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns status label for this platform. There are three possible results:
	 * <ol>
	 * <li><b>NERRVANA FAILURE</b> - Nerrvana process failed to execute Selenium tests (don't mess with tests failure)</li>
	 * <li><b>TESTS FAILURE</b> - Nerrvana process successfully completed, but Selenium tests sent messages of type ERROR or FATAL</li>
	 * <li><b>SUCCESS</b> - Nerrvana process successfully completed and tests didn't send ERROR or FATAL messages</li>
	 * </ol>
	 * 
	 * @return text representation of the execution status for this platform instance
	 */
	public String getStatus() {
		if (this.status != null && this.status.equalsIgnoreCase("ok")) {
			for (Iterator<UserMessage> uit = messages.iterator(); uit.hasNext();) {
				UserMessage m = uit.next();
				if (m.level.value() > UserMessageLevel.WARN.value())
					return "<span class=\"failure\">TESTS FAILURE</span>";
			}
			return "<span class=\"failure\">SUCCESS</span>";
		} else
			return "<span class=\"success\">NERRVANA FAILURE</span>";
	}

	/**
	 * Returns URL to Nerrvana where user (provided credentials) may review this platform execution artifacts
	 * @return URL to Nerrvana or label "Not available"
	 */
	public String getBrowseurl() {
		return (browse_url != null && browse_url.length() > 0) ? browse_url
				: "Not available";
	}

	/**
	 * Returns content for the onclick javascript handler
	 * 
	 * @return javascript code snippet
	 */
	public String getBrowseurlMarkup() {
		StringBuilder sb = new StringBuilder();
		if (browse_url != null && browse_url.length() > 0) {
			sb.append("var w = window.open('").append(browse_url)
					.append("','_blank','');w.focus();");
		} else
			sb.append("return false;");
		return sb.toString();
	}

	public String getBrowsetitle() {
		return (browse_url == null || browse_url.indexOf("://") < 0) ? "Not available"
				: "-&gt; at Nerrvana &lt;-";
	}

	public SortedMap<UserMessageLevel, List<UserMessage>> getSortedMessages() {
		return sortedMessages;
	}

	public List<UserMessage> getMessages() {
		return messages;
	}

	public int getMessageCount() {
		return messages == null ? 0 : messages.size();
	}

	/**
	 * Returns message type names with message count by type
	 * Example: "ERROR(4), WARN(2), TRACE(1)"
	 * 
	 * @return merged message type names 
	 */
	public String getMessagesLabel() {
		StringBuilder sb = new StringBuilder();
		if (sortedMessages != null) {
			for (UserMessageLevel level : UserMessageLevel.values()) {
				List<UserMessage> list = sortedMessages.get(level);
				if (list != null && list.size() > 0) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(level.name().toUpperCase()).append("(")
							.append(list.size()).append(")");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Returns Platform list which reside under the given XML node 
	 * @param parent parent node in XML document
	 * @return list of Platform objects
	 * @throws Exception
	 */
	public static ArrayList<Platform> getPlatformList(Node parent)
			throws Exception {
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getNodeName().equals("platform"))
				platforms.add(Platform.parsePlatform(n));
		}
		return platforms;
	}

	/**
	 * Performs XML to object mapping
	 * @param parent 
	 * @return Platform object mapped from XML
	 * @throws Exception
	 */
	private static Platform parsePlatform(Node parent) throws Exception {
		NodeList list = parent.getChildNodes();
		Platform platform = new Platform();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			String name = n.getNodeName();
			String value = Utils.nodeValue(n);
			if (name.equals("id"))
				platform.id = value;
			else if (name.equals("code"))
				platform.code = value;
			else if (name.equals("name"))
				platform.name = value;
			else if (name.equals("status"))
				platform.status = value;
			else if (name.equals("browse_url"))
				platform.browse_url = value;
			else if (name.equals("messages")) {
				platform.sortedMessages = UserMessage
						.getPlatformUserMessages(parent);
				platform.messages = Platform
						.sortedToPlainlist(platform.sortedMessages);
			}
		}
		return platform;
	}

	/**
	 * Converts map of messages sorted by message type into plain list of messages
	 * 
	 * @param sortedMessages Sorted map of messages
	 * @return plain list of messages
	 */
	private static List<UserMessage> sortedToPlainlist(
			SortedMap<UserMessageLevel, List<UserMessage>> sortedMessages) {
		List<UserMessage> plainlist = new ArrayList<UserMessage>();
		if (sortedMessages != null) {
			for (UserMessageLevel level : UserMessageLevel.values()) {
				List<UserMessage> list = sortedMessages.get(level);
				if (list != null && list.size() > 0) {
					for (Iterator<UserMessage> iterator = list.iterator(); iterator
							.hasNext();)
						plainlist.add(iterator.next());
				}
			}
		}
		return plainlist;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sid = id == null ? "[N/A]" : id.toString();
		sb.append("\t---BEGIN PLATFORM#").append(sid).append("---\n")
				.append("\t\tContext: ").append(name).append("\n")
				.append("\t\tStatus: ").append(status).append("\n");
		if (browse_url != null && browse_url.length() > 0)
			sb.append("\t\tBrowse: ").append(browse_url).append("\n");
		sb.append("\t-----END PLATFORM#").append(sid).append("---\n\n");
		return sb.toString();
	}
}