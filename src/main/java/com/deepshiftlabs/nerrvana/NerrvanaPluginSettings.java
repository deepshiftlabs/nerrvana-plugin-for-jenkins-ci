package com.deepshiftlabs.nerrvana;

import java.util.*;
import org.w3c.dom.*;

/**
 * Wraps all Nerrvana related settings. It expects to get following XML:
 *
 * <pre>
 * {@code
 *
 * <nerrvana-plugin>
 * <!-- You will be able to use this file to configure Nerrvana Jenkins plug-in in a near future -->
 * <!-- Nerrvana API parameters -->
 * <api-params>
 *       <!-- Address of the Nerrvana gateway -->
 *       <gateway>https://api.nerrvana.com</gateway>
 *       <!-- User-specific key which identifies user on Nerrvana side -->
 *       <!-- Available as an 'API public key' on Settings page (https://cloud.nerrvana.com/user/editAccount) in Nerrvana. -->
 *       <apikey>2bf7c81-a031f-1ad2-fd3a6-f59b9e0668e</apikey>
 *       <!-- This key is used by Nerrvana plug-in to create a checksum of an API call parameters to ensure their consistency. -->
 *       <!-- Available as an 'API private key' on Settings page in Nerrvana. -->
 *       <secretkey>wctfFwelygx3tXS4TasrsOS4oXV7YadcPppvEnx55WG7qPk6jrAHjJi1TRoLlhrarIlabte4H9zWROXVkLnXto2LlVC47EXx91Uu</secretkey>
 *   </api-params>
 *
 *   <!-- Parameters related to Nerrvana-driven Selenium tests -->
 *   <!-- Test run name template, the revision number will be added to the end automatically -->
 *   <test-run-name>AnswersTests_PGSQL</test-run-name>
 *
 *   <!-- Test run description, all test runs created by Jenkins will have this description -->
 *   <test-run-descr>Created by Nerrvana-Jenkins plugin</test-run-descr>
 *
 *   <!-- Dynamic part of the test run description.
 *        If such file exists, plugin will append its content to the test run description.
 *        For example, you can pass SVN revision number, SVN commit time or any other information into plugin.
 *    -->
 *   <test-run-descr-file>info.txt</test-run-descr-file>
 *
 *   <!-- Which executable file Nerrvana should use to start tests -->
 *   <executable-file>xbuild-pgsql.sh</executable-file>
 *
 *   <!-- List of platforms to run tests against for this config -->
 *   <platforms>
 *
 *       <!-- List of available platforms. Uncomment to use. -->
 *
 *       <!--platform><code>centos_53_firefox_36</code><name>Firefox 3.6
 * (CentOS)</name></platform-->
 * <!--platform><code>winxp_sp3_chrome_1801025</code><name>Chrome 18.0.1025
 * (WinXP)</name></platform-->
 * <!--platform><code>winxp_sp3_firefox_36</code><name>Firefox 3.6
 * (WinXP)</name></platform--> <!--platform><code>winxp_sp3_ie_8</code><name>IE
 * 8 (WinXP)</name></platform-->
 * <!--platform><code>winxp_sp3_opera_1162</code><name>Opera 11.62
 * (WinXP)</name></platform-->
 * <!--platform><code>winxp_sp3_safari_53455</code><name>Safari 5.34.55
 * (WinXP)</name></platform-->
 *
 * <platform><code>winxp_sp3_firefox_110</code><name>Firefox 11.0
 * (WinXP)</name></platform> </platforms>
 *
 * <!-- How many Selenium nodes should be used for each platform -->
 * <nodes-count>3</nodes-count>
 *
 * <!-- Parameters related to the transfer of the tests from Jenkins to Nerrvana
 * --> <!-- Folder in the workspace of Jenkins job where Selenium tests will be
 * located. It is presumed that build step of SCM plug-in, which always occurs
 * BEFORE other steps, puts tests in there. -->
 * <folder-with-tests>src</folder-with-tests> <!-- Nerrvana space previously
 * created by you through Nerrvana web interface --> <space> <id>4028</id>
 * <name>demo_space</name> <ftp-path>demo_space/_files</ftp-path> </space> <!--
 * Address and credentials of the Nerrvana FTPS connection. Note that a system
 * running Jenkins should have LFTP application installed. --> <ftp>
 * <server>ftp.nerrvana.com</server> <!-- Your username -->
 * <username>wise</username> <!-- Replace this value with your password!! -->
 * <password>24VZLXSP</password> </ftp>
 *
 * <!-- Execution-specific parameters of the plugin --> <plugin-settings> <!--
 * Maximum execution time (in seconds). Defines how long Nerrvana plug-in will
 * wait for the tests to complete. Set to quite big value first and adjust after
 * few runs to avoid indefinite loops, for example.-->
 * <max-execution-time>3600</max-execution-time> <!-- How often Nerrvana plug-in
 * will update tests execution status from Nerrvana (seconds) -->
 * <poll-period>20</poll-period> </plugin-settings>
 *
 * <!-- Optional parameter. Default value is true -->
 * <use-messages-to-set-build-status>true</use-messages-to-set-build-status>
 * <!-- Message threshold level. If user message level exceeds this level -
 * Nerrvana execution will be marked as FAILURE, and, consequently, full build
 * will be marked as failure --> <message_threshold>warn</message_threshold>
 * </nerrvana-plugin> }
 * </pre>
 *
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 * @todo Change parsing method to XPath
 */
public class NerrvanaPluginSettings {

    /**
     * Nerrvana address.
     */
    public String httpurl;
    /**
     * Key, which identifies Nerrvana user
     */
    public String apikey;
    /**
     * Secret key, the plugin uses to encrypt request (and Nerrvana API uses to
     * decrypt it)
     */
    public String secretkey;
    /**
     * Count of nodes (Selenium RC instances) Nerrvana should start for the
     * given tests set. Makes sense for the tests which support parallelizing
     * (TestNG)
     */
    public int nodes_count;
    /**
     * Name of the test run. Nerrvana creates folder in the user space with such
     * name
     */
    public String test_run_name;
    /**
     * Test run description - static part of
     */
    public String test_run_descr;
    /**
     * Name of the file containing dynamic part of the test run description. It
     * is presumed that file and its content has been created by some external
     * means prior the plugin start. Plugin appends content of this file to the
     * {@link NerrvanaPluginSettings#test_run_descr}
     */
    public String test_run_descr_file;
    /**
     * Name of the file Nerrvana should start to run Selenium tests
     */
    public String executable_file;
    /**
     * Should Nerrvana validate code or not So far Nerrvana supports validation
     * of the PHP (by lint) and Java (by compiling the code)
     */
    public boolean validation = false;
    /**
     * List of platforms (where platform is combination of the OS and browser of
     * the certain version)
     */
    public List<Platform> platforms;
    /**
     * Local folder in the workspace of the Jenkins job, this plugin belongs to.
     * Plugin presumes that this folder contains code of the tests to be
     * transferred to Nerrvana
     */
    public String folder_with_tests;
    /**
     * Nerrvana space ID. Space should be created manually by Nerrvana user
     * through web interface.
     */
    public String space_id;
    /**
     * Nerrvana space name. Space should be created manually by Nerrvana user
     * through web interface.
     */
    public String space_name;
    /**
     * Nerrvana space path - folder in Nerrvana FTPS server where plugin should
     * put tests. Space should be created manually by Nerrvana user through web
     * interface.
     */
    public String space_path;
    /**
     * FTPS URL - address of the Nerrvana FTPS server
     */
    public String ftpsurl;
    /**
     * Name of the FTPS user FTPS credentials are the same as the credentials
     * for the website
     */
    public String ftpsuser;
    /**
     * Password of the FTPS user FTPS credentials are the same as the
     * credentials for the website
     */
    public String ftpspass;
    /**
     * Maximum execution time for the plugin in seconds. Time count starts after
     * creation and starting of the test run
     */
    public int maxtime;
    /**
     * Poll period - amount of time between calls from plugin to Nerrvana.
     * Polling process starts after creation and starting of the test run
     */
    public int poll;
    /**
     * Name of the file in the build folder where to the plugin should store
     * test run execution results. Also see {@link #testExecutionResults}
     */
    public final String executionResultsFilename = "results.xml";
    /**
     * This flag determines should this plugin instance evaluate results of the
     * Nerrvana execution or not. If this flag=true and there were errors during
     * this or any previous Nerrvana plugin execution -
     * {@link NerrvanaPlugin#perform(hudson.model.AbstractBuild, hudson.Launcher, hudson.model.BuildListener)}
     * will return false. Otherwise (this flag=false or there were no errors) it
     * returns true.
     */
    public boolean testExecutionResults = true;

    /*
     * Message threshold 
     */
    public UserMessageLevel messageThreshold = UserMessageLevel.ERROR;
    /**
     * Flag which commands to plugin to skip upload step (Which means that files
     * were uploaded to Nerrvana cloud in some alternate way)
     */
    public boolean skipUpload = false;

    public static NerrvanaPluginSettings parse(Document doc) throws Exception {
        NodeList list = null;
        NerrvanaPluginSettings npc = new NerrvanaPluginSettings();
        npc.httpurl = Utils.uniqueTagValue("gateway", doc, true);
        npc.apikey = Utils.uniqueTagValue("apikey", doc, true);
        npc.secretkey = Utils.uniqueTagValue("secretkey", doc, true);

        String s = Utils.uniqueTagValue("nodes-count", doc, true);
        try {
            npc.nodes_count = Integer.parseInt(s);
            if (npc.nodes_count <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            Logger.infoln("\tSelenium nodes per platform value is incorrect: "
                    + s + ". Reset to 1.\n");
            npc.nodes_count = 1;
        }

        // should the plugin evaluate all test results or skip it to some other
        // build step
        s = Utils.uniqueTagValue("use-messages-to-set-build-status", doc, false);
        if (s != null && (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("0"))) {
            npc.testExecutionResults = false;
        }

        // set error threshold
        s = Utils.uniqueTagValue("message-threshold", doc, false);
        if (s != null) {
            npc.messageThreshold = UserMessageLevel.parseThreshold(s);
        }

        //
        npc.test_run_name = Utils.uniqueTagValue("test-run-name", doc, false);
        npc.test_run_descr = Utils.uniqueTagValue("test-run-descr", doc, false);
        npc.test_run_descr_file = Utils.uniqueTagValue("test-run-descr-file",
                doc, false);

        npc.executable_file = Utils
                .uniqueTagValue("executable-file", doc, true);
        npc.folder_with_tests = Utils.uniqueTagValue("folder-with-tests", doc,
                false);

        list = doc.getElementsByTagName("space");
        if (list.getLength() != 1) {
            throw new Exception("Nerrvana space incorrectly defined");
        }

        npc.space_id = Utils.getUniqueChildNodeValue(list.item(0), "id");
        npc.space_name = Utils.getUniqueChildNodeValue(list.item(0), "name");
        npc.space_path = Utils
                .getUniqueChildNodeValue(list.item(0), "ftp-path");

        npc.ftpsurl = Utils.uniqueTagValue("server", doc, true);
        npc.ftpsuser = Utils.uniqueTagValue("username", doc, true);
        npc.ftpspass = Utils.uniqueTagValue("password", doc, true);

        try {
            npc.maxtime = Integer.parseInt(Utils.uniqueTagValue(
                    "max-execution-time", doc, false));
        } catch (Exception e) {
            npc.maxtime = 60 * 60;
        }

        try {
            npc.poll = Integer.parseInt(Utils.uniqueTagValue("poll-period",
                    doc, false));
        } catch (Exception e) {
            npc.poll = 25;
        }

        list = doc.getElementsByTagName("platforms");
        if (list != null && list.getLength() > 0) {
            npc.platforms = Platform.getPlatformList(list.item(0));
        }
        
        s = Utils.uniqueTagValue("skip-tests-sync", doc, false);
        if (s !=  null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes"))) {
            npc.skipUpload = true;
        }
        
        return npc;
    }

    /**
     * Validates plugin settings
     *
     * @return true if settings are correct
     */
    public boolean checkSettings() {
        boolean result = true;
        StringBuilder sb = new StringBuilder("\n---BEGIN PLUGIN SETTINGS---\n");
        sb.append("\tNerrvana HTTP address: " + this.httpurl).append("\n");
        if (httpurl == null
                || httpurl.length() == 0
                || !(httpurl.startsWith("http://") || httpurl
                .startsWith("https://"))) {
            sb.append("\tNerrvana web address doesn't look like valid HTTP/HTTPS URL. Fatal error.\n");
            result = false;
        }
        sb.append("\tNerrvana API key: " + this.apikey).append("\n");
        if (apikey == null || apikey.length() == 0) {
            sb.append("\tNerrvana API key not set or is invalid. Fatal error.\n");
            result = false;
        }

        sb.append("\tSecret key: " + this.secretkey).append("\n");
        if (secretkey == null || secretkey.length() == 0) {
            sb.append("\tSecret key doesn't look like valid Nerrvana secret key. Fatal error.\n");
            result = false;
        }

        sb.append("\tSpace ID: " + this.space_id).append("\n");
        if (space_id == null || space_id.length() == 0) {
            sb.append("\tNerrvana space ID is not valid. Fatal error.\n");
            result = false;
        }

        sb.append("\tSpace: " + this.space_name).append("\n");
        if (space_name == null || space_name.length() == 0) {
            sb.append("\tNerrvana space name is not valid. Fatal error.\n");
            result = false;
        }

        // TODO: can it be empty?
        sb.append("\tSpace path[FTPS folder]: " + this.space_path).append("\n");
        if (space_path == null || space_path.length() == 0) {
            sb.append("\tNerrvana space folder is not valid. Fatal error.\n");
            result = false;
        }

        sb.append("\tSelenium nodes per platform: " + this.nodes_count).append(
                "\n");
        sb.append("\tTest run name: " + this.test_run_name).append("\n");

        if (platforms == null || platforms.size() == 0) {
            sb.append("\tNerrvana platforms not defined. Fatal error.\n");
            result = false;
        } else {
            sb.append("\tNerrvana platforms:\n");
            for (Iterator<Platform> it = platforms.iterator(); it.hasNext();) {
                sb.append("\t\t" + it.next().name).append("\n");
            }
        }

        sb.append("\tExecutable file: " + this.executable_file).append("\n");
        if (executable_file == null || executable_file.length() == 0) {
            sb.append("\tExecutable file not defined. Fatal error.\n");
            result = false;
        }

        if (ftpsurl == null || ftpsurl.length() == 0) {
            sb.append("\tNerrvana FTPS address: " + this.ftpsurl);
            sb.append("\tNerrvana FTPS address not defined. Fatal error.\n");
            result = false;
        } else {
            if (ftpsurl.startsWith("ftp://")) {
                ftpsurl = ftpsurl.substring("ftp://".length());
            } else if (ftpsurl.startsWith("ftps://")) {
                ftpsurl = ftpsurl.substring("ftps://".length());
            }
            sb.append("\tNerrvana FTPS address: " + this.ftpsurl).append("\n");
        }

        sb.append("\tNerrvana FTPS user: " + this.ftpsuser).append("\n");
        if (ftpsuser == null || ftpsuser.length() == 0) {
            sb.append("\tNerrvana FTPS username is missing. Fatal error.\n");
            result = false;
        }
        sb.append("\tNerrvana FTPS pass: " + this.ftpspass).append("\n");
        if (ftpspass == null || ftpspass.length() == 0) {
            sb.append("\tNerrvana FTPS password is missing. Fatal error.\n");
            result = false;
        }

        sb.append("\tWorkspace folder: " + this.folder_with_tests).append("\n");
        if (folder_with_tests == null || folder_with_tests.length() == 0) {
            sb.append("\tWorkspace folder not specified. Assuming workspace root.\n");
        }
        sb.append("\tMax execution time: " + this.maxtime).append("\n");
        sb.append("\tPoll period: " + this.poll).append("\n");
        sb.append(
                "\tParse user messages mode[results analyzer]: "
                + (testExecutionResults ? "ON" : "OFF")).append("\n");
        if (testExecutionResults) {
            sb.append("\tUser message threshold: " + messageThreshold.name())
                    .append("\n");
        }

        sb.append("-----END PLUGIN SETTINGS---");
        Logger.infoln(sb.toString());
        return result;
    }

    /**
     * Checks {@link #maxtime} range, adjusts it if needed and converts to
     * milliseconds
     *
     * @return adjusted {@link #maxtime} in milliseconds
     */
    public long getMaxtimeMillis() {
        long x = maxtime;
        if (x < 60) {
            Logger.infoln("Maximum execution time is too small[" + x
                    + "]. Resetting to 60 seconds (1 minute)");
            x = 60;
        } else if (x > 7200) {
            Logger.infoln("Maximum execution time is too big[" + x
                    + "]. Resetting to 7200 seconds (2 hours)");
            x = 7200;
        }
        return (long) (x * 1000);
    }

    /**
     * Checks {@link #poll} range, adjusts it if needed and converts to
     * milliseconds
     *
     * @return adjusted {@link #poll} in milliseconds
     */
    public long getPollMillis() {
        long x = poll;
        if (x < 20) {
            Logger.infoln("Poll period is too short[" + x
                    + "]. Resetting to 20 seconds");
            x = 20;
        } else if (x > 1800) {
            Logger.infoln("Poll period is too long[" + x
                    + "]. Resetting to 1800 seconds (30 minutes)");
            x = 1800;
        }
        return (long) (x * 1000);
    }
}
