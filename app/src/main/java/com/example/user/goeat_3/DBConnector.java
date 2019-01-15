package com.example.user.goeat_3;

import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class DBConnector {

    public static String executeQuery(String...query_string) {
        String result = "";
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.120:8080/GOEAT.php");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.e("abc",query_string[0]);
            params.add(new BasicNameValuePair("query_type", query_string[0]));
            if(query_string.length>1){
                params.add(new BasicNameValuePair("query_value", query_string[1]));
                Log.e("log_tag","A"+query_string[1]+query_string.length);

            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;

            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();

            result = builder.toString();

        } catch(Exception e) {
            //Log.e("log_tag", e.toString());
        }
        Log.e("abc","回傳成功");
        return result;
    }
}
