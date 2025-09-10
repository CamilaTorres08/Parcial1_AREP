package edu.eci.arep.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    int code = 200;
    String message = "OK";
    String body = "";

    Map<String,String> headers;

    public HttpResponse(){
        headers = new HashMap<>();
    }
    public HttpResponse statusCode(int status){
        this.code = status;
        return this;
    }
    public HttpResponse statusMessage(String statusMessage){
        this.message = statusMessage;
        return this;
    }

    public HttpResponse body(String bodyMessage){
        this.body = bodyMessage;
        return this;
    }

    public HttpResponse headers(String name, String val){
        headers.put(name,val);
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
