package com.chwilio.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TranslationService {

	private static String subscriptionKey = "71df830b23684636a5df44f89db22fed";

    private static String host = "https://api.cognitive.microsofttranslator.com";
    private static String tpath = "/translate?api-version=3.0";
    private static String dpath = "/detect?api-version=3.0";
    
    private String params;
    private String query;
    private String tQuery;
    private String queryLang;
    
    private HashMap<String, String> hashMap;
    
    public static class RequestBody {
        String Text;

        public RequestBody(String text) {
            this.Text = text;
        }
    }
    
    TranslationService(String query){
    	this.query = query;
    	this.hashMap = new HashMap<String, String>();
    }
    
    public static String Post(URL url, String content) throws Exception {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", content.length() + "");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        connection.setRequestProperty("X-ClientTraceId", java.util.UUID.randomUUID().toString());
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        byte[] encoded_content = content.getBytes("UTF-8");
        wr.write(encoded_content, 0, encoded_content.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }
    
    public String dPrettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
     
        JsonArray jarray = json.getAsJsonArray();
        JsonObject jobj = jarray.get(0).getAsJsonObject();
 
        return jobj.get("language").toString().replaceAll("\"", "");
    }
    
    public void tPrettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        JsonArray jArray = json.getAsJsonArray();
        JsonObject jObj = jArray.get(0).getAsJsonObject();
 
        json = jObj.get("translations");
        jArray = json.getAsJsonArray();
        
        hashMap.put(queryLang, query);
        
        for (JsonElement jElem : jArray) {
        	jObj = jElem.getAsJsonObject();
        	hashMap.put(jObj.get("to").toString().replaceAll("\"", ""), jObj.get("text").toString().replaceAll("\"", ""));
        }
    }
    
    public void detectLang(String content) throws Exception {
        URL url = new URL (host + dpath);
        queryLang = dPrettify(Post(url, content));
    }
    
    public String translateQuery() throws Exception {
    	List<RequestBody> objList = new ArrayList<RequestBody>();
        objList.add(new RequestBody(query));
        String content = new Gson().toJson(objList);

    	detectLang(content);
    	
    	if(queryLang.equals("en")) {
    		params = "&to=es&to=hi&to=fr&to=th";
    	}else if(queryLang.equals("es")){
    		params = "&to=en&to=hi&to=fr&to=th";
    	}else if(queryLang.equals("fr")){
    		params = "&to=es&to=hi&to=en&to=th";
    	}else if(queryLang.equals("hi")){
    		params = "&to=es&to=en&to=fr&to=th";
    	}else if(queryLang.equals("th")){
    		params = "&to=es&to=hi&to=fr&to=en";
    	}else {
    		params = null;
    	}
    	
    	if(params != null) {
    		URL url = new URL (host + tpath + params);
            tPrettify(Post(url, content));
    	}
        
        if(!hashMap.isEmpty()) {
        	tQuery = URLEncoder.encode(hashMap.get("en"), "UTF-8") + " OR " + URLEncoder.encode(hashMap.get("es"), "UTF-8") 
        	    + " OR " + URLEncoder.encode(hashMap.get("fr"), "UTF-8") + " OR " + hashMap.get("hi") 
        	    + " OR " + hashMap.get("th");
        }else {
        	tQuery = null;
        }
        
        return tQuery;
    }
    
    public String translateText(String lang) throws Exception {
    	List<RequestBody> objList = new ArrayList<RequestBody>();
        objList.add(new RequestBody(query));
        String content = new Gson().toJson(objList);
        params = "&to=" + lang;
   
		URL url = new URL (host + tpath + params);
        tPrettify(Post(url, content));
        
        if(!hashMap.isEmpty()) {
        	return hashMap.get(lang);
        }
        
        return null;
    }
    
}
