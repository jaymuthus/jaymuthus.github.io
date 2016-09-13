

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NexmoAppServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NexmoAppServlet() {
        super();
    }

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String charset = "UTF-8";
        String api_key = "9e82a8b4";
        String api_secret = "5f1792d518050bd4";
        String appName = "NexmoDemoApp";
        String answerUrl = "https://jaymuthus.github.io/NCCO-voice.json"; //this  url should provide the NCCO json
        String eventUrl = "https://jaymuthus.github.io/IVR1.vxml";
        String baseUrl = "https://api.nexmo.com/v1/applications";

        String query = String.format("api_key="+api_key+"&api_secret="+api_secret+"&name="+appName+"&type=voice&answer_url="+answerUrl+"&event_url="+eventUrl, 
            URLEncoder.encode("api_key", charset),
            URLEncoder.encode("api_secret", charset), 
            URLEncoder.encode("name", charset), 
            URLEncoder.encode("type", charset),
            URLEncoder.encode("answer_url",charset));

        System.out.println(query);
        String responseJSON = getResponseJSON(baseUrl, query, 3600);
        out.println(responseJSON);
        System.out.println(responseJSON);
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

    private String getResponseJSON(String connectionUrl, String query, int timeout) {
        String responseJSON = "";
        String charset = "UTF-8";
        HttpURLConnection con = null;
        try {
            URL url = new URL(connectionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Content-length", "0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.setConnectTimeout(timeout);

            OutputStream out = null;
            try 
            {
                out = con.getOutputStream();
                out.write(query.getBytes(charset));

                System.out.println(con.getResponseCode());
            
                int status = con.getResponseCode();
                BufferedReader br;

                System.out.println ("Response Code : " + status);

               // if (status == HttpURLConnection.HTTP_OK) { //success
                 if (status == 201) { //success
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
                    System.out.println("-----ERROR------");
                }

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                responseJSON = response.toString();

            }
            finally {
                if(out != null) {
                    out.close();
                 }
            }

        } catch (IOException ex) {
               System.out.println("Error: " + ex);
        } finally {
            if (con != null) {
              try {
                  con.disconnect();
              } catch (Exception ex) {
                    System.out.println("Error: " + ex);
              }
            }
        }
    return responseJSON;
    }
}