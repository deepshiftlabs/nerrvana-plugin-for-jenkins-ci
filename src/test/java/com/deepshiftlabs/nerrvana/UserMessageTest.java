package com.deepshiftlabs.nerrvana;

import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.*;
//import org.junit.*;
import junit.framework.TestCase;

public class UserMessageTest extends TestCase {
    private String xmlActual = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><executions><execution><id>4766</id><testrun_id>640</testrun_id><start_datetime>1344531857</start_datetime><status><![CDATA[ok]]></status><platforms><platform><id>9594</id><code>winxp_sp3_firefox_110</code><name><![CDATA[Firefox 11.0/WinXP SP3]]></name><browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_286/2012_08_09_17_04_17/winxp_sp3_firefox_110/</browse_url><status><![CDATA[ok]]></status><messages><error><message><txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin]Values:    used:[],    expected:[true],    actual:[false]]]></txt><created_time>1344521119</created_time></message><message><txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt><created_time>1344521119</created_time></message><message><txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin]Values:    used:[],    expected:[true],    actual:[false]]]></txt><created_time>1344521122</created_time></message><message><txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt><created_time>1344521123</created_time></message></error></messages></platform></platforms><testrun_name><![CDATA[AnswersTests_PGSQL build #286]]></testrun_name><testrun_description><![CDATA[Created by Nerrvana-Jenkins pluginRevision: 472Committer: bearDate: 2012-08-09 13:42:36 +0000 (Thu, 09 Aug 2012)Fixed some errors.]]></testrun_description></execution><execution><id>4767</id><testrun_id>641</testrun_id><start_datetime>1344531967</start_datetime><status><![CDATA[ok]]></status><platforms><platform><id>9595</id><code>winxp_sp3_firefox_110</code><name><![CDATA[Firefox 11.0/WinXP SP3]]></name><browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_MYSQL_build_286/2012_08_09_17_06_07/winxp_sp3_firefox_110/</browse_url><status><![CDATA[ok]]></status><messages><error><message><txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin]Values:  used:[],    expected:[true],    actual:[false]]]></txt><created_time>1344521255</created_time></message><message><txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt><created_time>1344521255</created_time></message><message><txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin]Values:    used:[],    expected:[true],    actual:[false]]]></txt><created_time>1344521258</created_time></message><message><txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt><created_time>1344521258</created_time></message></error></messages></platform></platforms><testrun_name><![CDATA[AnswersTests_MYSQL build #286]]></testrun_name><testrun_description><![CDATA[Created by Nerrvana-Jenkins pluginRevision: 472Committer: bearDate: 2012-08-09 13:42:36 +0000 (Thu, 09 Aug 2012)Fixed some errors.]]></testrun_description></execution></executions>";
    
    private String xml = 
"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
"   <executions>\n" +
"       <execution id=\"\">\n" +
"           <id>4691</id>\n" +
"           <testrun_id>597</testrun_id>\n" +
"           <start_datetime>1343836999</start_datetime>\n" +
"           <status><![CDATA[ok]]></status>\n" +
"           <platforms>\n" +
"               <platform id=\"p1\">\n" +
"                   <id>9471</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_264/2012_08_01_16_03_19/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <error>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </error>\n" +

"                       <info>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                       </info>\n" +

"                   </messages>\n" +
"               </platform>\n" +
"               <platform id=\"p2\">\n" +
"                   <id>9471</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_264/2012_08_01_16_03_19/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <error>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </error>\n" +

"                       <info>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                       </info>\n" +

"                   </messages>\n" +
"               </platform>\n" +
"           </platforms>\n" +
"       </execution>\n" +
"       <execution>\n" +
"           <id>4692</id>\n" +
"           <testrun_id>598</testrun_id>\n" +
"           <start_datetime>1343837106</start_datetime>\n" +
"           <status><![CDATA[ok]]></status>\n" +
"           <platforms>\n" +
"               <platform id=\"p3\">\n" +
"                   <id>9472</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_MYSQL_build_264/2012_08_01_16_05_06/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <error>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826364</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin]Values:    used:[],    expected:[true],    actual:[false]]]></txt>\n" +
"                               <created_time>1343826367</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826367</created_time>\n" +
"                           </message>\n" +
"                       </error>\n" +

"                       <trace>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE3]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE4]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </trace>\n" +

"                   </messages>\n" +
"               </platform>\n" +
"           </platforms>\n" +
"       </execution>\n" +
"   </executions>\n" +
"";

    
    private String xmlFatal = 
"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
"   <executions>\n" +
"       <execution>\n" +
"           <id>4691</id>\n" +
"           <testrun_id>597</testrun_id>\n" +
"           <start_datetime>1343836999</start_datetime>\n" +
"           <status><![CDATA[ok]]></status>\n" +
"           <platforms>\n" +
"               <platform>\n" +
"                   <id>9471</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_264/2012_08_01_16_03_19/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <fatal>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </fatal>\n" +

"                       <info>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                       </info>\n" +
"                   </messages>\n" +
"               </platform>\n" +
"           </platforms>\n" +
"       </execution>\n" +
"   </executions>\n";
            
    private String xmlError = 
"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
"   <executions>\n" +
"       <execution>\n" +
"           <id>4691</id>\n" +
"           <testrun_id>597</testrun_id>\n" +
"           <start_datetime>1343836999</start_datetime>\n" +
"           <status><![CDATA[ok]]></status>\n" +
"           <platforms>\n" +
"               <platform>\n" +
"                   <id>9471</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_264/2012_08_01_16_03_19/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <error>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: Do Successful Logout, target:[Login-form]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </error>\n" +

"                       <trace>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[TRACE2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                       </trace>\n" +
"                   </messages>\n" +
"               </platform>\n" +
"           </platforms>\n" +
"       </execution>\n" +
"   </executions>\n";

    private String xmlWarn = 
"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
"   <executions>\n" +
"       <execution>\n" +
"           <id>4691</id>\n" +
"           <testrun_id>597</testrun_id>\n" +
"           <start_datetime>1343836999</start_datetime>\n" +
"           <status><![CDATA[ok]]></status>\n" +
"           <platforms>\n" +
"               <platform>\n" +
"                   <id>9471</id>\n" +
"                   <code>winxp_sp3_firefox_110</code>\n" +
"                   <name><![CDATA[Firefox 11.0/WinXP SP3]]></name>\n" +
"                   <browse_url>https://wise.ftp.nerrvana.com/demo_space/_test_runs/AnswersTests_PGSQL_build_264/2012_08_01_16_03_19/winxp_sp3_firefox_110/</browse_url>\n" +
"                   <status><![CDATA[ok]]></status>\n" +
"                   <messages>\n" +
"                       <warn>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values: used:[], expected:[true], actual:[false]]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[ERROR: checkElementPresent, target:[lnkLogin] Values:used:[],expected:[true],actual:[false]]]></txt>\n" +
"                               <created_time>1343826270</created_time>\n" +
"                           </message>\n" +
"                       </warn>\n" +

"                       <info>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO1]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                           <message>\n" +
"                               <txt><![CDATA[INFO2]]></txt>\n" +
"                               <created_time>1343826267</created_time>\n" +
"                           </message>\n" +
"                       </info>\n" +
"                   </messages>\n" +
"               </platform>\n" +
"           </platforms>\n" +
"       </execution>\n" +
"   </executions>\n";

    
    public void setUp() throws Exception{
    }
    
    public void testUserMessages() throws Exception{
        Document doc = Utils.string2xml(xml);
        SortedMap<UserMessageLevel, List<UserMessage>> messageMap = UserMessage.getAllUserMessages(doc);
        List<UserMessage> list = null;
        //trace
        list = messageMap.get(UserMessageLevel.TRACE);
        assertNotNull(list);
        assertEquals(4, list.size());
        //debug
        list = messageMap.get(UserMessageLevel.DEBUG);
        assertNull(list);
        //info
        list = messageMap.get(UserMessageLevel.INFO);
        assertNotNull(list);
        assertEquals(5, list.size());
        //warn
        list = messageMap.get(UserMessageLevel.WARN);
        assertNull(list);
        //error
        list = messageMap.get(UserMessageLevel.ERROR);
        assertNotNull(list);
        assertEquals(16, list.size());
        for(Iterator<UserMessage> iter = list.iterator(); iter.hasNext();)
            System.out.println(iter.next().toString());
        
        //fatal
        list = messageMap.get(UserMessageLevel.FATAL);
        assertNull(list);
    }

    public void testPlatformUserMessages() throws Exception{
//        XPath xpath = XPathFactory.newInstance().newXPath();
//        String expression = "execution";
//        Node widgetNode = (Node) xpath.evaluate(expression, doc, XPathConstants.NODESET);        

        SortedMap<UserMessageLevel, List<UserMessage>> messageMap = null;
        List<UserMessage> list = null;
        Node platform = null;
        Document doc = null;

        doc = Utils.string2xml(xmlActual);
        platform = doc.getElementsByTagName("platform").item(0);
        
        
        doc = Utils.string2xml(xml);
        messageMap = UserMessage.getPlatformUserMessages(platform);
        list = messageMap.get(UserMessageLevel.ERROR);
        assertNotNull(list);
        assertEquals(4, list.size());

        // test 'p1' node
        NodeList platforms = doc.getElementsByTagName("platform");
        for(int i = 0; i < platforms.getLength(); i++){
            Element el = (Element)platforms.item(i);
            el.setIdAttribute("id", true);
//            Node id = nnm.getNamedItem("id");
//            if(id != null)
//                System.out.println(id.getNodeValue());
//            else
//                System.out.println("platform node doesn't have ID attribute");
        }
        
        
        platform = doc.getElementById("p1");
        assertNotNull(platform);
        messageMap = UserMessage.getPlatformUserMessages(platform);
        //trace
        list = messageMap.get(UserMessageLevel.TRACE);
        assertNull(list);
        //debug
        list = messageMap.get(UserMessageLevel.DEBUG);
        assertNull(list);
        //info
        list = messageMap.get(UserMessageLevel.INFO);
        assertEquals(2, list.size());
        //warn
        list = messageMap.get(UserMessageLevel.WARN);
        assertNull(list);
        //error
        list = messageMap.get(UserMessageLevel.ERROR);
        assertEquals(3, list.size());
        //fatal
        list = messageMap.get(UserMessageLevel.FATAL);
        assertNull(list);

        // test 'p2' node
        platform = doc.getElementById("p2");
        messageMap = UserMessage.getPlatformUserMessages(platform);
        //info
        list = messageMap.get(UserMessageLevel.INFO);
        assertEquals(3, list.size());
        //error
        list = messageMap.get(UserMessageLevel.ERROR);
        assertEquals(5, list.size());

        // test 'p3' node
        platform = doc.getElementById("p3");
        messageMap = UserMessage.getPlatformUserMessages(platform);
        //info
        list = messageMap.get(UserMessageLevel.TRACE);
        assertEquals(4, list.size());
        //error
        list = messageMap.get(UserMessageLevel.ERROR);
        assertEquals(8, list.size());
    }

    public void testUserMessageThreshold() throws Exception{
        String[] xmlStrings = {xmlFatal,xmlError,xmlWarn};
        UserMessageLevel[] thresholds = {UserMessageLevel.FATAL,UserMessageLevel.ERROR,UserMessageLevel.WARN};
        int[] expectedMessageCounts = {3,4,2};
        
        for(int i = 0; i < 3; i++){
            int expectedCountOfUserMessagesOfThresholdLevel = expectedMessageCounts[i];
            int actualCountOfUserMessagesOfThresholdLevel = -1;
            UserMessageLevel threshold = thresholds[i];
            Document doc = Utils.string2xml(xmlStrings[i]);
            SortedMap<UserMessageLevel, List<UserMessage>> messageMap = UserMessage.getAllUserMessages(doc);
            for(Iterator<Entry<UserMessageLevel, List<UserMessage>>> iterator = messageMap.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<UserMessageLevel, List<UserMessage>> m = iterator.next();
                UserMessageLevel key = m.getKey();
                if(key.value() >= threshold.value()){
                    List<UserMessage> list = m.getValue();
                    actualCountOfUserMessagesOfThresholdLevel = list.size();
                }
            }
            assertEquals(actualCountOfUserMessagesOfThresholdLevel,expectedCountOfUserMessagesOfThresholdLevel);
        }
    }
}
