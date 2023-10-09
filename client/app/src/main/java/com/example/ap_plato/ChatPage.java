package com.example.ap_plato;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

class Message{
    String username;
    String msg;
    String date;
    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", msg='" + msg + '\'' +
                ", date=" + date +
                '}';
    }
//    public Message(String username, String msg) {
//        this.username = username;
//        this.msg = msg;
//    }

    public Message(String username, String msg, String date) {
        this.username = username;
        this.msg = msg;
        this.date = date;
    }
}
public class ChatPage extends AppCompatActivity {
    static ArrayList<Message> messages;
    static RecyclerView recyclerView = null;
    static RecyclerView.Adapter recyclerviewAdapter = null;
    static RecyclerView.LayoutManager recyclerViewLayoutManger = null;
    static String otherSideUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        ChatPage.otherSideUsername = getIntent().getStringExtra("username");
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    messages = new ArrayList<>();
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetMSG");
                    dataOutputStream.writeUTF(MainActivity.username+"->"+ChatPage.otherSideUsername);
                    Integer size =  Integer.parseInt(dataInputStream.readUTF());
                    for (int i = 0; i < size; i++) {
                        String uname = dataInputStream.readUTF();
                        String msg  = dataInputStream.readUTF();
                        String date = dataInputStream.readUTF();
                        ChatPage.messages.add(new Message(uname , msg , date));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChatPage.recyclerviewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        de.hdodenhof.circleimageview.CircleImageView imageView = toolbar.findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        textView.setText("Chat with : " + ChatPage.otherSideUsername);
        textView.setClickable(false);
        setSupportActionBar(toolbar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatPage.this , MainPage.class));
                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            }
        });
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.chat_list);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManger = new LinearLayoutManager(this);
        recyclerviewAdapter = new MessageAdapter(ChatPage.messages);
        recyclerView.setLayoutManager(recyclerViewLayoutManger);
        recyclerView.setAdapter(recyclerviewAdapter);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress , MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new   DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("SetSocket");
                    while (true){
                        String uname = dataInputStream.readUTF();
                        String msg  = dataInputStream.readUTF();
                        String date = dataInputStream.readUTF();
                        Message message = new Message(uname , msg , date);
                        ChatPage.messages.add(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ChatPage.recyclerviewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }
    public void sendMessage(View view){
        final EditText message = findViewById(R.id.messageText);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress , MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("SendMSG");
                    dataOutputStream.writeUTF(MainActivity.username);
                    dataOutputStream.writeUTF(ChatPage.otherSideUsername);
                    dataOutputStream.writeUTF(message.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }
}
