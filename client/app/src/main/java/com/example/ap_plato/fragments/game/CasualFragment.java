package com.example.ap_plato.fragments.game;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ap_plato.*;
import com.example.ap_plato.fragments.chat.ChatFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class CasualFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    class Room {
        String player1;
        String player2;
        String game;
    }

    public static boolean iscasual;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    MyAdapter adapter;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Room> rooms;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.casual_fragment, container, false);
        listView = view.findViewById(R.id.list);
        Button newroombtn = view.findViewById(R.id.newroom);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetRooms");
                    dataOutputStream.writeUTF(Game.game);
                    rooms = new ArrayList<>();
                    while (true) {
                        String player1 = dataInputStream.readUTF();
                        if (player1.equals("finish")) {
                            break;
                        }
                        Room room = new Room();
                        room.player1 = player1;
                        room.game = Game.game;
                        rooms.add(room);
                        adapter = new MyAdapter(getActivity().getApplicationContext(), rooms);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        mainthread.start();


        newroombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Room room = new Room();
                room.player1 = MainActivity.username;
                room.game = Game.game;
                room.player2 = "";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataOutputStream.writeUTF("new");
                            dataOutputStream.writeUTF(room.player1);
                            dataOutputStream.writeUTF(room.game);
                        } catch (IOException e) {

                        }

                    }
                });
                thread.start();
                iscasual = true;
                switch (Game.game) {
                    case "Xo":
                        startActivity(new Intent(getActivity().getApplication(), Xo.class));
                        break;
                    case "Guessword":
                        startActivity(new Intent(getActivity().getApplication(), WordGuess.class));
                        break;
                    case "Dots&Boxes":
                        startActivity(new Intent(getActivity().getApplication(), DotsBoxes.class));
                        break;
                    case "Connect4":
                        startActivity(new Intent(getActivity().getApplication(), Connect4.class));
                        break;
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Room room = adapter.getItem(position);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataOutputStream.writeUTF("joinroom");
                            dataOutputStream.writeUTF(Game.game);
                            dataOutputStream.writeUTF(MainActivity.username);
                            dataOutputStream.writeUTF(room.player1);
                            String message = dataInputStream.readUTF();
                            if (message.equals("ok")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iscasual = true;
                                        switch (Game.game) {
                                            case "Xo":
                                                startActivity(new Intent(getActivity().getApplication(), Xo.class));
                                                break;
                                            case "Guessword":
                                                startActivity(new Intent(getActivity().getApplication(), WordGuess.class));
                                                break;
                                            case "Dots&Boxes":
                                                startActivity(new Intent(getActivity().getApplication(), DotsBoxes.class));
                                                break;
                                            case "Connect4":
                                                startActivity(new Intent(getActivity().getApplication(), Connect4.class));
                                                break;
                                        }
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplication(), "Full", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        } catch (IOException e) {

                        }
                    }
                });
                thread.start();
            }
        });
        return view;

    }


    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        swipeRefreshLayout.setRefreshing(false);
    }

    class MyAdapter extends ArrayAdapter<Room> {
        Context context;
        ArrayList<Room> rooms;

        MyAdapter(Context c, ArrayList<Room> rooms) {
            super(c, R.layout.roomitem_layout, rooms);
            this.context = c;
            this.rooms = rooms;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View listitem = layoutInflater.inflate(R.layout.roomitem_layout, parent, false);
            TextView creator = listitem.findViewById(R.id.creator);
            creator.setText(rooms.get(position).player1);
            return listitem;
        }
    }
}
