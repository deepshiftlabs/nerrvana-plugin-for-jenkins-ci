package com.deepshiftlabs.nerrvana;

import hudson.model.*;
import java.io.Serializable;

/**
 * Jenkins uses this class and accompanying file com\deepshiftlabs\nerrvana\ReportAction\index.jelly
 * to create report link and report page 
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class ReportAction implements Action, Serializable {
  private static final long serialVersionUID = 1L;
  private AbstractBuild<?,?> build;
  private String space_name;
  private String space_path;
  private Testrun testrun;
  private NerrvanaExecution exec;
  
  public ReportAction(AbstractBuild<?,?> build,String space_name,String space_path,Testrun testrun,NerrvanaExecution exec){
    this.build = build;
    this.space_name = space_name;
    this.space_path = space_path;
    this.testrun = testrun;
    this.exec = exec;
    
    Logger.traceln("\n\n===BEGIN REPORT CONTENT===\n");
    Logger.traceln("\tSpace name: "+this.space_name+"\n");
    Logger.traceln("\tSpace path: "+this.space_path+"\n");
    Logger.traceln(this.testrun.toString());
    Logger.traceln(this.exec.toString());
    Logger.traceln("=====END REPORT CONTENT===\n");
    
  }

  public String getIconFileName(){return "/plugin/NerrvanaPlugin/icons/nerrvana.png";}
  public String getDisplayName(){return "Nerrvana Report for "+testrun.name;}
  public String getUrlName(){return testrun.id;}
  public String getSpaceName(){return space_name;}
  public String getSpacePath(){return space_path;}
  public Testrun getTestrun(){return testrun;}
  public NerrvanaExecution getExec(){return exec;}
  public AbstractBuild<?,?> getOwner(){return build;}
}