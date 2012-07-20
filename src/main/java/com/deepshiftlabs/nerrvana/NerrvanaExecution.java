package com.deepshiftlabs.nerrvana;

import java.util.ArrayList;
import org.w3c.dom.*;

/**
 * <p>Parses and wraps information about tests execution in Nerrvana.
 * @see HttpCommunicator#getExecutionStatus(NerrvanaExecution)</p>
 * <br>
 * This class expects to get following response from Nerrvana API:
 * <pre>
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
 *                  <browse_url>https://xxx.ftp.nerrvana.com/demo_space/_test_runs/Testrun-Name/2022_01_22_22_22_22/winxp_sp3_firefox_110/</browse_url>
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
 */
public class NerrvanaExecution{
  public String id;
  public String starttime;
  public String status;
  public ArrayList<Platform> platforms;

  public NerrvanaExecution(){}

  public String getId(){return id;}
  public String getStarttime(){return starttime;}
  public String getStatus(){return status;}
  public ArrayList<Platform> getPlatforms(){return platforms;}

  public static ArrayList<NerrvanaExecution> xml2list(Document doc) throws Exception{
    ArrayList<NerrvanaExecution> execs = new ArrayList<NerrvanaExecution>();
    NodeList nodeExecs = doc.getDocumentElement().getChildNodes();
    for(int i = 0; i < nodeExecs.getLength(); i++){
      Node n = nodeExecs.item(i);
      if(n.getNodeName().equals("execution"))
        execs.add(NerrvanaExecution.parseExecution(n));
    }
    return execs;
  }

  private static NerrvanaExecution parseExecution(Node parent) throws Exception{
    NodeList list = parent.getChildNodes();
    NerrvanaExecution exec = new NerrvanaExecution();
    for(int i = 0; i < list.getLength(); i++){
      Node n = list.item(i);
      if(n.getNodeName().equals("id"))
        exec.id = Utils.nodeValue(n);
      else if(n.getNodeName().equals("start_datetime"))
        exec.starttime = Utils.nodeValue(n);
      else if(n.getNodeName().equals("status"))
        exec.status = Utils.nodeValue(n);
      else if(n.getNodeName().equals("platforms"))
        exec.platforms = Platform.getPlatformList(n);
    }
    return exec;
  }

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("---BEGIN EXECUTION#").append(id).append("---\n")
    .append("\tStart time: ").append(starttime).append("\n")
    .append("\tStatus: ").append(status).append("\n");
    if(platforms != null){
      for(int i = 0; i < platforms.size(); i++)
        sb.append(platforms.get(i).toString());
    }
    sb.append("-----END EXECUTION#").append(id).append("---\n\n");
    return sb.toString();
  }
}