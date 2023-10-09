package com.example.ap_plato.fragments.game;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ap_plato.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainPage) getActivity()).getSupportActionBar().setTitle("Game");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.game_fragment , container , false);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Scores");
                    dataOutputStream.writeUTF(MainActivity.username);
                    dataOutputStream.writeInt(Xo.score);
                    dataOutputStream.writeInt(WordGuess.score);
                    dataOutputStream.writeInt(DotsBoxes.score);
                    dataOutputStream.writeInt(Connect4.score);
                    Xo.score=0;
                    WordGuess.score=0;
                    DotsBoxes.score=0;
                    Connect4.score=0;
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        mainthread.start();
        ImageButton xobtn = root.findViewById(R.id.xo);
        xobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),Game.class);
                intent.putExtra("title","Xo");
                startActivity(intent);
            }
        });
        ImageButton guesswordbtn = root.findViewById(R.id.guessword);
        guesswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),Game.class);
                intent.putExtra("title","Guessword");
                startActivity(intent);
            }
        });
        ImageButton dotsboxesbtn = root.findViewById(R.id.dotsboxes);
        dotsboxesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),Game.class);
                intent.putExtra("title","Dots&Boxes");
                startActivity(intent);
            }
        });
        ImageButton connect4btn = root.findViewById(R.id.connect4);
        connect4btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(),Game.class);
                intent.putExtra("title","Connect4");
                startActivity(intent);
            }
        });
        return root;
    }
}
