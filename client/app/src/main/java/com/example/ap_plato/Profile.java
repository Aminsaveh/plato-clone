package com.example.ap_plato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ap_plato.fragments.chat.ChatAdapter;
import com.example.ap_plato.fragments.people.PeopleFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
class GameHandler{
    String name;
    int Score;
}
public class Profile extends AppCompatActivity implements BioDialog.BioDialogListener{
    EditText bio;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String biotext;
    static RecyclerView recyclerView = null;
    static RecyclerView.Adapter recyclerviewAdapter = null;
    static RecyclerView.LayoutManager recyclerViewLayoutManger = null;
    public static ArrayList<GameHandler> games = new ArrayList<>();
    public static int [] gamesFrequency = new int[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /////////////////////////////this following codes are used to handle the frequency using recycler view///////////////////////////////

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetScores");
                    dataOutputStream.writeUTF(MainActivity.username);
                    gamesFrequency[0]=dataInputStream.readInt();
                    gamesFrequency[1]=dataInputStream.readInt();
                    gamesFrequency[2]=dataInputStream.readInt();
                    gamesFrequency[3]=dataInputStream.readInt();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();

/*
        gamesFrequency[0] = 29;//0-> xo
        gamesFrequency[1] = 52;//1-> guessWord
        gamesFrequency[2] = 71;//2-> dotsandboxes
        gamesFrequency[3] = 8;//3-> connect3

 */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //////do not change the following codes
        GameHandler xo = new GameHandler();
        xo.name = "XO";
        xo.Score = gamesFrequency[0];
        GameHandler dotsandboxes = new GameHandler();
        dotsandboxes.name = "DotsAndBoxes";
        dotsandboxes.Score = gamesFrequency[2];
        GameHandler connect3 =  new GameHandler();
        connect3.name = "Connect 3";
        connect3.Score = gamesFrequency[3];
        GameHandler wordguess = new GameHandler();
        wordguess.name = "Word Guess";
        wordguess.Score = gamesFrequency[1];
        games = new ArrayList<>();
        games.add(xo);
        games.add(dotsandboxes);
        games.add(connect3);
        games.add(wordguess);
        /////////////////////////////////////////////////////////
        final ImageView profilepicture = findViewById(R.id.profilepicture);
        bio = findViewById(R.id.bio_edit_text);
        final TextView profileusername = findViewById(R.id.profile_Username);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        de.hdodenhof.circleimageview.CircleImageView imageView = toolbar.findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        textView.setText("Profile");
        textView.setClickable(false);
        ImageView settingsView = toolbar.findViewById(R.id.settings_image);
        settingsView.setVisibility(View.VISIBLE);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("GetBio");
                    dataOutputStream.writeUTF(MainActivity.username);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileusername.setText(MainActivity.username);
                            profilepicture.setImageBitmap(MainPage.profileimagebitmap);
                        }
                    });
                        biotext=dataInputStream.readUTF();
                        if(!biotext.equals("Say something...")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bio.setText(biotext);
                                    bio.clearFocus();
                                }
                            });
                        }

                } catch (IOException e) {

                }
            }
        });
        mainthread.start();
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Settings.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainPage.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setSupportActionBar(toolbar);
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        recyclerView = findViewById(R.id.game_list);
        recyclerView = findViewById(R.id.game_list);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManger = new LinearLayoutManager(this);
        recyclerviewAdapter = new game_adapter(Profile.games);
        recyclerView.setLayoutManager(recyclerViewLayoutManger);
        recyclerView.setAdapter(recyclerviewAdapter);
    }

    public void openDialog() {
        BioDialog bioDialog = new BioDialog();
        bioDialog.show(getSupportFragmentManager(), "bio dialog");
    }

    @Override
    public void applyTexts(String username, String password) {
        bio.setText(username);
    }

}
