/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deepshiftlabs.nerrvana;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author wise
 */
public class SignatureTest {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -cp nerrvana-plugin.jar com.deepshiftlabs.nerrvana.SignatureTest <path to plugin config> [optional - config file charset]");
            System.exit(0);
        }

        String filename = args[0];
        String charset = "ASCII";
        if(args.length > 1){
            charset = args[1];
        }
        
        InputStream in = new FileInputStream(args[0]);
        StringBuilder sb = new StringBuilder();
        InputStreamReader inR = new InputStreamReader(in, charset);
        BufferedReader buf = new BufferedReader(inR);
        String line;
        while ((line = buf.readLine()) != null) {
            sb.append(line);
        }
        String settingsXmlString = sb.toString();
        NerrvanaPluginSettings settings = NerrvanaPluginSettings.parse(Utils.string2xml(settingsXmlString));
        System.out.println("API key:\t"+settings.apikey);
        System.out.println("Secret key: "+settings.secretkey);
        HttpCommunicator comm = new HttpCommunicator(settings);
        
        sb = new StringBuilder();
        for (Iterator<Platform> it = settings.platforms.iterator(); it.hasNext();) {
            if (sb.length() > 0){
                sb.append(",");
            }
            sb.append(it.next().code);
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("space_id", settings.space_id));
        params.add(new BasicNameValuePair("name", settings.test_run_name));
        params.add(new BasicNameValuePair("description", settings.test_run_descr));
        params.add(new BasicNameValuePair("platforms", sb.toString()));
        params.add(new BasicNameValuePair("executable_file", settings.executable_file));
        params.add(new BasicNameValuePair("validation", "0"));
        params.add(new BasicNameValuePair("nodes_count", "" + settings.nodes_count));
        params.add(new BasicNameValuePair("apikey", settings.apikey));

        System.out.println("---BEGIN PARAMETERS FOR SIGNATURE---");
        for (Iterator<NameValuePair> it = params.iterator(); it.hasNext();) {
            NameValuePair nvp = it.next();
            System.out.println(nvp.getName() + " => " + nvp.getValue());
        }
        System.out.println("-----END PARAMETERS FOR SIGNATURE---\n");
        
        String signature1 = comm.calculateSignature(params);
        String signature2 = comm.calculateSignature(params,charset);
        System.out.println("Signature for test run(old version): "+signature1);
        System.out.println("Signature for test run("+charset+" version): "+signature2);
    }
}
