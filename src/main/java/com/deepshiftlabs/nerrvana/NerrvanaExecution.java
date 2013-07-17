package com.deepshiftlabs.nerrvana;

import hudson.model.AbstractBuild;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.w3c.dom.*;

/**
 * <p>
 * Parses and wraps information about tests execution in Nerrvana.
 * 
 * @see HttpCommunicator#getExecutionStatus(NerrvanaExecution)</p>
 * 
 *      This class expects to get following response from Nerrvana API:
 * 
 *      <pre>
 * {@code 
 * <?xml version="1.0" encoding="utf-8"?>
 * <executions>
 *      <execution>
 *          <id>4547</id>
 *          <testrun_id>520</testrun_id>
 *          <start_datetime>1342533469</start_datetime>
 *          <status><![CDATA[ok]]></status>
 *          <platforms>
 *              <platform>
 *                  <id>9234</id>
 *                  <code>winxp_sp3_firefox_110</code>
 *                  <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>
 *                  <browse_url>https://xxx.ftps.nerrvana.com/demo_space/_test_runs/Testrun-Name/2022_01_22_22_22_22/winxp_sp3_firefox_110/</browse_url>
 *                  <status><![CDATA[ok]]></status>
 *              </platform>
 *          </platforms>
 *      </execution>
 * </executions>
 * }
 * </pre>
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 * @version 1.01 added {@link NerrvanaExecution#testExecutionResults(AbstractBuild, NerrvanaPluginSettings)}
 * 
 */
public class NerrvanaExecution {
	public String id;
	public String starttime;
	public String status;
	public String testrun_id;
	public String testrun_name;
	public String testrun_description;
	public ArrayList<Platform> platforms;

	public NerrvanaExecution() {
	}

	public String getId() {
		return id;
	}

	public String getTestrunName() {
		return this.testrun_name;
	}

	public String getTestrunDescription() {
		return this.testrun_description;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getStatus() {
		return status;
	}

	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}

	/**
	 * Tests if execution of selenium tests in Nerrvana was successful.
	 * Execution is successful if
	 * <ol>
	 * <li>
	 * All platforms completed execution with status = ok 
	 * (which means that Nerrvana processes successfuly completed, but doesn't mean
	 * that tests execution was successful);
	 * </li>
	 * <li>
	 * All messages dispatched from Nerrvana have the lower level then message
	 * threshold defined in plugin settings (default level is: &lt;message-threshold&gt;ERROR&lt;/message-threshold&gt;).
	 * Also see {@link UserMessageLevel} for message levels
	 * </li> 
	 * 
	 * @param build
	 * @param settings
	 * @return
	 */
	public static boolean testExecutionResults(AbstractBuild<?, ?> build,
			NerrvanaPluginSettings settings) {
		try {
			// Check if results.xml file exists
			File buildDir = build.getRootDir();
			File executionResultsFile = new File(buildDir,
					settings.executionResultsFilename);
			if (!(executionResultsFile.exists() && executionResultsFile
					.isFile())) {
				Logger.infoln("Test execution results file ['"
						+ settings.executionResultsFilename
						+ "'] not found - there is nothing to evaluate");
				return true;
			} else
				Logger.infoln("---BEGIN TEST EXECUTION RESULTS---");

			// Evaluate results, step 1: test that all platforms completed work
			// with status = OK
			boolean result = true;
			Document doc = Utils.file2xml(executionResultsFile
					.getAbsolutePath());
			NodeList platforms = doc.getElementsByTagName("platform");
			for (int i = 0; i < platforms.getLength(); i++) {
				org.w3c.dom.Node platform = platforms.item(i);
				String status = Utils.getUniqueChildNodeValue(platform,
						"status");
				if (status == null || !status.equalsIgnoreCase("completed")) {
					String platformName = Utils.getUniqueChildNodeValue(
							platform, "name");
					result = false;
					Logger.infoln("Nerrvana execution on platform '"
							+ platformName + "' failed with status '" + status
							+ "'.");
					break;
				}
			}

			// Evaluate results, step 2: test for errors not exceeding threshold
			if (result) {
				// get all messages grouped by message level
				SortedMap<UserMessageLevel, List<UserMessage>> messageMap = UserMessage
						.getAllUserMessages(doc);
				for (Iterator<Entry<UserMessageLevel, List<UserMessage>>> i = messageMap
						.entrySet().iterator(); i.hasNext();) {
					Map.Entry<UserMessageLevel, List<UserMessage>> m = i.next();
					UserMessageLevel key = m.getKey();
					if (key.value() >= settings.messageThreshold.value()) {
						List<UserMessage> list = m.getValue();
						Logger.infoln("At least "
								+ list.size()
								+ " message(s) from Nerrvana side reach(es) or exceed(s) threshold level ("
								+ settings.messageThreshold.name()
								+ ").\nMessage analyzer marks execution as failure.");
						result = false;
						break;
					}
				}
			}
			if (result)
				Logger.infoln("All Nerrvana executions successfully completed.");
			Logger.infoln("-----END TEST EXECUTION RESULTS---");
			return result;
		} catch (Exception e) {
			Logger.exception(e);
			return false;
		}
	}

	/**
	 * Adds message about fatal error to the last &lt;execution&gt; node returned by Nerrvana
	 * or creates placeholder containing &lt;execution&gt; node when no NerrvanaExecution object 
	 * has been returned by Nerrvana, e.g on connection failure. 
	 * 
	 * @param lastExecutionResult last XML returned by Nerrvana server
	 * @param e exception caused fatal error
	 * @param settings settings object. Actually we need names of platforms for error report 
	 * @return parsed XML document with &lt;execution&gt; node containing fatal error text
	 * @throws Exception
	 */
	public static Document fatalResult(Document lastExecutionResult,
			Exception e, NerrvanaPluginSettings settings) throws Exception {
		if (lastExecutionResult == null) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<Platform> iterator = settings.platforms.iterator(); iterator
					.hasNext();) {
				if (sb.length() > 0)
					sb.append(", ");
				Platform p = iterator.next();
				sb.append(p.name);
			}
			String platforms = sb.toString();
			sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<executions>" + "     <execution>"
					+ "         <id>N/A</id>"
					+ "         <testrun_id>N/A</testrun_id>"
					+ "         <testrun_name>Failed test run</testrun_name>"
					+ "         <testrun_description></testrun_description>"
					+ "         <start_datetime>N/A</start_datetime>"
					+ "         <status></status>" + "         <platforms>"
					+ "             <platform>"
					+ "                 <id>N/A</id>"
					+ "                 <code>N/A</code>"
					+ "                 <name><![CDATA[")
					.append(platforms)
					.append("]]></name>"
							+ "                 <browse_url></browse_url>"
							+ "                 <status>N/A</status>"
							+ "                 <messages>"
							+ "                     <fatal>"
							+ "                         <message>"
							+ "                             <txt><![CDATA[")
					.append(e.getMessage())
					.append("]]></txt>"
							+ "                             <created_time></created_time>"
							+ "                         </message>"
							+ "                     </fatal>"
							+ "                 </messages>"
							+ "             </platform>"
							+ "         </platforms>" + "     </execution>"
							+ "</executions>");
			lastExecutionResult = Utils.string2xml(sb.toString());
		} else {
			NodeList nodePlatforms = lastExecutionResult
					.getElementsByTagName("platform");
			for (int i = 0; i < nodePlatforms.getLength(); i++) {
				Node platform = nodePlatforms.item(i);
				Node messages = Utils.createChildElementIfNotExists(platform,
						"messages");
				Node fatal = Utils.createChildElementIfNotExists(messages,
						"fatal");
				Node txt = Utils.createChildElementIfNotExists(fatal, "txt");
				Utils.setCDATAValue(txt, e.getMessage());
			}
		}
		return lastExecutionResult;
	}

	/**
	 * Converts XML document into list of NerrvanaExecution objects 
	 * 
	 * @param doc document containing &lt;execution&gt; nodes
	 * @return list of NerrvanaExecution objects
	 * @throws Exception
	 */
	public static ArrayList<NerrvanaExecution> xml2list(Document doc)
			throws Exception {
		ArrayList<NerrvanaExecution> execs = new ArrayList<NerrvanaExecution>();
		NodeList nodeExecs = doc.getElementsByTagName("execution");
		for (int i = 0; i < nodeExecs.getLength(); i++) {
			Node n = nodeExecs.item(i);
			execs.add(NerrvanaExecution.parseExecution(n));
		}
		return execs;
	}

	private static NerrvanaExecution parseExecution(Node parent)
			throws Exception {
		NodeList list = parent.getChildNodes();
		NerrvanaExecution exec = new NerrvanaExecution();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getNodeName().equals("id"))
				exec.id = Utils.nodeValue(n);
			else if (n.getNodeName().equals("start_datetime"))
				exec.starttime = Utils.nodeValue(n);
			else if (n.getNodeName().equals("status"))
				exec.status = Utils.nodeValue(n);
			else if (n.getNodeName().equals("testrun_id"))
				exec.testrun_id = Utils.nodeValue(n);
			else if (n.getNodeName().equals("testrun_name"))
				exec.testrun_name = Utils.nodeValue(n);
			else if (n.getNodeName().equals("testrun_description"))
				exec.testrun_description = Utils.nodeValue(n);
			else if (n.getNodeName().equals("platforms"))
				exec.platforms = Platform.getPlatformList(n);
		}
		return exec;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---BEGIN EXECUTION#").append(id).append("---\n")
				.append("\tStart time: ").append(starttime).append("\n")
				.append("\tStatus: ").append(status).append("\n");
		if (platforms != null) {
			for (int i = 0; i < platforms.size(); i++)
				sb.append(platforms.get(i).toString());
		}
		sb.append("-----END EXECUTION#").append(id).append("---\n\n");
		return sb.toString();
	}
}