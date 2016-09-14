

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
    private static final String API_KEY = "9e82a8b4";
    private static final String API_SECRET = "5f1792d518050bd4";
    private static final String BASE_URL = "https://api.nexmo.com/v1/applications";
       
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

        String action = request.getParameter("q");
        String query = String.format("api_key="+API_KEY+"&api_secret="+API_SECRET, 
            URLEncoder.encode("api_key", charset),
            URLEncoder.encode("api_secret", charset));
        String url = "";
        String responseJSON = "";
        String appId = "", appName = "", answerUrl = "", eventUrl = "";
        switch(action) {
            case "list" :
                url = BASE_URL + "?"+query; 
                responseJSON = getResponseJSON(url, "GET", 3600);
                break;
            case "create" :
                appName = request.getParameter("appName");
                answerUrl = request.getParameter("answerUrl"); //this url should provide the NCCO json
                eventUrl = request.getParameter("eventUrl"); //url to receive status update

                query = String.format("api_key="+API_KEY+"&api_secret="+API_SECRET+"&name="+appName+"&type=voice&answer_url="+answerUrl+"&event_url="+eventUrl, 
                    URLEncoder.encode("api_key", charset),
                    URLEncoder.encode("api_secret", charset), 
                    URLEncoder.encode("name", charset), 
                    URLEncoder.encode("type", charset),
                    URLEncoder.encode("answer_url",charset),
                    URLEncoder.encode("event_url",charset));

                responseJSON = getResponseJson(BASE_URL, "POST", query, 3600);
                break;
            case "update" :
                appId = request.getParameter("appId");
                appName = request.getParameter("appName");
                answerUrl = request.getParameter("answerUrl"); //this url should provide the NCCO json
                eventUrl = request.getParameter("eventUrl"); 

                query = String.format("api_key="+API_KEY+"&api_secret="+API_SECRET+"&name="+appName+"&type=voice&answer_url="+answerUrl+"&event_url="+eventUrl, 
                    URLEncoder.encode("api_key", charset),
                    URLEncoder.encode("api_secret", charset), 
                    URLEncoder.encode("name", charset), 
                    URLEncoder.encode("type", charset),
                    URLEncoder.encode("answer_url",charset),
                    URLEncoder.encode("event_url",charset));

                responseJSON = getResponseJson(BASE_URL+"/"+appId, "PUT", query, 3600);
                break;
            case "get" :
                appId = request.getParameter("appId");
                url = BASE_URL + "/" + appId + "?" + query; 
                responseJSON = getResponseJSON(url, "GET", 3600);
                break;
            case "delete" :
                appId = request.getParameter("appId");
                url = BASE_URL + "/" + appId + "?" + query; 
                responseJSON = getResponseJSON(url, "DELETE", 3600);
                break;
        }

        System.out.println(query);
        
        out.println(responseJSON);
        System.out.println(responseJSON);
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

    /**
    *For GET request
    */
    private String getResponseJSON(String connectionUrl, String requestType, int timeout) {
        String responseJSON = "";
        String charset = "UTF-8";
        HttpURLConnection con = null;
        try {
            URL url = new URL(connectionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestType);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Content-length", "0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.setConnectTimeout(timeout);

            
               con.connect();

                System.out.println(con.getResponseCode());
            
                int status = con.getResponseCode();
                BufferedReader br=null;

                System.out.println ("Response Code : " + status);

                if (status == 200) { //success
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else if (status == 204) {
                   responseJSON = "Application deleted.";
                } else {
                     br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
                    System.out.println("-----ERROR------");
                }

                String inputLine;
                StringBuffer response = new StringBuffer(); 

                if (br != null) {
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();

                    responseJSON = response.toString(); 
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

    /**
    *For POST request
    */
    private String getResponseJson(String connectionUrl, String requestType, String query, int timeout) {
        String responseJSON = "";
        String charset = "UTF-8";
        HttpURLConnection con = null;
        try {
            URL url = new URL(connectionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod(requestType);
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
