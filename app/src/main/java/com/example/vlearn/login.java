package com.example.vlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.google.android.gms.common.api.Response;

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
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText userame,pass;
    Button Login,gotoRegister;
    String l_name="",l_pass="";
    public String LOGGED_USER_NAME;
    public String LOGGED_USER_EMAIL;
    public String LOGGED_USER_ID;

    public static final String LOGIN_URL="https://vlearndroidrun.000webhostapp.com/login.php";
    public static final String KEY_NAME="name";
    public static final String KEY_PASSWORD="password";
    public static final String LOGIN_SUCCESS="success";
    public static final String SHARED_PREF_NAME="tech";
    public static final String EMAIL_SHARED_PREF="name";
    public static final String LOGGEDIN_SHARED_PREF="loggedin";
    private boolean loggedIn=false;

    String json_string;
    String JSON_String;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String emyl;
    public String USER_ID,USER_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userame=findViewById(R.id.L_username);
        pass=findViewById(R.id.L_password);
        Login=findViewById(R.id.login);
        gotoRegister=findViewById(R.id.goto_register);
        emyl=userame.getText().toString().trim();

        l_name=userame.getText().toString();
        l_pass=pass.getText().toString();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(login.this,"hio"+emyl,Toast.LENGTH_LONG).show();

                loginmet();

            }
        });


        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login.this,register.class);
                LOGGED_USER_EMAIL=l_name;
                startActivity(i);
            }
        });
        

    }

    //RETRIEVE DETAILS
    public void getDatafromJSON()
    {
        Toast.makeText(login.this,"hio"+JSON_String,Toast.LENGTH_LONG).show();
       // String answer,user_Id;
        //String user_name,q_Id,ans_Id;
        //mquestionfetch =new ArrayList<>();

        try {
            jsonObject=new JSONObject(JSON_String);

            int count=0;
            jsonArray=jsonObject.getJSONArray("server_response");

            while(count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);
                USER_NAME=jo.getString("User_Name");
                USER_ID=jo.getString("User_Id");
                Toast.makeText(login.this,USER_NAME+" "+USER_ID,Toast.LENGTH_SHORT).show();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        String json_url="https://vlearndroidrun.000webhostapp.com/getDetails.php";



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
                String data= URLEncoder.encode("u_email","UTF-8")+"="+URLEncoder.encode(emyl,"UTF-8");
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

            Toast.makeText(login.this,JSON_String,Toast.LENGTH_LONG).show();
           getDatafromJSON();
            Intent i=new Intent(login.this,MainActivity.class);
            startActivity(i);
            //super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



    private void loginmet() {

        final String name = userame.getText().toString();
        emyl=name;
        final String password = pass.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                   // Toast.makeText(login.this,"fkng",Toast.LENGTH_LONG).show();
                    @Override
                    public void onResponse(String response) {



                        if(response.trim().equalsIgnoreCase(LOGIN_SUCCESS)){

                            SharedPreferences sharedPreferences = login.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                            editor.putString(EMAIL_SHARED_PREF, emyl);

                            editor.commit();

                            new login.BackgroundTask().execute();
                           /* Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);*/
                        }else{
                            Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams = new HashMap<>();
                prams.put(KEY_NAME, name);
                prams.put(KEY_PASSWORD, password);

                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
  /*  @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);
        if(loggedIn){
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
    }*/
}