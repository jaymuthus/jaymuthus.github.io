

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NexmoVoiceServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NexmoVoiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


/**
* @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
*/
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub
String charset = "UTF-8";
PrintWriter out = response.getWriter();
String query = String.format("api_key=9e82a8b4&api_secret=5f1792d518050bd4&country=US&msisdn=12673684844&moHttpUrl=https://jaymuthus.github.io/voicexml.vxml&moSmppSysType=inbound&&voiceCallbackType=vxml&voiceCallbackValue=https://jaymuthus.github.io/voicexml.vxml", 
    URLEncoder.encode("api_key", charset),
    URLEncoder.encode("api_secret", charset), 
    URLEncoder.encode("country", charset), 
    URLEncoder.encode("msisdn", charset),
    URLEncoder.encode("moHttpUrl", charset),
    URLEncoder.encode("moSmppSysType",charset),
    URLEncoder.encode("voiceCallbackType",charset),
    URLEncoder.encode("voiceCallbackValue",charset));
HttpURLConnection con = (HttpURLConnection) new URL("https://rest.nexmo.com/number/update").openConnection();
System.out.println(query);
con.setDoOutput(true);
con.setRequestMethod("POST");
con.setRequestProperty("Accept", "application/json");
con.setRequestProperty("Accept-Charset", charset);
con.setRequestProperty("Content-Length", "0");
con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
try {
    System.out.println("Connecting...");
con.getOutputStream().write(query.getBytes(charset));


out.println("<h2>" + con.getResponseCode() + "</h2>");
System.out.println(con.getResponseCode());


/*BufferedReader br = null;
if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
    br = new BufferedReader(new InputStreamReader((con.getInputStream())));
} else {
    br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
}
String output;
StringBuilder sb = new StringBuilder();
while ((output = br.readLine()) != null) {
sb.append(output);
}
PrintWriter out = response.getWriter();
System.out.print( sb.toString());*/
}
finally {
con.getOutputStream().close();
}
}

/**
* @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
*/
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub
doGet(request, response);
}

}