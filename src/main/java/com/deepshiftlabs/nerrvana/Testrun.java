package com.deepshiftlabs.nerrvana;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import org.w3c.dom.*;

/**
 * <p> Wraps information about
 * {@link <a href="https://nerrvana.com/docs/test-runs">Nerrvana test run</a>}
 * </p> This class expects following XML returned by Nerrvana API call:
 *
 * <pre>
 * {@code
 * <testruns>
 *   <testrun>
 *     <id>1181</id>
 *     <space_id>676</space_id>
 *     <name><![CDATA[First_created_by_API]]></name>
 *     <description><![CDATA[Bla bla bla]]></description>
 *     <executable_file><![CDATA[rebuildAndRun.sh]]></executable_file>
 *     <platforms>
 *       <platform>
 * <code>winxp_sp3_firefox_3</code> <name>Firefox 3/WinXP SP3</name> <platform>
 * </platforms> <start_date>2012-02-21</start_date> <week_days>Monday,
 * Tuesday</week_days> <start_time>20:45</start_time>
 * <period_type>hr</period_type> <periodicity>10</periodicity>
 * <on_pause>1</on_pause> <validation>1</validation>
 * <ftp_name>First_created_by_API</ftp_name>
 *
 * <next_executions> <next_execution> <id>1767812</id>
 * <start_datetime>2012-02-21 20:46:19</start_datetime> </next_execution>
 * </next_executions> </testrun> </testruns> }
 * </pre>
 *
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class Testrun {

    public String id;
    public String exec_id;
    public String name;
    public String description;

    public Testrun() {
    }

    public String getId() {
        return id;
    }

    public String getExecid() {
        return exec_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @version 1.0 finds revision.txt, extracts revision number and appends it
     * to the name.
     * @version 1.1 revision.txt related logic excluded, since Jenkins job could
     * be non SCM-driven
     * @param test_run_name
     * @param build
     * @return assembled test run name
     */
    public static String assembleName(String test_run_name, AbstractBuild<?, ?> build) {
        if (test_run_name == null || test_run_name.length() == 0) {
            test_run_name = build.getProject().getDisplayName();
        }
        return test_run_name + " build #" + build.getNumber();
    }
    
    /**
     * Assembles test run description from static part contained in NerrvanaPluginSettings.test_run_descr
     * and dynamic part contained in the file defined as NerrvanaPluginSettings.test_run_descr_file
     * 
     * @param workspace 
     * @param settings
     * @return test run description
     * @throws Exception 
     */
    public static String assembleDescription(FilePath workspace, NerrvanaPluginSettings settings) throws Exception {
        String tdesc = settings.test_run_descr == null ? "" : settings.test_run_descr;
        if (settings.test_run_descr_file != null && settings.test_run_descr_file.length() > 0) {
            String from_file = "";
            if (workspace.child(settings.test_run_descr_file).exists()) {
                from_file = workspace.child(settings.test_run_descr_file).readToString();
            }
            if (from_file != null && from_file.trim().length() > 0) {
                tdesc += "\n" + from_file;
            }
        }
        return tdesc;
    }

    /**
     * Maps XML document to Testrun object
     *
     * @param parent parent XML node to start mapping from
     * @return mapped object
     * @throws Exception
     */
    public static Testrun getTestrun(Node parent) throws Exception {
        Testrun testrun = new Testrun();
        Node root = Utils.getChildNode(parent, "testrun");
        Node node = Utils.getChildNode(root, "id");
        testrun.id = Utils.nodeValue(node);
        node = Utils.getChildNode(root, "name");
        testrun.name = Utils.nodeValue(node);
        node = Utils.getChildNode(root, "description");
        testrun.description = Utils.nodeValue(node);

        node = Utils.getChildNode(root, "next_executions");
        node = Utils.getChildNode(node, "next_execution");
        node = Utils.getChildNode(node, "id");
        testrun.exec_id = Utils.nodeValue(node);
        return testrun;
    }
}