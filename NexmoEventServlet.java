import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NexmoEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NexmoEventServlet() {
        super();
    }

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
        
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        String nccoJson ="";
        System.out.println("********START*********");

        JSONObject jsonObject = null;
        try{
            jsonObject = getRequestJson(request);    
        } catch(ParseException ex) {
            System.out.println(ex);
        }
        

        String convId = (String)jsonObject.get("conversation_uuid");
        String dtmf = (String)jsonObject.get("dtmf");
        System.out.println ("Conv ID" + convId);
        System.out.println ("DTMF : " + dtmf);
        if(dtmf != null) {
            switch(dtmf) {
                case "1":
                    nccoJson = "[{";
                    nccoJson += "\"action\": \"talk\",";
                    nccoJson += "\"text\": \"Please wait while we transfer your call\"";
                    nccoJson += "},";
                    nccoJson += "{";
                    nccoJson += "\"action\": \"connect\",";
                    nccoJson += "\"endpoint\": [{ ";
                    nccoJson += "\"type\": \"phone\",";
                    nccoJson += "\"number\": \"14089817733\"";
                    nccoJson += "}]";
                    nccoJson += "}]";
                    break;
                case "2":
                    nccoJson = "[{";
                    nccoJson += "\"action\": \"talk\",";
                    nccoJson += "\"text\": \"Please wait while we transfer your call\"";
                    nccoJson += "},";
                    nccoJson += "{";
                    nccoJson += "\"action\": \"connect\",";
                    nccoJson += "\"endpoint\": [{ ";
                    nccoJson += "\"type\": \"phone\",";
                    nccoJson += "\"number\": \"14083686487\"";
                    nccoJson += "}]";
                    nccoJson += "}]";
                    break;
                default:
                    nccoJson = "[{";
                    nccoJson += "\"action\": \"talk\",";
                    nccoJson += "\"text\": \"Invalid input. For sales, press 1, for support, press 2\"";
                    nccoJson += "},";
                    nccoJson += "{";
                    nccoJson += "\"action\": \"input\"";                    
                    nccoJson += "}]";
            }

            out.println(nccoJson);
            System.out.println(nccoJson);
        }
    }

    private JSONObject getRequestJson(HttpServletRequest request) throws IOException, ParseException{
        StringBuffer jb = new StringBuffer();
        String line = null;

        BufferedReader br = request.getReader();
        while ((line = br.readLine()) != null) {
            jb.append(line);
        }

        System.out.println(jb.toString());

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jb.toString());
        return (JSONObject) obj;
    }
}
