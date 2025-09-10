package edu.eci.arep;

import edu.eci.arep.server.HttpRequest;
import edu.eci.arep.server.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import static edu.eci.arep.helpers.Converter.convertErrorMessage;
import static edu.eci.arep.helpers.Converter.convertResponse;

public class Facade {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:36000";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean running = true;
        while(running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine="";
            URI uri = null;
            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if(firstLine){
                    try {
                        uri = new URI(inputLine.split(" ")[1]);
                    } catch (URISyntaxException e) {
                        System.exit(1);
                    }
                    firstLine = false;
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = manageRequest(uri);
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();

    }
    public static String manageRequest(URI uri){
        HttpRequest req = new HttpRequest(uri);
        HttpResponse resp = new HttpResponse();
        try{
            System.out.println(req.getRoute());
            if(req.getRoute().startsWith("/cliente")){
                String file = sendFile();
                return file;
            }
            else if(req.getRoute().startsWith("/add") || req.getRoute().startsWith("/list") || req.getRoute().startsWith("/stats") || req.getRoute().startsWith("/clear")){
                resp = manageGetResponse(req);
            }
        }catch(ConnectException ex) {
            resp = resp.statusCode(502).statusMessage("Bad Gateway").body(convertErrorMessage(502, "backend_unreachable")).headers("Content-type", "application/json");
        }
        catch(IOException e){
            resp = resp.statusCode(500).statusMessage("Internal Server Error").body(convertErrorMessage(500, e.getMessage())).headers("Content-type", "application/json");
        }

        return convertResponse(resp);
    }
    public static HttpResponse manageGetResponse(HttpRequest req) throws IOException {
        HttpResponse res = new HttpResponse();
        System.out.println(req.getURI());
        URL obj = new URL(GET_URL+ req.getURI());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        String statusMessage = con.getResponseMessage();
        System.out.println("GET Response Code :: " + responseCode);
        System.out.println("GET Response Code :: " + statusMessage);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        res.statusCode(responseCode).statusMessage(statusMessage).body(response.toString()).headers("Content-type","application/json");
        return res;
    }
    public static String sendFile(){
        return "HTTP/1.1 200 OK \r\n"
                + "Content-type: text/html\r\n"
                + "\r\n"
                +
                """
                <!DOCTYPE html>
                <html>
                                        
                <head>
                    <title>Form Example</title>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                                        
                <body>
                    <h1>Form with GET</h1>
                    <form action="/add">
                        <label for="add">Add method:</label><br>
                        <input type="text" id="add" name="add"><br><br>
                        <input type="button" value="Submit" onclick="loadGetMsg()">
                    </form>
                    <div id="getrespmsg"></div>
                    <form action="/list">
                        <label for="list">List Method:</label><br>
                        <input type="button" value="Submit" onclick="loadGetMsg2()">
                    </form>
                    <div id="getrespmsg2"></div>
                    <form action="/stats">
                        <label for="stats">Stats Method:</label><br>
                        <input type="button" value="Submit" onclick="loadGetMsg3()">
                    </form>
                    <div id="getrespmsg3"></div>
                    <form action="/clear">
                        <label for="clear">Clear Method:</label><br>
                        <input type="button" value="Submit" onclick="loadGetMsg4()">
                    </form>
                    <div id="getrespmsg4"></div>            
                    <script>
                        function loadGetMsg() {
                            let nameVar = document.getElementById("add").value;
                            const xhttp = new XMLHttpRequest();
                            xhttp.onload = function () {
                                document.getElementById("getrespmsg").innerHTML =
                                    this.responseText;
                            }
                            xhttp.open("GET", "/add?x=" + nameVar);
                            xhttp.send();
                        }
                        function loadGetMsg2() {
                            const xhttp = new XMLHttpRequest();
                            xhttp.onload = function () {
                                document.getElementById("getrespmsg2").innerHTML =
                                    this.responseText;
                            }
                            xhttp.open("GET", "/list");
                            xhttp.send();
                        }
                        function loadGetMsg3() {
                            const xhttp = new XMLHttpRequest();
                            xhttp.onload = function () {
                                document.getElementById("getrespmsg3").innerHTML =
                                    this.responseText;
                            }
                            xhttp.open("GET", "/stats");
                            xhttp.send();
                        }
                        function loadGetMsg4() {
                            const xhttp = new XMLHttpRequest();
                            xhttp.onload = function () {
                                document.getElementById("getrespmsg4").innerHTML =
                                    this.responseText;
                            }
                            xhttp.open("GET", "/clear");
                            xhttp.send();
                        }
                    </script>
                                        
                </body>
                                        
                </html>
                        
                """;
    }
}
