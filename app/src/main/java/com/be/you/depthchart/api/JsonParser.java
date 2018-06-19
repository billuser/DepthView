package com.be.you.depthchart.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by Bill on 2018/1/2.
 */

public class JsonParser {

    public JSONObject urlConnect(String urlString, HashMap<String, String> postDataParams){
        URL url;
        String response = "";
        InputStream inputStream = null;
        try {
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
//            SSLContext sslContext = prepareSelfSign(mContent);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");  //            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();

            os.close();

            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK){
                inputStream = conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                response = bufferedReader.readLine();
            }else {
                response = "Error,sendPostRequest() , responseCode = " + responseCode;
                Log.e("responseCode", "response " +response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("responseCode", "error01   " + e.getMessage());
//                return null;
        }
        //        Log.d(Tool.getTag(this),"response = " + response );
        try {
            return new JSONObject(URLDecoder.decode(response, "UTF-8" ));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("responseCode", "error02   " + e.getMessage());
            return null;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
//

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
//        Log.d(Tool.getTag(getA),"result.toString() = " + result.toString() );
        return result.toString();
    }

    public JSONObject urlConnectGet(String urlString, HashMap<String, String> getDataParams){

        URL url;
        String response = "";
        InputStream inputStream = null;
        String getParams = "";
        for(String key : getDataParams.keySet()){
            getParams += key + "=" + getDataParams.get(key) + "&";
        }
        getParams = getParams.substring(0, getParams.length() - 1);

        try {
            url = new URL("https://api.huobi.pro/v1/depth?symbol=btcusdt&type=step0");
            Log.e("asdasd", "url " +url);

            HttpURLConnection  conn = (HttpURLConnection ) url.openConnection();
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/Accept-Language:zh-cn");
//            SSLContext sslContext = prepareSelfSign(mContent);
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setUseCaches(false);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("GET");  //            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(false);
//            conn.setUseCaches(false);
//            conn.setAllowUserInteraction(false);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            os.close();

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.e("responseCode", " " +responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK){
                inputStream = conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                response = bufferedReader.readLine();
            }else {
                response = "Error,sendPostRequest() , responseCode = " + responseCode;
                Log.e("Exception", "response " +response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("asdasd", " " +e.getMessage());
//                return null;
        }
        //        Log.d(Tool.getTag(this),"response = " + response );
        try {
            return new JSONObject(URLDecoder.decode(response, "UTF-8" ));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("codeaaaa", "error02   " + e.getMessage());
            return null;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}