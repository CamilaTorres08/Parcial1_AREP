package edu.eci.arep.server;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    URI uri;
    Map<String,String> params = new HashMap<>();
    public HttpRequest(URI u){
        uri = u;
        if(uri != null && uri.getQuery() != null) setParams();
    }
    private void setParams(){
        String[] paramsVal = uri.getQuery().split("&");
        for(String val : paramsVal){
            String[] param = val.split("=");
            if(param.length == 2){
                params.put(param[0],param[1]);
            }
        }
    }
    public String getValue(String name){
        return params.get(name);
    }
    public String getRoute(){
        return uri.getPath();
    }
    public String getURI(){
        return uri.toString();
    }
}
