package com.example.vlearn.chatWithAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlearn.R;
import com.example.vlearn.USER_Class;
import com.example.vlearn.adapter.MessageAdapter;
import com.example.vlearn.object.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chatWithAdmin extends AppCompatActivity {

    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    EditText text_send;
    ImageView btn_send;
    MessageAdapter messageAdapter;
    List<chat> mchat;
    RecyclerView recyclerView;
    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_admin);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        username=findViewById(R.id.uname);
        text_send=findViewById(R.id.text_send);
        //prof=findViewById(R.id.profile);
        btn_send=findViewById(R.id.btn_send);


        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // User user=dataSnapshot.getValue(User.class);//problem here
                //username.setText(user.getUsername());
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    chatUser user=snapshot.getValue(chatUser.class);
                    Log.d("user",user.getUsername());
                    String u=user.getUsername();
                    if(u.equals("adminadmin"))
                    {
                        readMessage(fuser.getUid(),user.getId());
                        username.setText("adminadmin");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=text_send.getText().toString().trim();
                sendMessage(fuser.getUid(),user_id,text);
                text_send.setText(" ");
            }
        });
    }
    private  void sendMessage(String sender,String reciever,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid,final String userid){
        user_id=userid;
        // Toast.makeText(this,userid+"    "+myid,Toast.LENGTH_SHORT).show();
        mchat =new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    chat Chat=snapshot.getValue(chat.class);
                    String s=Chat.getSender();
                    String r=Chat.getReciever();
                    Toast.makeText(chatWithAdmin.this,Chat.getMessage(),Toast.LENGTH_SHORT).show();
                    //break;
                    Log.d("here",userid+" "+myid);
                    Log.d("verify",r+" "+s);


                    if(r.equals(myid) && s.equals(userid)){
                        Log.d("yaha",r+" "+myid+" "+s+" "+userid);
                        mchat.add(Chat);
                        messageAdapter =new MessageAdapter(chatWithAdmin.this,mchat);
                        recyclerView.setAdapter(messageAdapter);
                    }
                    if(r.equals(userid) && s.equals(myid))
                    {
                        Log.d("yaha",r+" "+myid+" "+s+" "+userid);
                        mchat.add(Chat);
                        messageAdapter =new MessageAdapter(chatWithAdmin.this,mchat);
                        recyclerView.setAdapter(messageAdapter);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
