package edu.eci.arep.server;

import edu.eci.arep.classes.Calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import static edu.eci.arep.helpers.Converter.*;

public class HttpServer {
    static Calculator calculator = new Calculator();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
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
        HttpResponse res = new HttpResponse();
        try {
            if (req.getRoute().startsWith("/add")) {
                String[] param = uri.getQuery().split("=");
                if (param.length < 2) {
                    res = res.statusCode(400).statusMessage("Bad Request").body(convertErrorMessage(400, "Param value not set"));
                } else {
                    String numberString = req.getValue(param[0]);
                    if (numberString == null) {
                        res = res.statusCode(400).statusMessage("Bad Request").body(convertErrorMessage(400, "Param value not set"));
                    } else {
                        double number = convertToDouble(numberString.replace(" ", "".trim()));
                        double result = calculator.add(number);
                        String json = "{"
                                + "\"status\": " + "\"OK\"" + ", " + "\"added\": " + number + ", " + "\"count\": " + result + "}";
                        res = res.statusCode(200).statusMessage("OK").body(json);
                    }
                }
            } else if (req.getRoute().startsWith("/list")) {
                Object[] list = calculator.list();
                String listString = Arrays.toString(list);
                String json = "{"
                        + "\"status\": " + "\"OK\"" + ", " + "\"values\": " + listString + "}";
                res = res.statusCode(200).statusMessage("OK").body(json);

            } else if (req.getRoute().startsWith("/clear")) {
                calculator.clear();
                String json = "{"
                        + "\"status\": " + "\"OK\""
                        + ", "
                        + "\"message\": " + "\"list_cleared\""
                        + "}";
                res = res.statusCode(200).statusMessage("OK").body(json);

            } else if (req.getRoute().startsWith("/stats")) {
                int total = calculator.total();
                if(total <= 0){
                    res = res.statusCode(409).statusMessage("Conflict").body(convertErrorMessage(409, "empty_list"));
                }else{
                    Double median = calculator.median();
                    Double desv = calculator.estandarDesv();
                    if (median == null || desv == null) {
                        res = res.statusCode(400).statusMessage("Bad Request").body(convertErrorMessage(400, "Cannot divide by 0"));
                    } else {
                        String json = "{"
                                + "\"status\": " + "\"OK\"" + "," + "\"mean\": " + median + "," + "\"stddev\": " + desv + ", " + "\"count\": " + total
                                + "}";
                        res = res.statusCode(200).statusMessage("OK").body(json);
                    }
                }

            } else {
                res = res.statusCode(405).statusMessage("Method Not Allowed").body(convertErrorMessage(405, "Request does not exist"));
            }
        }catch (Exception e){
            res = res.statusCode(500).statusMessage("Internal Server Error").body(convertErrorMessage(500,e.getMessage())).headers("Content-type","application/json");
        }finally {
            return convertResponse(res);
        }
    }
}
