

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


public class NexmoAccountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String api_key = "9e82a8b4";
    private static final String api_secret = "5f1792d518050bd4";
    private static final String baseUrl = "https://rest.nexmo.com/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NexmoAccountServlet() {
        super();
    }

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String charset = "UTF-8";

        String query = String.format("api_key="+api_key+"&api_secret="+api_secret, 
            URLEncoder.encode("api_key", charset),
            URLEncoder.encode("api_secret", charset));

        String countrycode = "", phoneno="";
        String url="";
        String action = request.getParameter("q");

        switch(action) {
            case "numbers" :
                url = baseUrl + "account/Numbers" + "?"+query; 
                break;
            case "balance" :
                url = baseUrl + "account/get-balance" + "?"+query; 
                break;
            case "pricing" :
                countrycode = request.getParameter("country");
                url = baseUrl + "account/get-pricing/outbound" + "?"+query+"&country="+countrycode; 
                break;
            case "search" :
                //String countrycode = request.getParameter("country");
                url = baseUrl + "number/search" + "?"+query+"&country=US&size=100"; //+countrycode; 
                break;
            case "check":
                countrycode = request.getParameter("country");
                phoneno = request.getParameter("phoneno");
                url = baseUrl + "number/search" + "?"+query+"&country="+countrycode+"&pattern="+phoneno+"&search_pattern=1";
                break;
            case "buy" :
                countrycode = request.getParameter("country");
                phoneno = request.getParameter("phoneno");
                url = baseUrl + "number/buy";
                query = String.format("api_key="+api_key+"&api_secret="+api_secret+"&country="+countrycode+"&msisdn="+phoneno, 
                            URLEncoder.encode("api_key", charset),
                            URLEncoder.encode("api_secret", charset), 
                            URLEncoder.encode("country", charset), 
                            URLEncoder.encode("msisdn",charset));
                break;
        }
        System.out.println(url);
        String responseJSON ="";
        if (action.equals("buy")) {
            responseJSON = getResponseJSON(url, query, 3600);
        } else {
            responseJSON = getResponseJSON(url, 3600);
        }

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
    private String getResponseJSON(String connectionUrl,  int timeout) {
        String responseJSON = "";
        String charset = "UTF-8";
        HttpURLConnection con = null;
        try {
            URL url = new URL(connectionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
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
                BufferedReader br;

                System.out.println ("Response Code : " + status);

                if (status == HttpURLConnection.HTTP_OK) { //success
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
