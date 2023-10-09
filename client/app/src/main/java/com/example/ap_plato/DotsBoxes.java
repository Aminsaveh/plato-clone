package com.example.ap_plato;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ap_plato.fragments.game.CasualFragment;
import com.example.ap_plato.fragments.game.RankedFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class DotsBoxes extends AppCompatActivity {
    public static boolean turn = true;//true for the first player and false for the second player
    public static View[] horizontalLines = new View[30];
    public static View[] verticalLines = new View[30];
    public static View[] squares = new View[25];
    public static ArrayList<ArrayList<View>> sq = new ArrayList<>();
    public static int redSqCount = 0;
    public static int blueSqCount = 0;
    public static int score;
    public static boolean[][] checker = new boolean[25][4];
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    static Socket socket;
    static int id;
    static View view1;
    int counter=0;
     int red;
    int blue;
    static int num1;
    static boolean reader = false;
    static boolean chance = false;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dots_boxes);
        for (int i = 0; i < 25; i++) {
            sq.add(new ArrayList<View>());
        }
        horizontalLines[0] = findViewById(R.id.view12);
        horizontalLines[1] = findViewById(R.id.view);
        horizontalLines[2] = findViewById(R.id.view2);
        horizontalLines[3] = findViewById(R.id.view3);
        horizontalLines[4] = findViewById(R.id.view4);
        horizontalLines[5] = findViewById(R.id.view11);
        horizontalLines[6] = findViewById(R.id.view13);
        horizontalLines[7] = findViewById(R.id.view14);
        horizontalLines[8] = findViewById(R.id.view15);
        horizontalLines[9] = findViewById(R.id.view16);
        horizontalLines[10] = findViewById(R.id.view54);
        horizontalLines[11] = findViewById(R.id.view42);
        horizontalLines[12] = findViewById(R.id.view53);
        horizontalLines[13] = findViewById(R.id.view52);
        horizontalLines[14] = findViewById(R.id.view51);
        horizontalLines[15] = findViewById(R.id.view50);
        horizontalLines[16] = findViewById(R.id.view49);
        horizontalLines[17] = findViewById(R.id.view48);
        horizontalLines[18] = findViewById(R.id.view47);
        horizontalLines[19] = findViewById(R.id.view46);
        horizontalLines[20] = findViewById(R.id.view56);
        horizontalLines[21] = findViewById(R.id.view62);
        horizontalLines[22] = findViewById(R.id.view59);
        horizontalLines[23] = findViewById(R.id.view58);
        horizontalLines[24] = findViewById(R.id.view57);
        horizontalLines[25] = findViewById(R.id.view44);
        horizontalLines[26] = findViewById(R.id.view45);
        horizontalLines[27] = findViewById(R.id.view55);
        horizontalLines[28] = findViewById(R.id.view61);
        horizontalLines[29] = findViewById(R.id.view60);

        verticalLines[0] = findViewById(R.id.view5);
        verticalLines[1] = findViewById(R.id.view6);
        verticalLines[2] = findViewById(R.id.view7);
        verticalLines[3] = findViewById(R.id.view8);
        verticalLines[4] = findViewById(R.id.view9);
        verticalLines[5] = findViewById(R.id.view10);
        verticalLines[6] = findViewById(R.id.view17);
        verticalLines[7] = findViewById(R.id.view21);
        verticalLines[8] = findViewById(R.id.view22);
        verticalLines[9] = findViewById(R.id.view23);
        verticalLines[10] = findViewById(R.id.view33);
        verticalLines[11] = findViewById(R.id.view34);
        verticalLines[12] = findViewById(R.id.view19);
        verticalLines[13] = findViewById(R.id.view24);
        verticalLines[14] = findViewById(R.id.view27);
        verticalLines[15] = findViewById(R.id.view30);
        verticalLines[16] = findViewById(R.id.view35);
        verticalLines[17] = findViewById(R.id.view36);
        verticalLines[18] = findViewById(R.id.view18);
        verticalLines[19] = findViewById(R.id.view25);
        verticalLines[20] = findViewById(R.id.view28);
        verticalLines[21] = findViewById(R.id.view31);
        verticalLines[22] = findViewById(R.id.view37);
        verticalLines[23] = findViewById(R.id.view38);
        verticalLines[24] = findViewById(R.id.view20);
        verticalLines[25] = findViewById(R.id.view26);
        verticalLines[26] = findViewById(R.id.view29);
        verticalLines[27] = findViewById(R.id.view32);
        verticalLines[28] = findViewById(R.id.view39);
        verticalLines[29] = findViewById(R.id.view40);

        squares[0] = findViewById(R.id.sq1);
        squares[1] = findViewById(R.id.sq2);
        squares[2] = findViewById(R.id.sq3);
        squares[3] = findViewById(R.id.sq4);
        squares[4] = findViewById(R.id.sq5);
        squares[5] = findViewById(R.id.sq6);
        squares[6] = findViewById(R.id.sq7);
        squares[7] = findViewById(R.id.sq8);
        squares[8] = findViewById(R.id.sq9);
        squares[9] = findViewById(R.id.sq10);
        squares[10] = findViewById(R.id.sq11);
        squares[11] = findViewById(R.id.sq12);
        squares[12] = findViewById(R.id.sq13);
        squares[13] = findViewById(R.id.sq14);
        squares[14] = findViewById(R.id.sq15);
        squares[15] = findViewById(R.id.sq16);
        squares[16] = findViewById(R.id.sq17);
        squares[17] = findViewById(R.id.sq18);
        squares[18] = findViewById(R.id.sq19);
        squares[19] = findViewById(R.id.sq20);
        squares[20] = findViewById(R.id.sq21);
        squares[21] = findViewById(R.id.sq22);
        squares[22] = findViewById(R.id.sq23);
        squares[23] = findViewById(R.id.sq24);
        squares[24] = findViewById(R.id.sq25);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("DotsBoxes");
                    if(RankedFragment.isranked){
                        dataOutputStream.writeUTF("Ranked");
                    }
                    else if(CasualFragment.iscasual){
                        dataOutputStream.writeUTF("Casual");
                    }
                    dataOutputStream.writeUTF(MainActivity.username);
                    String whichPlayer = dataInputStream.readUTF();
                    if (whichPlayer.equals("player1")) {
                        turn = true;
                        reader = false;
                    } else {
                        turn = false;
                        reader = true;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 30; ++i) {
                                if (turn) {
                                    verticalLines[i].setClickable(true);
                                    horizontalLines[i].setClickable(true);
                                } else {
                                    verticalLines[i].setClickable(false);
                                    horizontalLines[i].setClickable(false);
                                }
                            }
                        }
                    });
                    Thread listener = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                int loc = 0;
                                String message = "";
                                try {
                                    message = dataInputStream.readUTF();
                                    System.out.println(message);
                                    loc = dataInputStream.readInt();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                View view = findViewById(loc);
                                if (DotsBoxes.turn == true) {
                                    view.setBackgroundColor(Color.RED);
                                    view.setClickable(false);
                                } else {
                                    view.setBackgroundColor(Color.BLUE);
                                    view.setClickable(false);
                                }
                                check();
                                checkCount();
                                if (DotsBoxes.redSqCount + DotsBoxes.blueSqCount == 25) {
                                    try {
                                        dataOutputStream.writeInt(999);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    redSqCount=0;
                                    blueSqCount=0;
                                    checker = new boolean[25][4];
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DotsBoxes.this);
                                            //type one
                                            if (DotsBoxes.redSqCount > DotsBoxes.blueSqCount) {
                                                alertDialog.setMessage("You won");
                                                if(RankedFragment.isranked)
                                                score=3;
                                            }
                                            else if (DotsBoxes.redSqCount < DotsBoxes.blueSqCount) {
                                                alertDialog.setMessage("You Lost");
                                                if(RankedFragment.isranked)
                                                score=0;
                                            } else {
                                                alertDialog.setMessage("Draw");
                                                if(RankedFragment.isranked)
                                                score=1;
                                            }
                                            alertDialog.setTitle("Finish!");
                                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    redSqCount=0;
                                                    blueSqCount=0;
                                                    startActivity(new Intent(DotsBoxes.this,MainPage.class));
                                                    if(RankedFragment.isranked){
                                                        RankedFragment.isranked=false;
                                                    }
                                                    if(CasualFragment.iscasual){
                                                        CasualFragment.iscasual =false;
                                                    }
                                                    finish();
                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    });
                                }
                                if (message.startsWith("chance")) {
                                    continue;
                                } else {
                                    turn = !turn;
                                    reader = false;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 30; ++i) {
                                            verticalLines[i].setClickable(true);
                                            horizontalLines[i].setClickable(true);
                                        }
                                    }
                                });
                            }
                        }
                    });

                    listener.start();
                } catch (IOException e) {
                        e.printStackTrace();
                }
            }
        });
        mainthread.start();

    }


    public void check() {
        checkSquares(0, 1, 5, 0, 1);//1
        checkSquares(1, 2, 6, 1, 2);//2
        checkSquares(2, 3, 7, 2, 3);//3
        checkSquares(3, 4, 8, 3, 4);//4
        checkSquares(4, 5, 9, 4, 5);//5
        checkSquares(5, 7, 10, 6, 6);//6
        checkSquares(6, 8, 11, 7, 7);//7
        checkSquares(7, 9, 12, 8, 8);//8
        checkSquares(8, 10, 13, 9, 9);//9
        checkSquares(9, 11, 14, 10, 10);//10
        checkSquares(10, 13, 15, 12, 11);//11
        checkSquares(11, 14, 16, 13, 12);//12
        checkSquares(12, 15, 17, 14, 13);//13
        checkSquares(13, 16, 18, 15, 14);//14
        checkSquares(14, 17, 19, 16, 15);//15
        checkSquares(15, 19, 20, 18, 16);//16
        checkSquares(16, 20, 21, 19, 17);//17
        checkSquares(17, 21, 22, 20, 18);//18
        checkSquares(18, 22, 23, 21, 19);//19
        checkSquares(19, 23, 24, 22, 20);//20
        checkSquares(20, 25, 25, 24, 21);//21
        checkSquares(21, 26, 26, 25, 22);//22
        checkSquares(22, 27, 27, 26, 23);//23
        checkSquares(23, 28, 28, 27, 24);//24
        checkSquares(24, 29, 29, 28, 25);//25
    }

    public void checkSquares(int a, int b, int c, int d, int num) {

        int[] colors = getColors(a, b, c, d);//top-right-bottom-left
        if (true) {
            if (((colors[0] == Color.BLUE) || (colors[0] == Color.RED)) && checker[num - 1][0] == false) {
                checker[num - 1][0] = true;
                sq.get(num - 1).add(horizontalLines[a]);
            }
            if (((colors[1] == Color.BLUE) || (colors[1] == Color.RED)) && checker[num - 1][1] == false) {
                checker[num - 1][1] = true;
                sq.get(num - 1).add(verticalLines[b]);
            }
            if (((colors[2] == Color.BLUE) || (colors[2] == Color.RED)) && checker[num - 1][2] == false) {
                checker[num - 1][2] = true;
                sq.get(num - 1).add(horizontalLines[c]);
            }
            if (((colors[3] == Color.BLUE) || (colors[3] == Color.RED)) && checker[num - 1][3] == false) {
                checker[num - 1][3] = true;
                sq.get(num - 1).add(verticalLines[d]);
            }
            if (sq.get(num - 1).size() == 4) {
                        ColorDrawable viewColor = (ColorDrawable) sq.get(num - 1).get(3).getBackground();
                        squares[num - 1].setBackgroundColor(viewColor.getColor());
                        sq.get(num - 1).removeAll(sq.get(num - 1));

                if (reader == false&&counter==0) {
                    counter++;
                    DotsBoxes.turn = !DotsBoxes.turn;
                    chance = true;
                }
            }
        }
    }

    public int[] getColors(int a, int b, int c, int d) {
        int[] colors = new int[4];
        ColorDrawable viewColor;
        viewColor = (ColorDrawable) horizontalLines[a].getBackground();
        colors[0] = viewColor.getColor();
        viewColor = (ColorDrawable) verticalLines[b].getBackground();
        colors[1] = viewColor.getColor();
        viewColor = (ColorDrawable) horizontalLines[c].getBackground();
        colors[2] = viewColor.getColor();
        viewColor = (ColorDrawable) verticalLines[d].getBackground();
        colors[3] = viewColor.getColor();
        return colors;
    }

    public void onLineCLick(View view) {
        id = view.getId();
        view1 = view;
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeInt(id);
                            if (DotsBoxes.turn == true) {
                                view1.setBackgroundColor(Color.RED);
                                view1.setClickable(false);
                            } else {
                                view1.setBackgroundColor(Color.BLUE);
                                view1.setClickable(false);
                            }
                    DotsBoxes.turn = !DotsBoxes.turn;
                    check();
                    counter=0;
                    checkCount();
                    if (DotsBoxes.redSqCount + DotsBoxes.blueSqCount == 25) {
                        try {
                            dataOutputStream.writeInt(999);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DotsBoxes.this);
                                //type one
                                if (DotsBoxes.redSqCount > DotsBoxes.blueSqCount) {
                                    alertDialog.setMessage("You won");
                                    if(RankedFragment.isranked)
                                    score=3;
                                }
                                else if (DotsBoxes.redSqCount < DotsBoxes.blueSqCount) {
                                    alertDialog.setMessage("You lost");
                                    if(RankedFragment.isranked)
                                    score=0;
                                } else {
                                    alertDialog.setMessage("Draw");
                                    if(RankedFragment.isranked)
                                    score=1;
                                }
                                alertDialog.setTitle("Finish!");
                                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        redSqCount=0;
                                        blueSqCount=0;
                                        checker = new boolean[25][4];
                                        startActivity(new Intent(DotsBoxes.this,MainPage.class));
                                        if(RankedFragment.isranked){
                                            RankedFragment.isranked=false;
                                        }
                                        if(CasualFragment.iscasual){
                                            CasualFragment.iscasual =false;
                                        }
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }
                    if (chance == true) {
                        chance = false;
                        dataOutputStream.writeUTF("chance");
                        System.out.println("s");
                    } else {
                        dataOutputStream.writeUTF("Ok");
                        System.out.println("s");
                        reader = true;
                                for (int i = 0; i < 30; ++i) {
                                    verticalLines[i].setClickable(false);
                                    horizontalLines[i].setClickable(false);
                                }
                    }
                }catch (IOException e) {
                }
            }
        });
        x.start();
    }


    public void checkCount() {
         red = 0;
         blue = 0;
        for (int i = 0; i < squares.length; i++) {
            ColorDrawable color = (ColorDrawable) squares[i].getBackground();
            if (color.getColor() == Color.BLUE) {
                blue++;
            } else if (color.getColor() == Color.RED) {
                red++;
            }
        }
        DotsBoxes.redSqCount = red;
        DotsBoxes.blueSqCount = blue;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((red != DotsBoxes.redSqCount) || (blue != DotsBoxes.blueSqCount)) {
                    Toast.makeText(DotsBoxes.this, "Red -> " + red + "  Blue -> " + blue, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
