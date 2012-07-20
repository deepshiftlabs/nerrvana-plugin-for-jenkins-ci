package com.deepshiftlabs.nerrvana;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Parses and wraps information about platform where tests are executed in Nerrvana.
 * @see HttpCommunicator#getExecutionStatus(NerrvanaExecution)</p>
 * @see NerrvanaExecution</p>
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class Platform{
  public String id;
  public String code;
  public String name;
  public String status;
  public String browse_url;

  public Platform(){}
  public Platform(String id,String code,String name,String status,String browse_url){
      this.id = id;
      this.code = code;
      this.name = name;
      this.status = status;
      this.browse_url = browse_url;
  }

  public String getId(){return id;}
  public String getCode(){return code;}
  public String getName(){return name;}
  public String getStatus(){return status;}
  public String getBrowseurl(){return browse_url;}
  public String getBrowsetitle(){
    return (browse_url == null || browse_url.indexOf("://") < 0) ? "Not available" : "-&gt; at Nerrvana &lt;-";
  }

  public static ArrayList<Platform> getPlatformList(Node parent) throws Exception{
    ArrayList<Platform> platforms = new ArrayList<Platform>();
    NodeList list = parent.getChildNodes();
    for(int i = 0; i < list.getLength(); i++){
      Node n = list.item(i);
      if(n.getNodeName().equals("platform"))
        platforms.add(Platform.parsePlatform(n));
    }
    return platforms;
  }

  private static Platform parsePlatform(Node parent) throws Exception{
    NodeList list = parent.getChildNodes();
    Platform platform = new Platform();
    for(int i = 0; i < list.getLength(); i++){
      Node n = list.item(i);
      String name = n.getNodeName();
      String value = Utils.nodeValue(n);
      if(name.equals("id"))
        platform.id = value;
      else if(name.equals("code"))
        platform.code = value;
      else if(name.equals("name"))
          platform.name = value;
      else if(name.equals("status"))
        platform.status = value;
      else if(name.equals("browse_url"))
        platform.browse_url = value;
    }
    return platform;
  }

  public String toString(){
    StringBuilder sb = new StringBuilder();
    String sid = id == null ? "[N/A]":id.toString();
    sb.append("\t---BEGIN PLATFORM#").append(sid).append("---\n")
    .append("\t\tContext: ").append(name).append("\n")
    .append("\t\tStatus: ").append(status).append("\n");
    if(browse_url != null && browse_url.length() > 0)
      sb.append("\t\tBrowse: ").append(browse_url).append("\n");
    sb.append("\t-----END PLATFORM#").append(sid).append("---\n\n");
    return sb.toString();
  }
}