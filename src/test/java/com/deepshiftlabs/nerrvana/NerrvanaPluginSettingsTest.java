package com.deepshiftlabs.nerrvana;

import java.util.*;
//import org.junit.*;
import junit.framework.TestCase;;

public class NerrvanaPluginSettingsTest extends TestCase {
    //private NerrvanaPluginSettings settings;
    
    public void setUp(){
        Logger.init(System.out,"normal");
    }
    
    private NerrvanaPluginSettings settings() throws Exception{
        NerrvanaPluginSettings settings = new NerrvanaPluginSettings();
        settings.httpurl = "https://api.nerrvana.com";
        settings.apikey = "1111111-22222-3333-44444-55555555555";
        settings.secretkey = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";

        settings.nodes_count = 1;
        settings.test_run_name = "Test run name";
        settings.test_run_descr = "Test run description";
        
        settings.executable_file = "executable.sh";
        settings.validation = false;
        settings.platforms = new ArrayList<Platform>();
        settings.platforms.add(new Platform(null,"winxp_sp3_firefox_110","Firefox 11.0 (WinXP)",null,null));

        settings.folder_with_tests = "src";
        settings.space_id = "123";
        settings.space_name = "space_name";
        settings.space_path = "space_name/some_path";
        settings.ftpsurl = "ftp.nerrvana.com";
        settings.ftpsuser = "ftp_name";
        settings.ftpspass = "ftp_path";

        settings.maxtime = 3600;
        settings.poll = 60;
        return settings;
    }

    public void testHttpSettings() throws Exception{
        String s = null;
        NerrvanaPluginSettings settings = settings();
        //valid settings
        assertTrue(settings.checkSettings());
        
        //no http url
        s = settings.httpurl; settings.httpurl = null;
        assertFalse(settings.checkSettings()); settings.httpurl = s;
        
        //no apikey
        s = settings.apikey; settings.apikey = null;
        assertFalse(settings.checkSettings()); settings.apikey = s;
        
        //no secretkey
        s = settings.secretkey; settings.secretkey = null;
        assertFalse(settings.checkSettings()); settings.secretkey = s;
    }

    public void testFtpSettings() throws Exception{
        String s = null;
        NerrvanaPluginSettings settings = settings();
        //valid settings
        assertTrue(settings.checkSettings());
        
        //no ftpurl
        s = settings.ftpsurl; settings.ftpsurl = null;
        assertFalse(settings.checkSettings()); settings.ftpsurl = s;
        
        //no space_name
        s = settings.space_name; settings.space_name = null;
        assertFalse(settings.checkSettings()); settings.space_name = s;
        
        //no space_path
        s = settings.space_path; settings.space_path = null;
        assertFalse(settings.checkSettings()); settings.space_path = s;
    }

    public void testSpaceSettings() throws Exception{
        String s = null;
        NerrvanaPluginSettings settings = settings();
        //valid settings
        assertTrue(settings.checkSettings());
        
        //no space_id
        s = settings.space_id; settings.space_id = null;
        assertFalse(settings.checkSettings()); settings.space_id = s;
        
        //no space_name
        s = settings.space_name; settings.space_name = null;
        assertFalse(settings.checkSettings()); settings.space_name = s;
        
        //no space_path
        s = settings.space_path; settings.space_path = null;
        assertFalse(settings.checkSettings()); settings.space_path = s;
    }
}
