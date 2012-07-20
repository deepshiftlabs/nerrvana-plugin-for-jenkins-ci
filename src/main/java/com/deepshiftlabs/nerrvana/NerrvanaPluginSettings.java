package com.deepshiftlabs.nerrvana;

import java.util.*;
import org.w3c.dom.*;

/**
 * Wraps all Nerrvana related settings.
 * It expects to get following XML:
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
 *   <!-- Test run description, all test runs created by Jenkins will be with this description -->
 *   <test-run-descr>Created by Nerrvana-Jenkins plugin</test-run-descr>
 * 
 *   <!-- Which executable file Nerrvana should use to start tests -->
 *   <executable-file>xbuild-pgsql.sh</executable-file>
 * 
 *   <!-- List of platforms to run tests against for this config -->
 *   <platforms>
 * 
 *       <!-- List of available platforms. Uncomment to use. -->
 * 
 *       <!--platform><code>centos_53_firefox_36</code><name>Firefox 3.6 (CentOS)</name></platform-->
 *       <!--platform><code>winxp_sp3_chrome_1801025</code><name>Chrome 18.0.1025 (WinXP)</name></platform-->
 *       <!--platform><code>winxp_sp3_firefox_36</code><name>Firefox 3.6 (WinXP)</name></platform-->
 *       <!--platform><code>winxp_sp3_ie_8</code><name>IE 8 (WinXP)</name></platform-->
 *       <!--platform><code>winxp_sp3_opera_1162</code><name>Opera 11.62 (WinXP)</name></platform-->
 *       <!--platform><code>winxp_sp3_safari_53455</code><name>Safari 5.34.55 (WinXP)</name></platform-->
 * 
 *       <platform><code>winxp_sp3_firefox_110</code><name>Firefox 11.0 (WinXP)</name></platform>
 *   </platforms>
 * 
 *   <!-- How many Selenium nodes should be used for each platform -->
 *   <nodes-count>3</nodes-count>
 * 
 *   <!-- Parameters related to the transfer of the tests from Jenkins to Nerrvana -->
 *   <!-- Folder in the workspace of Jenkins job where Selenium tests will be located.
 *        It is presumed that build step of SCM plug-in, which always occurs BEFORE other steps, puts tests in there. -->
 *   <folder-with-tests>src</folder-with-tests>
 *   <!-- Nerrvana space previously created by you through Nerrvana web interface -->
 *   <space>
 *       <id>4028</id>
 *       <name>demo_space</name>
 *       <ftp-path>demo_space/_files</ftp-path>
 *   </space>
 *   <!-- Address and credentials of the Nerrvana FTPS connection.
 *        Note that a system running Jenkins should have LFTP application installed. -->
 *   <ftp>
 *     <server>ftp.nerrvana.com</server>
 *     <!-- Your username -->
 *     <username>wise</username>
 *     <!-- Replace this value with your password!! -->
 *     <password>24VZLXSP</password>
 *   </ftp>
 * 
 *   <!-- Execution-specific parameters of the plugin -->
 *   <plugin-settings>
 *     <!-- Maximum execution time (in seconds). Defines how long Nerrvana plug-in
 *          will wait for the tests to complete. Set to quite big value first and
 *          adjust after few runs to avoid indefinite loops, for example.-->
 *     <max-execution-time>3600</max-execution-time>
 *     <!-- How often Nerrvana plug-in will update tests execution status from Nerrvana (seconds) -->
 *     <poll-period>20</poll-period>
 *   </plugin-settings>
 * </nerrvana-plugin>
 * }
 * </pre>
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 * @todo Change parsing method to XPath
 */
public class NerrvanaPluginSettings {
  public String httpurl;
  public String apikey;
  public String secretkey;

  public int nodes_count;
  public String test_run_name;
  public String test_run_descr;
  
  public String executable_file;
  public boolean validation = false;
  public List<Platform> platforms;

  public String folder_with_tests;
  public String space_id;
  public String space_name;
  public String space_path;
  public String ftpurl;
  public String ftpuser;
  public String ftppass;

  public int maxtime;
  public int poll;

  public static NerrvanaPluginSettings parse(Document doc) throws Exception{
    NodeList list = null;
    NerrvanaPluginSettings npc = new NerrvanaPluginSettings();
    npc.httpurl = Utils.uniqueTagValue("gateway",doc,true);
    npc.apikey = Utils.uniqueTagValue("apikey",doc,true);
    npc.secretkey = Utils.uniqueTagValue("secretkey",doc,true);

    String s = Utils.uniqueTagValue("nodes-count",doc,true);
    try{
        npc.nodes_count = Integer.parseInt(s);
        if(npc.nodes_count <= 0)
            throw new Exception();
    }
    catch(Exception e){
        Logger.infoln("\tSelenium nodes per platform value is incorrect: "+s+". Reset to 1.\n");
        npc.nodes_count = 1;
    }

    npc.test_run_name = Utils.uniqueTagValue("test-run-name",doc,false);
    npc.test_run_descr = Utils.uniqueTagValue("test-run-descr",doc,false);

    npc.executable_file = Utils.uniqueTagValue("executable-file",doc,true);
    npc.folder_with_tests = Utils.uniqueTagValue("folder-with-tests",doc,false);

    list = doc.getElementsByTagName("space");
    if(list.getLength() != 1)
      throw new Exception("Nerrvana space incorrectly defined");

    npc.space_id = Utils.getUniqueChildNodeValue(list.item(0),"id");
    npc.space_name = Utils.getUniqueChildNodeValue(list.item(0),"name");
    npc.space_path = Utils.getUniqueChildNodeValue(list.item(0),"ftp-path");

    npc.ftpurl = Utils.uniqueTagValue("server",doc,true);
    npc.ftpuser = Utils.uniqueTagValue("username",doc,true);
    npc.ftppass = Utils.uniqueTagValue("password",doc,true);

    try{
      npc.maxtime = Integer.parseInt(Utils.uniqueTagValue("max-execution-time",doc,false));
    }
    catch(Exception e){
      npc.maxtime = 60 * 60;
    }

    try{
      npc.poll = Integer.parseInt(Utils.uniqueTagValue("poll-period",doc,false));
    }
    catch(Exception e){
      npc.poll = 25;
    }

    list = doc.getElementsByTagName("platforms");
    if(list != null && list.getLength() > 0)
      npc.platforms = Platform.getPlatformList(list.item(0));
    return npc;
  }

  public boolean checkSettings(){
    boolean result = true;
    Logger.infoln("---BEGIN logger SETTINGS---");
    Logger.infoln("\tNerrvana HTTP address: "+this.httpurl);
    if(httpurl == null || httpurl.length() == 0 || !(httpurl.startsWith("http://") || httpurl.startsWith("https://"))){
      Logger.infoln("\tNerrvana web address doesn't look like valid HTTP/HTTPS URL. Fatal error.");
      result = false;
    }
    Logger.infoln("\tAPI key: "+this.apikey);
    if(apikey == null || apikey.length() == 0){
      Logger.infoln("\tAPI key doesn't look like valid Nerrvana API key. Fatal error.");
      result = false;
    }

    Logger.infoln("\tSecret key: "+this.secretkey);
    if(secretkey == null || secretkey.length() == 0){
      Logger.infoln("\tSecret key doesn't look like valid Nerrvana secret key. Fatal error.\n");
      result = false;
    }
    
    Logger.infoln("\tSpace ID: "+this.space_id);
    if(space_id == null || space_id.length() == 0){
      Logger.infoln("\tNerrvana space ID is not valid. Fatal error.");
      result = false;
    }
    
    Logger.infoln("\tSpace: "+this.space_name);
    if(space_name == null || space_name.length() == 0){
      Logger.infoln("\tNerrvana space name is not valid. Fatal error.");
      result = false;
    }
    
    //TODO: can it be empty? 
    Logger.infoln("\tSpace path[FTP folder]: "+this.space_path);
    if(space_path == null || space_path.length() == 0){
      Logger.infoln("\tNerrvana space folder is not valid. Fatal error.");
      result = false;
    }

    Logger.infoln("\tSelenium nodes per platform: "+this.nodes_count);
    Logger.infoln("\tTest run name: "+this.test_run_name);
    
    if(platforms == null || platforms.size() == 0){
      Logger.infoln("\tNerrvana platforms not defined. Fatal error.");
      result = false;
    }
    else{
      Logger.infoln("\tNerrvana platforms:");
      for(Iterator<Platform> it = platforms.iterator(); it.hasNext();)
        Logger.infoln("\t\t"+it.next().name);
    }
    
    Logger.infoln("\tExecutable file: "+this.executable_file);
    if(executable_file == null || executable_file.length() == 0){
      Logger.infoln("\tExecutable file not defined. Fatal error.");
      result = false;
    }

    if(ftpurl == null || ftpurl.length() == 0){
      Logger.infoln("\tNerrvana FTP address: "+this.ftpurl);
      Logger.infoln("\tNerrvana FTP address not defined. Fatal error.");
      result = false;
    }
    else{
      if(ftpurl.startsWith("ftp://"))
        ftpurl = ftpurl.substring("ftp://".length());
      else if(ftpurl.startsWith("ftps://"))
        ftpurl = ftpurl.substring("ftps://".length());
      Logger.infoln("\tNerrvana FTP address: "+this.ftpurl);
    }

    Logger.infoln("\tNerrvana FTP user: "+this.ftpuser);
    if(ftpuser == null || ftpuser.length() == 0){
      Logger.infoln("\tNerrvana FTP username is missing. Fatal error.");
      result = false;
    }
    Logger.infoln("\tNerrvana FTP pass: "+this.ftppass);
    if(ftppass == null || ftppass.length() == 0){
      Logger.infoln("\tNerrvana FTP password is missing. Fatal error.");
      result = false;
    }
    
    Logger.infoln("\tWorkspace folder: "+this.folder_with_tests);
    if(folder_with_tests == null || folder_with_tests.length() == 0){
      Logger.infoln("\tWorkspace folder not specified. Assuming workspace root.");
    }
    Logger.infoln("\tMax execution time: "+this.maxtime);
    Logger.infoln("\tPoll period: "+this.poll);

    Logger.infoln("-----END logger SETTINGS---");
    return result;
  }
  
  public long getMaxtimeMillis(){
    long x = maxtime;
    if(x < 60){
      Logger.infoln("Maximum execution time is too small["+x+"]. Resetting to 60 seconds (1 minute)");
      x = 60;
    }
    else if(x > 7200){
      Logger.infoln("Maximum execution time is too big["+x+"]. Resetting to 7200 seconds (2 hours)");
      x = 7200;
    }
    return (long)(x * 1000);
  }

  public long getPollMillis(){
    long x = poll;
    if(x < 20){
      Logger.infoln("Poll period is too short["+x+"]. Resetting to 20 seconds");
      x = 20;
    }
    else if(x > 1800){
      Logger.infoln("Poll period is too long["+x+"]. Resetting to 1800 seconds (30 minutes)");
      x = 1800;
    }
    return (long)(x * 1000);
  }

  public static void main(String[] args) throws Exception{
    Logger.init(System.out,"trace");
    if(args.length > 0){
      for(int i = 0; i < args.length; i++){
        NerrvanaPluginSettings config = NerrvanaPluginSettings.parse(Utils.file2xml(args[i]));
        config.checkSettings();
      }
    }
  }
}
