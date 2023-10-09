package com.example.ap_plato;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ap_plato.fragments.chat.ChatFragment;
import com.example.ap_plato.fragments.game.GameFragment;
import com.example.ap_plato.fragments.home.HomeFragment;
import com.example.ap_plato.fragments.people.PeopleFragment;
import com.example.ap_plato.fragments.shop.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainPage extends AppCompatActivity {
    private   BottomNavigationView bottomNavigationView;
    DataOutputStream dos;
    DataInputStream dis;
    Bitmap bitmap=null;
    Socket socket = null;
    public static Bitmap profileimagebitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        final ImageView profileimage = findViewById(R.id.profile_image);
        Thread mainthread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
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
        mainthread2.start();
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF("Main");
                    dos.writeUTF(MainActivity.username);
                    int len = dis.readInt();
                    if(len==-1){
                        Drawable myDrawable = getResources().getDrawable(R.drawable.avatar_default);
                        bitmap = ((BitmapDrawable) myDrawable).getBitmap();
                        profileimagebitmap=bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                profileimage.setImageBitmap(bitmap);
                            }
                        });
                    }else{
                        System.out.println(len);
                        byte[] image = new byte[len];
                        dis.readFully(image);
                        System.out.println(image.length);
                        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        if(bitmap==null){
                            Drawable myDrawable = getResources().getDrawable(R.drawable.avatar_default);
                            bitmap = ((BitmapDrawable) myDrawable).getBitmap();
                        }
                        profileimagebitmap = bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                profileimage.setImageBitmap(bitmap);
                            }
                        });

                    }
                    /*
                    (new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Socket socket = new Socket(MainActivity.ipAddress , MainActivity.portNumber);
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                dataOutputStream.writeUTF("ClearSocket");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })).start();

                     */
                }catch (IOException e){

                }
            }
        });
        mainthread.start();


        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Plato");
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeItem:{
                        Toolbar toolbar = findViewById(R.id.home_toolbar);
                        TextView textView = toolbar.findViewById(R.id.toolbar_title);
                        textView.setText("Home");
                        setSupportActionBar(toolbar);
                        HomeFragment homeFragment =  new HomeFragment();
                        openFragment(homeFragment);
                        setBottomNavigationItemIcon(bottomNavigationView , 0);

                        break;
                    }
                    case R.id.chatItem:{
                        Toolbar toolbar = findViewById(R.id.home_toolbar);
                        TextView textView = toolbar.findViewById(R.id.toolbar_title);
                        textView.setText("Chat");
                        setSupportActionBar(toolbar);
                        ChatFragment chatFragment = new ChatFragment();
                        item.setIcon(R.drawable.ic_chat_black_24dp);
                        openFragment(chatFragment);
                        setBottomNavigationItemIcon(bottomNavigationView , 2);

                        break;
                    }
                    case R.id.gameItem:{
                        Toolbar toolbar = findViewById(R.id.home_toolbar);
                        TextView textView = toolbar.findViewById(R.id.toolbar_title);
                        textView.setText("Game");
                        setSupportActionBar(toolbar);
                        GameFragment gameFragment = new GameFragment();
                        openFragment(gameFragment);
                        setBottomNavigationItemIcon(bottomNavigationView , 1);

                        break;
                    }
                    case R.id.peopleItem:{
                        Toolbar toolbar = findViewById(R.id.home_toolbar);
                        TextView textView = toolbar.findViewById(R.id.toolbar_title);
                        textView.setText("People");
                        setSupportActionBar(toolbar);
                        PeopleFragment peopleFragment = new PeopleFragment();
                        openFragment(peopleFragment);
                        setBottomNavigationItemIcon(bottomNavigationView , 3);

                        break;
                    }
                    case R.id.shopItem:{
                        Toolbar toolbar = findViewById(R.id.home_toolbar);
                        TextView textView = toolbar.findViewById(R.id.toolbar_title);
                        textView.setText("Shop");
                        setSupportActionBar(toolbar);
                        ShopFragment shopFragment = new ShopFragment();
                        openFragment(shopFragment);
                        setBottomNavigationItemIcon(bottomNavigationView , 4);

                        break;
                    }
                }
                return false;
            }
        });
    }
    public void setBottomNavigationItemIcon(BottomNavigationView bottomNavigationView , int i){
        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_home_black_24dp_50_percent_opacity);
        bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_videogame_asset_black_24dp_50_percent_opacity);
        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_chat_black_24dp_50_percent_opacity);
        bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_people_black_24dp_50_percent_opacity);
        bottomNavigationView.getMenu().getItem(4).setIcon(R.drawable.ic_shopping_cart_black_24dp_50_percent_opacity);
        if (i == 0){
            bottomNavigationView.getMenu().getItem(i).setIcon(R.drawable.ic_home_black_24dp);
        }
        else if(i == 1){
            bottomNavigationView.getMenu().getItem(i).setIcon(R.drawable.ic_videogame_asset_black_24dp);
        }
        else if(i == 2){
            bottomNavigationView.getMenu().getItem(i).setIcon(R.drawable.ic_chat_black_24dp);
        }
        else if(i == 3){
            bottomNavigationView.getMenu().getItem(i).setIcon(R.drawable.ic_people_black_24dp);
        }
        else if(i == 4){
            bottomNavigationView.getMenu().getItem(i).setIcon(R.drawable.ic_shopping_cart_black_24dp);
        }

    }
    public void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container , fragment);
        fragmentTransaction.commit();
    }
    public void moveToProfile(View view){
        startActivity(new Intent(MainPage.this , Profile.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void moveToSignUpfromMain(View view){
        startActivity(new Intent(view.getContext() , Signup.class));
    }

}
