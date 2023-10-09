package com.example.ap_plato;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.ap_plato.fragments.game.CasualFragment;
import com.example.ap_plato.fragments.game.RankedFragment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WordGuess extends AppCompatActivity {
    public static int chances = 0;
    public static EditText[] letters = new EditText[6];
    public static int count = 0;
    public static boolean beginBool = false;
    static boolean isfirst = false;
    boolean thiswins = false;
    boolean isthisrecieved = false;
    boolean isotherwinner = false;
    boolean isotherrecieved = false;
    static EditText wordEditText;
    static TextView chancestextview;
    static String wordString = null;
    static Button check;
    static Button start;
    TextView line;
    static TextView player1;
    static TextView player2;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    static Socket socket;
    public static int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_guess);
        check = findViewById(R.id.wg_check_btn);
        wordEditText = findViewById(R.id.wg_set_word_edit_text);
        wordString = null;
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        line = findViewById(R.id.line);
        start = findViewById(R.id.wg_start_btn);
        letters[0] = findViewById(R.id.letter1);
        letters[1] = findViewById(R.id.letter2);
        letters[2] = findViewById(R.id.letter3);
        letters[3] = findViewById(R.id.letter4);
        letters[4] = findViewById(R.id.letter5);
        letters[5] = findViewById(R.id.letter6);
        chancestextview = findViewById(R.id.chance_count);
        player1.setVisibility(View.VISIBLE);
        wordEditText.setVisibility(View.VISIBLE);
        player2.setVisibility(View.VISIBLE);
        start.setVisibility(View.VISIBLE);
        line.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        chancestextview.setVisibility(View.INVISIBLE);
        WordGuess.count = 0;
        WordGuess.chances = 0;
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeUTF("Guessword");
                    dataOutputStream.flush();
                    if (RankedFragment.isranked) {
                        dataOutputStream.writeUTF("Ranked");
                    }
                    if (CasualFragment.iscasual) {
                        dataOutputStream.writeUTF("Casual");
                    }
                    dataOutputStream.writeUTF(MainActivity.username);
                    String whichPlayer = dataInputStream.readUTF();
                    if (whichPlayer.equals("player1")) {
                        isfirst = true;
                    } else {
                        isfirst = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                player2.setText("Waiting for Player1..");
                            }
                        });

                    }

                } catch (IOException e) {

                }

            }
        });
        mainthread.start();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                wordString = wordEditText.getText().toString();
                if (wordString.length() <= 6) {
                    WordGuess.beginBool = true;
                    Toast.makeText(WordGuess.this, wordString, Toast.LENGTH_SHORT).show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(wordString);
                                dataOutputStream.writeUTF(wordString);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    Thread listener = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                wordString = dataInputStream.readUTF();
                                System.out.println("sssssss" + wordString);
                                Thread listener1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String message = dataInputStream.readUTF();
                                            System.out.println("ssssssssss" + message);
                                            if (message != null) {
                                                if (message.startsWith("won")) {
                                                    isotherwinner = true;
                                                    isotherrecieved = true;

                                                }
                                                if (message.startsWith("lose")) {
                                                    isotherwinner = false;
                                                    isotherrecieved = true;
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                listener1.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        player2.setText("Please Guess");
                                        for (int i = 0; i < wordString.length(); i++) {
                                            letters[i].setVisibility(View.VISIBLE);
                                        }
                                        check.setVisibility(View.VISIBLE);
                                        chancestextview.setVisibility(View.VISIBLE);
                                        WordGuess.chances = wordString.length();
                                        chancestextview.setText("Chances Left = " + WordGuess.chances);
                                        wordEditText.setEnabled(false);
                                        start.setEnabled(false);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    listener.start();


                } else {
                    WordGuess.beginBool = false;
                    Toast.makeText(WordGuess.this, "Invalid Input", Toast.LENGTH_LONG).show();
                }
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;
                int democount = 0;
                for (int i = 0; i < wordString.length(); i++) {
                    if (!(letters[i].getText().length() == 0)) {
                        if (!(letters[i].getText().charAt(0) == wordString.charAt(i))) {
                            isOk = false;
                        } else {
                            letters[i].setEnabled(false);
                            democount++;
                        }
                    }
                }
                WordGuess.count = democount;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (checkresult() == false) {

                        }
                    }
                });
                thread.start();
                if (WordGuess.chances <= 1) {
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                thiswins = false;
                                isthisrecieved = true;
                                dataOutputStream.writeUTF("lose");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread1.start();

                } else if (WordGuess.count == wordString.length()) {
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                isthisrecieved = true;
                                thiswins = true;
                                dataOutputStream.writeUTF("won");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread2.start();


                }
                if (isOk == false) {
                    Toast.makeText(WordGuess.this, "Wrong Inputs", Toast.LENGTH_SHORT).show();
                    TextView chances = findViewById(R.id.chance_count);
                    chances.setText("Chances Left = " + --WordGuess.chances);
                } else {
                    Toast.makeText(WordGuess.this, "Correct Inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkresult() {
        if (isotherrecieved && isthisrecieved) {
            if (thiswins) {
                if (isotherwinner) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                            alertDialog.setTitle("Draw");
                            if (RankedFragment.isranked)
                                score = 1;
                            alertDialog.setMessage("Oops!!            Draw");
                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                     wordString = null;
                                    chances = 0;
                                    letters = new EditText[6];
                                    count = 0;
                                    beginBool = false;
                                    thiswins = false;
                                    isthisrecieved = false;
                                    isotherwinner = false;
                                    isotherrecieved = false;
                                    startActivity(new Intent(WordGuess.this, MainPage.class));
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
                    return true;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                            alertDialog.setTitle("You Win");
                            if (RankedFragment.isranked)
                                score = 3;
                            alertDialog.setMessage("Congratulations You won this round");
                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    wordString = null;
                                    chances = 0;
                                   letters = new EditText[6];
                                     count = 0;
                                    beginBool = false;
                                    thiswins = false;
                                    isthisrecieved = false;
                                    isotherwinner = false;
                                    isotherrecieved = false;
                                    startActivity(new Intent(WordGuess.this, MainPage.class));
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
                    return true;

                }
            } else {
                if (!isotherwinner) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                            alertDialog.setTitle("Draw");
                            if (RankedFragment.isranked)
                                score = 1;
                            alertDialog.setMessage("Oops!!            Draw");
                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    wordString = null;
                                    chances = 0;
                                    letters = new EditText[6];
                                    count = 0;
                                    beginBool = false;
                                   thiswins = false;
                                     isthisrecieved = false;
                                    isotherwinner = false;
                                    isotherrecieved = false;
                                    startActivity(new Intent(WordGuess.this, MainPage.class));
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
                    return true;
                }
                if (isotherwinner) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                            alertDialog.setTitle("You Lose");
                            if (RankedFragment.isranked)
                                score = 0;
                            alertDialog.setMessage("oh , You Lost this round");
                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    wordString = null;
                                    chances = 0;
                                    letters = new EditText[6];
                                    count = 0;
                                    beginBool = false;
                                    thiswins = false;
                                    isthisrecieved = false;
                                    isotherwinner = false;
                                    isotherrecieved = false;
                                    startActivity(new Intent(WordGuess.this, MainPage.class));
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
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}





    /*
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
        alertDialog.setTitle("You Won");
        alertDialog.setMessage("Congratulations You won this round");
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alertDialog.show();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
        alertDialog.setTitle("You Lost");
        alertDialog.setMessage("oh , You Lost this round");
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        alertDialog.show();


                /*
                if ((isfirst && round1)||(!isfirst&&!round1)) {
                    System.out.println("slm");
                    Thread listener = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!isgamefinished) {
                                    trueguess = 0;
                                    democount = 0;
                                    isok = true;
                                    int loc = dataInputStream.readInt();
                                    String guess = dataInputStream.readUTF();
                                    System.out.println(loc);
                                    System.out.println(guess);
                                    if (guess.charAt(0) != wordString.charAt(loc) && guess.charAt(0) != '2') {
                                        letters[loc].setText(guess);
                                        guess = "2";
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                chancestextview.setText("Chances Left = " + WordGuess.chances);
                                            }
                                        });
                                        isok = false;
                                    } else if (guess.charAt(0) != '2') {
                                        letters[loc].setText(guess);
                                        guess = "2";
                                    }
                                    for (int i = 0; i < wordString.length(); ++i) {
                                        if (letters[i].length() != 0) {
                                            if (letters[i].getText().charAt(0) == wordString.charAt(i)) {
                                                trueguess++;
                                            }
                                            if (letters[i].getText().charAt(0) != wordString.charAt(i)) {
                                                WordGuess.chances--;
                                            }
                                        }
                                    }
                                    if (trueguess == wordString.length()) {
                                        thisturn = false;
                                        isgamefinished = true;
                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                                        alertDialog.setTitle("You Lost");
                                        alertDialog.setMessage("oh , You Lost this round");
                                        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (round1) {
                                                    round1 = false;
                                                    Intent i = new Intent(WordGuess.this, WordGuess.class);
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(i);
                                                    overridePendingTransition(0, 0);
                                                } else if (!round1) {
                                                    Intent i = new Intent(WordGuess.this, MainPage.class);
                                                    finish();
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                System.exit(0);
                                            }
                                        });
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                alertDialog.show();
                                            }
                                        });
                                        break;
                                    }
                                    if (WordGuess.chances <= 1) {
                                        thisturn = false;
                                        isgamefinished = true;
                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WordGuess.this);
                                        alertDialog.setTitle("You Won");
                                        alertDialog.setMessage("Congratulations You won this round");
                                        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                thisturn = false;
                                                if (round1) {
                                                    round1 = false;
                                                    Intent i = new Intent(WordGuess.this, WordGuess.class);
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(i);
                                                    overridePendingTransition(0, 0);
                                                } else if (!round1) {
                                                    Intent i = new Intent(WordGuess.this, MainPage.class);
                                                    finish();
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                System.exit(0);
                                            }
                                        });
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                alertDialog.show();
                                            }
                                        });

                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    listener.start();
                }
            }
        });

                 */
        /*
        Thread finisher = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isgamefinished) {
                        if (isfirst) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < letters.length; i++) {
                                        letters[i].setEnabled(true);
                                        letters[i].setText("");
                                        letters[i].setVisibility(View.INVISIBLE);
                                    }
                                    start.setVisibility(View.INVISIBLE);
                                    wordEditText.setVisibility(View.INVISIBLE);
                                    player1.setText("Waiting for Player2");
                                    chancestextview.setVisibility(View.INVISIBLE);
                                }
                            });
                            try {
                                wordString = dataInputStream.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView line = findViewById(R.id.line);
                                    line.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < wordString.length(); i++) {
                                        letters[i].setVisibility(View.VISIBLE);
                                    }
                                    check.setVisibility(View.VISIBLE);
                                    chancestextview.setVisibility(View.VISIBLE);
                                    WordGuess.chances = wordString.length();
                                    chancestextview.setText("Chances Left = " + WordGuess.chances);
                                }
                            });
                            isgamefinished=false;
                            break;

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < letters.length; i++) {
                                        letters[i].setEnabled(true);
                                        letters[i].setText("");
                                        letters[i].setVisibility(View.INVISIBLE);
                                    }
                                    check.setVisibility(View.INVISIBLE);
                                    TextView line = findViewById(R.id.line);
                                    line.setVisibility(View.INVISIBLE);
                                    start.setEnabled(true);
                                    wordEditText.setEnabled(true);
                                    wordEditText.setText("");
                                    TextView chances = findViewById(R.id.chance_count);
                                    chances.setText("");
                                    chances.setVisibility(View.INVISIBLE);
                                    wordEditText.setVisibility(View.VISIBLE);
                                    start.setVisibility(View.VISIBLE);
                                    WordGuess.count = 0;
                                    WordGuess.chances = 0;
                                }
                            });
                            isgamefinished=false;
                          break;

                        }


                    }
                }
            }
        });
        finisher.start();

         */