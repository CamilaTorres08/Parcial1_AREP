package edu.eci.arep.helpers;

import edu.eci.arep.server.HttpResponse;

import java.util.Map;

public class Converter {
    public static double convertToDouble(String number){
        return Double.parseDouble(number);
    }
    public static String convertResponse(HttpResponse res){
        res.headers("Content-type","application/json");
        String headersResp = "";
        for(Map.Entry<String,String> entry : res.getHeaders().entrySet()){
            headersResp += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        String resResult = "HTTP/1.1 "+res.getCode() + " " + res.getMessage() + "\r\n"
                + headersResp
                + "\r\n";
        if(!res.getBody().isEmpty()){
            resResult += res.getBody();
        }
        return resResult;
    }
    public static String convertErrorMessage(int status, String error){
        return "{"
                + "\"status\": " + status
                +", "
                + "\"error\": " + error
                + "}";
    }
}
