package com.example.ap_plato;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ap_plato.fragments.game.CasualFragment;
import com.example.ap_plato.fragments.game.RankedFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Xo extends AppCompatActivity implements  View.OnClickListener{
    private Button[][] buttons = new Button[3][3];
    private boolean isplayer1 = true;
    private static boolean thisturn = false;
    private  static int roundcount;
    private TextView turn;
    private int iloc;
    private int jloc;
    private static int index;
    public static int score;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xo);
        turn = findViewById(R.id.yourturn);
        Thread mainthred = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Xo");
                    if (RankedFragment.isranked) {
                        dataOutputStream.writeUTF("Ranked");
                    } else if (CasualFragment.iscasual) {
                        dataOutputStream.writeUTF("Casual");
                    }
                    dataOutputStream.writeUTF(MainActivity.username);
                    String whichPlayer = dataInputStream.readUTF();
                    if (whichPlayer.equals("player1")) {
                        isplayer1 = true;
                        thisturn = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (thisturn) {
                                    turn.setText("Your Turn");
                                } else
                                    turn.setText("!Your Turn");
                            }
                        });
                    } else {
                        isplayer1 = false;
                        thisturn = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (thisturn) {
                                    turn.setText("Your Turn");
                                } else
                                    turn.setText("!Your Turn");
                            }
                        });
                    }
                    while (!checkForWin()) {
                        if (thisturn == false) {
                            final int otherplayeri = dataInputStream.readInt();
                            System.out.println(otherplayeri);
                            final int otherplayerj = dataInputStream.readInt();
                            System.out.println(otherplayerj);
                            thisturn = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isplayer1) {
                                        roundcount++;
                                        if (thisturn) {
                                            turn.setText("Your Turn");
                                        } else
                                            turn.setText("!Your Turn");
                                        buttons[otherplayeri][otherplayerj].setText("O");
                                        if (checkForWin() || roundcount == 9) {
                                        }
                                    } else {
                                        roundcount++;
                                        if (thisturn) {
                                            turn.setText("Your Turn");
                                        } else
                                            turn.setText("!Your Turn");
                                        buttons[otherplayeri][otherplayerj].setText("X");
                                        if (checkForWin() || roundcount == 9) {


                                        }
                                    }
                                }
                            });
                        }
                    }

                } catch (IOException e) {

                }


            }
        });
        mainthred.start();


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (checkForWin()) {
                        roundcount=0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Xo.this);
                                if (!thisturn) {
                                    alertDialog.setTitle("Win");
                                    alertDialog.setMessage("WellDone!");
                                    if (RankedFragment.isranked)
                                        score = 3;
                                }
                                if (thisturn) {
                                    alertDialog.setTitle("lose");
                                    alertDialog.setMessage("Oops!");
                                    if (RankedFragment.isranked)
                                        score = 0;
                                }
                                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Xo.this, MainPage.class));
                                        if (RankedFragment.isranked) {
                                            RankedFragment.isranked = false;
                                        }
                                        if (CasualFragment.iscasual) {
                                            CasualFragment.iscasual = false;
                                        }
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                        break;
                    } else if (roundcount == 9) {
                        roundcount=0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Xo.this);
                                alertDialog.setTitle("Draw");
                                alertDialog.setMessage("Draw!");
                                if (RankedFragment.isranked)
                                    score = 1;
                                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Xo.this, MainPage.class));
                                        if (RankedFragment.isranked) {
                                            RankedFragment.isranked = false;
                                        }
                                        if (CasualFragment.iscasual) {
                                            CasualFragment.iscasual = false;
                                        }
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                        break;
                    }
                }
            }
        });
        thread.start();
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (isplayer1&&thisturn&&!checkForWin()) {
            thisturn =false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (thisturn) {
                        turn.setText("Your Turn");
                    } else
                        turn.setText("!Your Turn");
                    }
            });
            roundcount++;
            ((Button) v).setText("X");
            int id = ((Button)v).getId();
            for (int i =0 ; i<3;++i){
                for(int j =0;j<3;++j){
                    String buttonID = "button_" + i + j;
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    if(id==resID){
                        iloc = i;
                        jloc = j;
                        Thread thread =new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dataOutputStream.writeInt(iloc);
                                    dataOutputStream.writeInt(jloc);
                                    if(checkForWin()){
                                        if(!thisturn) {
                                            dataOutputStream.writeUTF("win");

                                        }
                                        if(thisturn){
                                            dataOutputStream.writeUTF("win");

                                        }
                                    }
                                    else if (roundcount==9){
                                        dataOutputStream.writeUTF("draw");
                                    }
                                    else {
                                        dataOutputStream.writeUTF("continue");
                                    }
                                } catch (IOException e ) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        break;
                    }
                }
            }
        } else if (!isplayer1&&thisturn&&!checkForWin()) {
            ((Button) v).setText("O");
            thisturn =false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (thisturn) {
                        turn.setText("Your Turn");
                    } else
                        turn.setText("!Your Turn");
                }
            });
            roundcount++;
            int id = ((Button)v).getId();
            for (int i =0 ; i<3;++i){
                for(int j =0;j<3;++j){
                    String buttonID = "button_" + i + j;
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    if(id==resID){
                        iloc = i;
                        jloc = j;
                        Thread thread =new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dataOutputStream.writeInt(iloc);
                                    dataOutputStream.writeInt(jloc);
                                    if(checkForWin()){
                                        if(!thisturn) {
                                            dataOutputStream.writeUTF("win");


                                        }
                                        if(thisturn){
                                            dataOutputStream.writeUTF("win");
                                        }
                                    }
                                    else if (roundcount==9){
                                        roundcount=0;
                                        dataOutputStream.writeUTF("draw");

                                    }
                                    else {
                                        dataOutputStream.writeUTF("continue");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        break;
                    }
                }
            }
        }


    }


        private boolean checkForWin() {
            final String[][] field = new String[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    field[i][j] = buttons[i][j].getText().toString();
                }
            }
            for (int i = 0; i < 3; i++) {
                if (field[i][0].equals(field[i][1])
                        && field[i][0].equals(field[i][2])
                        && !field[i][0].equals("")) {
                    index=i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          buttons[index][0].setTextColor(Color.parseColor("#FF0000"));
                          buttons[index][1].setTextColor(Color.parseColor("#FF0000"));
                          buttons[index][2].setTextColor(Color.parseColor("#FF0000"));
                        }
                    });
                    return true;
                }
            }
            for (int i = 0; i < 3; i++) {
                if (field[0][i].equals(field[1][i])
                        && field[0][i].equals(field[2][i])
                        && !field[0][i].equals("")) {
                    index=i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttons[0][index].setTextColor(Color.parseColor("#FF0000"));
                            buttons[1][index].setTextColor(Color.parseColor("#FF0000"));
                            buttons[2][index].setTextColor(Color.parseColor("#FF0000"));
                        }
                    });
                    return true;
                }
            }
            if (field[0][0].equals(field[1][1])
                    && field[0][0].equals(field[2][2])
                    && !field[0][0].equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttons[0][0].setTextColor(Color.parseColor("#FF0000"));
                        buttons[1][1].setTextColor(Color.parseColor("#FF0000"));
                        buttons[2][2].setTextColor(Color.parseColor("#FF0000"));
                    }
                });
                return true;
            }
            if (field[0][2].equals(field[1][1])
                    && field[0][2].equals(field[2][0])
                    && !field[0][2].equals("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttons[0][2].setTextColor(Color.parseColor("#FF0000"));
                        buttons[1][1].setTextColor(Color.parseColor("#FF0000"));
                        buttons[2][0].setTextColor(Color.parseColor("#FF0000"));
                    }
                });
                return true;
            }
            return false;
        }

}
