package com.example.user.goeat_3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.List;


public class DBConnector {

    public static String executeQuery(String...query_string) {
        String result = "";
        try {

            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost("http://192.168.43.252:80/GOEAT.php");
            HttpPost httpPost = new HttpPost("http://34.80.250.76/GOEAT.php");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("query_type", query_string[0]));
            for(int i=1;i<query_string.length;i++){

                params.add(new BasicNameValuePair("value"+Integer.toString(i), query_string[i]));
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
    public static class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

        //宣告，儲存要顯示的資料
        private List<String> mDataSet;

        // 建構式，用來接收外部程式傳入的項目資料。
        public RecordAdapter(List<String> data) {
            mDataSet = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_record, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.item.setText(mDataSet.get(i));
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView item;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                item = itemView.findViewById(R.id.item);
            }
        }
    }
}
