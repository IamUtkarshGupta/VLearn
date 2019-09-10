package com.example.vlearn.PersonalInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlearn.Admin.PostWaitDetail;
import com.example.vlearn.R;

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

public class EditPost extends AppCompatActivity {

    String posty,post_title,post_id,user_id;
    TextView title;
    EditText editPost;
    Button btnEditPost,btnDelPost;
    String FLAG;
    String json_string,JSON_String;                 //this activity is showing answer of particular question and giving option to add answer
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Intent i=getIntent();
        posty=i.getStringExtra("Post");
        post_title=i.getStringExtra("Post_Title");
        post_id=i.getStringExtra("Post_Id");
        user_id=i.getStringExtra("User_Id");



        title=findViewById(R.id.tvEditTitle);
        editPost=findViewById(R.id.tvEditPost);
        btnEditPost=findViewById(R.id.btnEditpost);
        btnDelPost=findViewById(R.id.btnDelpost);

        title.setText(post_title);
        editPost.setText(posty);



        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG="1";
                posty=editPost.getText().toString().trim();
                Toast.makeText(EditPost.this,posty+" "+post_id+ " postid",Toast.LENGTH_SHORT).show();

                new EditPost.BackgroundTask().execute();


            }
        });

        btnDelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG="0";
                new EditPost.BackgroundTask().execute();

            }
        });










    }


    public void getDatafromJSON()
    {
        //Toast.makeText(PostDetail.this,"hio"+JSON_String,Toast.LENGTH_LONG).show();
        String status;


        try {
            jsonObject=new JSONObject(JSON_String);

            int count=0;
            jsonArray=jsonObject.getJSONArray("server_response");

            while(count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);
                status=jo.getString("status");
                Log.d("status",status);
                //p_comment=jo.getString("Comment");


                count++;


            }
         //   dialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        String json_url="https://vlearndroidrun.000webhostapp.com/editDeletePost.php"; //delrte



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
                String data= URLEncoder.encode("Post_Id","UTF-8")+"="+URLEncoder.encode(post_id,"UTF-8")+"&"+
                        URLEncoder.encode("Post","UTF-8")+"="+URLEncoder.encode(posty,"UTF-8")+"&"+
                        URLEncoder.encode("Flag","UTF-8")+"="+URLEncoder.encode(FLAG,"UTF-8");
                Log.d("up   :    ",data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();


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

               /// return FLAG;

                // httpURLConnection.disconnect();

                //return "successfully deleted";

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


          //  Toast.makeText(PostWaitDetail.this,success+result,Toast.LENGTH_SHORT).show();

            getDatafromJSON();

            //super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }





}
