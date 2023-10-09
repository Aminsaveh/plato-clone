package com.example.ap_plato.fragments.game;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ap_plato.*;
import com.google.android.material.button.MaterialButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RankedFragment extends Fragment {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    public static  boolean isranked;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranked_fragment,container,false);
        final Button search = view.findViewById(R.id.search);
        final Button join = view .findViewById(R.id.join);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Ranked");
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        mainthread.start();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                                dataOutputStream.writeUTF(Game.game);
                                dataOutputStream.writeUTF(MainActivity.username);
                                String message = dataInputStream.readUTF();
                                System.out.println(message);
                                if (message.equals("found")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            search.setEnabled(false);
                                            join.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity().getApplication(), "Game Found", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity().getApplication(), "Game NotFound!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                        }catch (IOException e){

                        }

                    }
                });
                thread.start();

            }

        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isranked=true;
                        switch (Game.game){
                            case "Xo" : startActivity(new Intent(getActivity().getApplication(), Xo.class));break;
                            case "Guessword" : startActivity(new Intent(getActivity().getApplication(), WordGuess.class));break;
                            case "Dots&Boxes" : startActivity(new Intent(getActivity().getApplication(), DotsBoxes.class));break;
                            case "Connect4" : startActivity(new Intent(getActivity().getApplication(), Connect4.class));break;
                        }
                    }
                });
            }
        });
        return view;
    }
}
