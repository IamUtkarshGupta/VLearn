package com.example.vlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlearn.PersonalInfo.UpdateInfo;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class interests extends AppCompatActivity { //to select user topics

    static HashMap<String,Character> TopicToKey;    //Maps topic to key
    public StringBuffer Interest_Str = new StringBuffer();
    public String operation;
    TextView tc,tnm;
    RelativeLayout rl;

    static{
        TopicToKey = new HashMap<>();
        TopicToKey.put("Physics",'A');
        TopicToKey.put("Maths",'B');
        TopicToKey.put("Computer",'C');
        TopicToKey.put("Science",'D');
        TopicToKey.put("Politics",'E');
        TopicToKey.put("Business",'F');
        TopicToKey.put("Technology",'G');
        TopicToKey.put("Sports",'H');
        TopicToKey.put("Movies",'I');
        TopicToKey.put("Music",'J');
    }



    MyCustomAdapter dataAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_interests);
        Intent i=getIntent();
        operation=i.getStringExtra("operation");
        tc=findViewById(R.id.profileion);
        String nm=USER_Class.getLoggedUserName();
        Character a=nm.charAt(0);
        tc.setText(a.toString());
        tnm=findViewById(R.id.myname);
        tnm.setText(nm);
        rl=findViewById(R.id.intlay);

        displayListView();

        checkButtonClick();

    }
    private void displayListView() {

        //ArrayList of countries
        ArrayList<Topics> interestList = new ArrayList<Topics>();
        Topics interestItem = new Topics("Physics",false);
        interestList.add(interestItem);
        interestItem = new Topics("Maths",false);
        interestList.add(interestItem);
        interestItem = new Topics("Computer",false);
        interestList.add(interestItem);
        interestItem = new Topics("Science",false);
        interestList.add(interestItem);
        interestItem = new Topics("Politics",false);
        interestList.add(interestItem);
        interestItem = new Topics("Business",false);
        interestList.add(interestItem);
        interestItem = new Topics("Technology",false);
        interestList.add(interestItem);
        interestItem = new Topics("Sports",false);
        interestList.add(interestItem);
        interestItem = new Topics("Movies",false);
        interestList.add(interestItem);
        interestItem = new Topics("Music",false);
        interestList.add(interestItem);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.topic_info, interestList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                com.example.vlearn.Topics country = (com.example.vlearn.Topics) parent.getItemAtPosition(position);
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<com.example.vlearn.Topics> {

        private ArrayList<com.example.vlearn.Topics> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<com.example.vlearn.Topics> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<com.example.vlearn.Topics>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {

            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.topic_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        com.example.vlearn.Topics country = (com.example.vlearn.Topics) cb.getTag();
                        Snackbar snackbar=Snackbar.make(findViewById(R.id.intlay),cb.getText() + " is selected",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            com.example.vlearn.Topics country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(interests.this);
                builder.setTitle("Confirm Update !");
                builder.setMessage("You are about to update your Topics. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer responseText = new StringBuffer();

                        responseText.append("The following were selected...\n");

                        ArrayList<Topics> countryList = dataAdapter.countryList;
                        for(int i=0;i<countryList.size();i++){
                            Topics topics = countryList.get(i);
                            if(topics.isSelected()){
                                responseText.append("\n" + topics.getName());
                                String str=topics.getName();
                                String key=TopicToKey.get(str).toString();
                                Interest_Str.append(key);
                            }
                        }
                        new interests.BackgroundTask().execute();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Topics not Updated", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();


            }
        });

    }
    class BackgroundTask extends AsyncTask<Void,Void,String>        //this class is background task performing which will connect to database
    {


        @Override
        protected String doInBackground(Void... voids) {

            try {String interest_url;
                if(false)
                {
                    interest_url="https://vlearndroidrun.000webhostapp.com/updateUserTopics.php";
                }else{
                    interest_url="https://vlearndroidrun.000webhostapp.com/addTopics.php";
                }
                URL url=new URL(interest_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("User_Id","UTF-8")+"="+URLEncoder.encode(USER_Class.getLoggedUserId(),"UTF-8")+"&"+
                        URLEncoder.encode("User_Topics","UTF-8")+"="+URLEncoder.encode(Interest_Str.toString(),"UTF-8");
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

            try {
                if (result.equals("Posted Successfully")) {
                    Intent i = new Intent(interests.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }catch (Exception e){

                Snackbar snackbar = Snackbar.make(rl, "No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
