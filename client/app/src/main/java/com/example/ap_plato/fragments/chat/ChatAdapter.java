package com.example.ap_plato.fragments.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap_plato.ChatPage;
import com.example.ap_plato.MainActivity;
import com.example.ap_plato.R;
import com.example.ap_plato.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.RecyclerViewHolder> {
    Bitmap bitmap;
    private ArrayList<String> friends;
    private ArrayList<View> items = new ArrayList<>();
    public class RecyclerViewHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            this.textView = itemView.findViewById(R.id.friends_name);
            this.imageView =itemView.findViewById(R.id.friends_img);
        }
    }
    public ChatAdapter(ArrayList<String> friends){
        this.friends = friends;
    }
    @NonNull
    @Override
    public ChatAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_card_view , parent , false);
        ChatAdapter.RecyclerViewHolder recyclerViewHolder = new ChatAdapter.RecyclerViewHolder(view);
        items.add(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.RecyclerViewHolder holder, final int position) {
        final String user = this.friends.get(position);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetImageFreinds");
                    dataOutputStream.writeUTF(user);
                    int len = dataInputStream.readInt();
                    System.out.println(len);
                    byte[] image = new byte[len];
                    dataInputStream.readFully(image);
                    System.out.println(image.length);
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    System.out.println("sssss");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        holder.textView.setText(user);
        holder.imageView.setImageBitmap(bitmap);
        this.items.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(MainActivity.ipAddress , MainActivity.portNumber);
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            DataInputStream dataInputStream = new   DataInputStream(socket.getInputStream());
                            dataOutputStream.writeUTF("InitChat");
                            dataOutputStream.writeUTF(MainActivity.username);
                            dataOutputStream.writeUTF(friends.get(position));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })).start();
                Intent intent = new Intent(v.getContext() , ChatPage.class);
                intent.putExtra("username" , friends.get(position));
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

}
