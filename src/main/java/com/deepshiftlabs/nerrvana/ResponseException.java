package com.deepshiftlabs.nerrvana;

/**
 * HTTP response exception<br>
 * It is raised when API returns HTTP status other than 200. 
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class ResponseException extends Exception{
  private static final long serialVersionUID = 1L;
  private int code = 0;
  
  public ResponseException(String msg){
    super(msg);
  }
  
  public ResponseException(String msg,int code){
    super(msg);
    this.code = code;
  }
  
  public int code(){return code;}
}