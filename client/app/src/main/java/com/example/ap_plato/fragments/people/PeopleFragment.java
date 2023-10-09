package com.example.ap_plato.fragments.people;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;


import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ap_plato.MainActivity;
import com.example.ap_plato.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ap_plato.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class PeopleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static ArrayList<String> friends;
    static RecyclerView recyclerView = null;
    static RecyclerView.Adapter recyclerviewAdapter = null;
    static RecyclerView.LayoutManager recyclerViewLayoutManger = null;
    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;
    public static Socket socket;
    public static Bitmap bitmap;
    public static String Uname = "";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getFragmentManager().popBackStack();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.people_fragment, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swiperefresh3);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friends = new ArrayList<>();
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetFriends");
                    dataOutputStream.writeUTF(MainActivity.username);
                    Integer size = Integer.parseInt(dataInputStream.readUTF());
                    for (int i = 0; i < size; i++) {
                        PeopleFragment.friends.add(dataInputStream.readUTF());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();

        Drawable myDrawable = getResources().getDrawable(R.drawable.avatar_default);
        bitmap = ((BitmapDrawable) myDrawable).getBitmap();
        recyclerView = root.findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManger = new LinearLayoutManager(getActivity());
        recyclerviewAdapter = new PeopleAdapter(friends);
        recyclerView.setLayoutManager(recyclerViewLayoutManger);
        recyclerView.setAdapter(recyclerviewAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add user by plato username");
                final EditText input = new EditText(getActivity());
                input.setHint("Username");
                input.setPadding(50, 50, 50, 50);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PeopleFragment.Uname = input.getText().toString();
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                    dataOutputStream.writeUTF("SendNotification");
                                    dataOutputStream.writeUTF(PeopleFragment.Uname);
                                    dataOutputStream.writeUTF(MainActivity.username);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })).start();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("SetFriendSocket");
                    while (true) {
                        final String fromUsername = dataInputStream.readUTF();
                        if (fromUsername.equals("OVER")){
                            break;
                        }
                        if (fromUsername.equals("##")){

                            (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                        dataOutputStream.writeUTF("AddFriend");
                                        dataOutputStream.writeUTF(MainActivity.username);
                                        dataOutputStream.writeUTF(PeopleFragment.Uname);
                                        PeopleFragment.friends.add(PeopleFragment.Uname);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                PeopleFragment.recyclerviewAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            })).start();
                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("New Friend");
                                    builder.setMessage(fromUsername + "  Wants to add you to his friends");
                                    builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            (new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                                                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                                        dataOutputStream.writeUTF("AddFriend");
                                                        dataOutputStream.writeUTF(MainActivity.username);
                                                        dataOutputStream.writeUTF(fromUsername);
                                                        PeopleFragment.friends.add(fromUsername);
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                PeopleFragment.recyclerviewAdapter.notifyDataSetChanged();
                                                            }
                                                        });
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            })).start();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            (new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                                                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                                        dataOutputStream.writeUTF("DltFriend");
                                                        dataOutputStream.writeUTF(MainActivity.username);
                                                        System.out.println(MainActivity.username);
                                                        System.out.println(fromUsername);
                                                        dataOutputStream.writeUTF(fromUsername);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            })).start();
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
        return root;
    }


    @Override
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        swipeRefreshLayout.setRefreshing(false);
    }
}
