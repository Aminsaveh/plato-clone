package com.example.ap_plato;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkHandler extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    EditText username;
    EditText password;
    NetworkHandler(EditText username,EditText password) {
        super();
        this.username = username;
        this.password = password;
    }

    public void run() {
        super.run();
        try {
            Socket socket = new Socket("192.168.1.9", 9000);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(){
        Thread senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                    username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                try {
                                    dos.writeUTF(username.getText().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            }
        });
        senderThread.start();

    }
}

