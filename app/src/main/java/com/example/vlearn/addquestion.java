package com.example.vlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import dmax.dialog.SpotsDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import static java.lang.Character.toUpperCase;

public class addquestion extends AppCompatActivity {
    TextView prof_icon;
    EditText Q_txt;
    Button b_Post;
    String QTxt;
    SpotsDialog dialog;
    USER_Class User_obj;//unused object
    ImageButton btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);

        Q_txt=findViewById(R.id.Q_txt);
        b_Post=findViewById(R.id.Q_post);
        prof_icon=findViewById(R.id.prof_icon);
        btn_close=findViewById(R.id.close_btn);
        dialog=new SpotsDialog(this);

        String username=USER_Class.getLoggedUserName();
        Character name=username.charAt(0);
        name=toUpperCase(name);
        prof_icon.setText(name.toString());

        Toast.makeText(addquestion.this,"user"+USER_Class.getLoggedUserId(),Toast.LENGTH_SHORT).show();

        b_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                QTxt=Q_txt.getText().toString().trim();
                PostQuestion();                 // post question to database
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
    public void PostQuestion()
    {

        if(!QTxt.equals(""))
        {
            //post question
            new addquestion.BackgroundTask().execute();


        }else{
            Toast.makeText(addquestion.this,"Enter Your Question",Toast.LENGTH_SHORT).show();
        }
    }

    class BackgroundTask extends AsyncTask<Void,Void,String>        //this class is background task performing which will connect
                                                                    //app to our database(php script of required) and then php script
                                                                    // will run our queryand make changes in database
    {
        String addQuestion_url="https://vlearndroidrun.000webhostapp.com/addQuestion.php";



        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url=new URL(addQuestion_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("User_Id","UTF-8")+"="+URLEncoder.encode(USER_Class.getLoggedUserId(),"UTF-8")+"&"+
                        URLEncoder.encode("Q_text","UTF-8")+"="+URLEncoder.encode(QTxt,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS=httpURLConnection.getInputStream();
                IS.close();
                return "Posted Successfully";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "jgfksg";
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

            //JSON_String=result;
            dialog.dismiss();
            /*QuesFragment q=new QuesFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.beginTransaction(R.id.fragment_container,q);
            fragmentTransaction.commit();*/
            //Intent i=new Intent(addquestion.this,QuesFragment.class);
            //startActivity(i);

             Toast.makeText(addquestion.this,result,Toast.LENGTH_LONG).show();
            //getDatafromJSON();
           // Intent i=new Intent(login.this,MainActivity.class);
            //startActivity(i);
            //super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
