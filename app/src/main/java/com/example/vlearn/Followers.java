package com.example.vlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vlearn.adapter.Lboard_adapter;
import com.example.vlearn.object.Lboard_user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Followers extends AppCompatActivity {

    private RecyclerView recyclerView;
    String json_string,JSON_String;                 //this activity is showing answer of particular question and giving option to add answer
    JSONArray jsonArray;
    JSONObject jsonObject;
    List<Lboard_user> leaderData;
    Lboard_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        recyclerView =findViewById(R.id.leader_board_recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new BackgroundTask().execute();
    }
    public void getDatafromJSON()
    {
        // Toast.makeText(PostDetail.this,"hio"+JSON_String,Toast.LENGTH_LONG).show();
        String user_name,user_email,user_id;
        leaderData=new ArrayList<>();

        try {
            jsonObject=new JSONObject(JSON_String);

            int count=0;
            jsonArray=jsonObject.getJSONArray("server_response");

            while(count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);
                user_name=jo.getString("UserName");
                user_email=jo.getString("Email");
                user_id=jo.getString("User_Id");

                Toast.makeText(Followers.this,"hi"+user_name+user_email,Toast.LENGTH_LONG).show();
                Lboard_user contacts=new  Lboard_user(user_name,user_email,user_id);
                leaderData.add(contacts);
                adapter = new Lboard_adapter(this, leaderData);
                recyclerView.setAdapter(adapter);
                count++;


            }
            //dialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class BackgroundTask extends AsyncTask<Void,Void,String>            //answer upload karega
    {
        String json_url="https://vlearndroidrun.000webhostapp.com/getUserFollowers.php";


        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                //my
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("User_Id","UTF-8")+"="+URLEncoder.encode(USER_Class.getLoggedUserId(),"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                //
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();

                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSON_String=stringBuilder.toString().trim();
                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        public BackgroundTask()
        {
            super();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            JSON_String=result;
            Toast.makeText(Followers.this,"gghg"+JSON_String,Toast.LENGTH_LONG).show();
            getDatafromJSON();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
