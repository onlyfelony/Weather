package com.example.administrator.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    String mess="北京";
    String news=null;
    TextView text;
    Button button;
    EditText ed;
    private static final int UPDATE_TEXT = 1;
    private Handler handler=new Handler(){
        @Override
    public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                    text.setText(news);break;
                default:break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       text= (TextView) findViewById(R.id.chaxun);
        button= (Button) findViewById(R.id.but);
        ed= (EditText) findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess=ed.getText().toString();
                sendRequestWithHttpURLConnection();
            }
        });

    }
    private void sendRequestWithHttpURLConnection(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    String cityname = URLEncoder.encode(mess, "UTF-8");
                    URL url=new URL("http://apis.baidu.com/apistore/weatherservice/citylist?cityname="+cityname);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setRequestProperty("apikey", "02912e7aeb058df39e053d930e9c9c92");
                    connection.connect();
                    Log.e("xu", "xxx");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        //  response.append("\r\n");
                    }
                    String x=response.toString();
                    Log.e("xu",x);
                    JSONObject jsonObject=new JSONObject(x);
                    String errMsg=jsonObject.getString("errMsg");
                    Log.e("xu",errMsg);
                    JSONArray jsonArray=jsonObject.getJSONArray("retData");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject  jso=jsonArray.getJSONObject(i);
                        String province_cn=jso.getString("province_cn");
                        String district_cn=jso.getString("district_cn");
                        String name_cn=jso.getString("name_cn");
                        String name_en=jso.getString("name_en");
                        String area_id=jso.getString("area_id");
                        Log.e("xu",province_cn);
                        Log.e("xu",district_cn);
                        Log.e("xu",name_cn);
                        Log.e("xu",name_en);
                        Log.e("xu",area_id);
                        news = news +province_cn+district_cn+name_cn+name_en+area_id;
                    }

                    Message message = new Message();
                    message.what = UPDATE_TEXT;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();


    }

}

