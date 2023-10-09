package com.example.ap_plato;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ap_plato.fragments.game.CasualFragment;
import com.example.ap_plato.fragments.game.RankedFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Connect4 extends AppCompatActivity {
    public static ImageView[] circles = new ImageView[16];
    boolean isOver = false;
    int id;
    int first=0;
    boolean turn ;//true for player 1 and false for player two
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    static Socket socket;
    public static int score;
    public static int [] positions = new int[16];
    public static void printArray(int [] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "  ");
        }
        System.out.println();
    }
    public  static void fillArray(int a , int[] array){
        for (int i = 0; i < array.length; i++) {
            array[i] = a;
        }
    }
    int red = 0;
    int white = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect4);
        circles[0] = findViewById(R.id.cr1);//1
        circles[1] = findViewById(R.id.cr30);//2
        circles[2] = findViewById(R.id.cr26);//3
        circles[3] = findViewById(R.id.cr25);//4
        circles[4] = findViewById(R.id.cr24);//5
        circles[5] = findViewById(R.id.cr23);//6
        circles[6] = findViewById(R.id.cr22);//7
        circles[7] = findViewById(R.id.cr21);//8
        circles[8] = findViewById(R.id.cr20);//9
        circles[9] = findViewById(R.id.cr19);//10
        circles[10] = findViewById(R.id.cr18);//11
        circles[11] = findViewById(R.id.cr17);//12
        circles[12] = findViewById(R.id.cr29);//13
        circles[13] = findViewById(R.id.cr28);//14
        circles[14] = findViewById(R.id.cr27);//15
        circles[15] = findViewById(R.id.cr16);//16
        fillArray(-1 , positions);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Connect4");
                    if(RankedFragment.isranked){
                        dataOutputStream.writeUTF("Ranked");
                    }
                    else if(CasualFragment.iscasual){
                        dataOutputStream.writeUTF("Casual");
                    }
                    dataOutputStream.writeUTF(MainActivity.username);
                    String whichPlayer = dataInputStream.readUTF();
                    System.out.println(whichPlayer);
                    if (whichPlayer.equals("player1")) {
                        turn = true;
                        first=1;
                    } else {
                       first=2;
                        turn = false;
                    }
                    Thread listener = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try {
                                    if(turn==false) {
                                        id = dataInputStream.readInt();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                View view = findViewById(id);
                                                if (first==1) {
                                                    white++;
                                                    view.setBackgroundColor(Color.WHITE);
                                                    view.setBackgroundResource(R.drawable.white);
                                                } else if (first==2) {
                                                    view.setBackgroundColor(Color.RED);
                                                    red++;
                                                    view.setBackgroundResource(R.drawable.red);
                                                }
                                                check(view);
                                                turn = !turn;
                                                printArray(positions);
                                                finisher();
                                                if (isOver) {
                                                    Toast.makeText(Connect4.this, "Over", Toast.LENGTH_SHORT).show();
                                                }
                                                TextView r = findViewById(R.id.red_count);
                                                TextView w = findViewById(R.id.white_count);
                                                r.setText(String.valueOf(red));
                                                w.setText(String.valueOf(white));
                                            }
                                        });

                                    }
                                }catch (IOException e){
                                    e.printStackTrace();
                                }



                            }
                        }
                    });
                    listener.start();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        mainthread.start();
    }
    public void click(View view){
        if(turn) {
            id = view.getId();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataOutputStream.writeInt(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            if (first==1) {
                view.setBackgroundColor(Color.RED);
                red++;
                view.setBackgroundResource(R.drawable.red);
            } else if (first==2) {
                white++;
                view.setBackgroundColor(Color.WHITE);
                view.setBackgroundResource(R.drawable.white);
            }
            check(view);
            turn = !turn;
            printArray(positions);
            finisher();
            if (isOver) {
                Toast.makeText(Connect4.this, "Over", Toast.LENGTH_SHORT).show();
            }
            TextView r = findViewById(R.id.red_count);
            TextView w = findViewById(R.id.white_count);
            r.setText(String.valueOf(red));
            w.setText(String.valueOf(white));
        }
    }
    public void check(View view){
        int num = 0;
        for (int i = 0; i < circles.length; i++) {
            if (view.equals ( circles[i])) {
                num = i;
                System.out.println(circles[num].getBackground());
            }
        }
        if (turn){
            positions[num] = 1;
        }else{
            positions[num] = 0;
        }
    }
    public void finisher(){
        checkFinisher(0,1,2);
        checkFinisher(0,4,8);
        checkFinisher(0,5 ,10);
        checkFinisher(0,5,10);
        checkFinisher(1,5,9);
        checkFinisher(1,6,11);
        checkFinisher(1,2,3);
        checkFinisher(2,5,8);
        checkFinisher(2,6,10);
        checkFinisher(3,7,11);
        checkFinisher(3,6,9);
        checkFinisher(4,5,6);
        checkFinisher(4,9,14);
        checkFinisher(4,8,12);
        checkFinisher(5,6,7);
        checkFinisher(5,9,13);
        checkFinisher(5,10,15);
        checkFinisher(6,9,12);
        checkFinisher(6,12,9);
        checkFinisher(6,10,14);
        checkFinisher(7,11,15);
        checkFinisher(7,10,13);
        checkFinisher(8,9,10);
        checkFinisher(9,10,11);
        checkFinisher(12,13,14);
        checkFinisher(13,14,15);
    }
    public void checkFinisher(int a, int b , int c){
        if (inARow(a ,b ,c)){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataOutputStream.writeInt(999);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            if (turn == true){
                alertDialog.setTitle("You lose");
                alertDialog.setMessage("Oops! You lost");
                if(RankedFragment.isranked)
                score=0;
            }else{
                alertDialog.setTitle("You won");
                alertDialog.setMessage("You won");
                if(RankedFragment.isranked)
                score=3;
            }
            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(RankedFragment.isranked){
                        RankedFragment.isranked=false;
                    }
                    if(CasualFragment.iscasual){
                        CasualFragment.iscasual=false;
                    }
                    startActivity(new Intent(Connect4.this,MainPage.class));
                }
            });
            alertDialog.show();
        }
    }
    public boolean inARow(int a, int b, int c){
        if (positions[a] != -1){
            if (positions[a] == positions[b]){
                if (positions[b] == positions[c]){
                    return true;
                }
            }
        }
        return false;
    }


}