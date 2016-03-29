package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class JSMS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String mes = "Merhaba bu Java mesaji";
		String num = "5318430621";
		sending(mes,num);
	}
	public static void sending(String text,String number){
		String xml = "<request>";
        xml += "<authentication>";
        xml += "<username>5318430621</username>";
        xml += "<password>64722900</password>";
        xml += "</authentication>";
        xml += "<order>";
        xml += "<sender>ILETI MRKZI</sender>";
        xml += "<sendDateTime></sendDateTime>";
        xml += "<message>";
        xml += "        <text><![CDATA["+text+"]]></text>";
        xml += "        <receipents>";
        xml += "                <number>"+number+"</number>";
        xml += "        </receipents>";
        xml += "</message>";
        xml += "</order>";
        xml += "</request>";
		excutePost("http://api.iletimerkezi.com/v1/send-sms",xml);
	}
	public static String excutePost(String targetURL, String xml){
		
		String urlParameters = null;
		try {
			urlParameters = "data=" + URLEncoder.encode(xml,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    URL url;
	
	    HttpURLConnection connection = null;  
	
	    try {
	
	      //Create connection
	      url = new URL(targetURL);
	
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	      connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "UTF-8");  
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();
	      
	      //System.out.println(connection.getResponseMessage());
	      //Get Response
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	      String line;
	      StringBuffer response = new StringBuffer(); 
			
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	
	      rd.close();
	      return response.toString();
	      
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    } finally {
	    	if(connection != null) {
	    		connection.disconnect(); 
	    	}
	    }
	
	  }
	
}
