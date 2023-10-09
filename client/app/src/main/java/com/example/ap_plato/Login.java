package com.example.ap_plato;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login extends AppCompatActivity {
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket = null;
    String servermessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        Button login = findViewById(R.id.btn_login);
        final EditText username = findViewById(R.id.loginusernameInputEditText);
        final EditText password = findViewById(R.id.loginpasswordInputEditText);
        final TextInputLayout usernameInputLayout = findViewById(R.id.loginusernameInputLayout);
        final TextInputLayout passwordInputLayout = findViewById(R.id.loginpasswordInputLayout);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                   dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                }
                catch (IOException e ){

                }
            }
        });
        mainthread.start();
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!hasFocus)
                        try{
                            dataOutputStream.writeUTF("Login");
                            dataOutputStream.writeUTF("Username:" + username.getText());
                            servermessage = dataInputStream.readUTF();
                            if (servermessage.startsWith("Username")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        username.setError(servermessage);
                                        usernameInputLayout.setErrorEnabled(true);
                                        usernameInputLayout.setError(servermessage);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        usernameInputLayout.setErrorEnabled(false);
                                    }
                                });

                            }
                        }catch (IOException e){

                        }
                    }
                });
                thread.start();
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataOutputStream.writeUTF("Login");
                            dataOutputStream.writeUTF("Password:" + s.toString());
                            servermessage = dataInputStream.readUTF();
                            if (servermessage.startsWith("Password")) {
                                System.out.println("LLLLLLLLLL" + servermessage);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        passwordInputLayout.setErrorEnabled(true);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        passwordInputLayout.setErrorEnabled(false);
                                    }
                                });
                            }
                        } catch (IOException e) {

                        }

                    }
                });
                thread3.start();
                    }
                });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordInputLayout.isErrorEnabled()){
                    password.setError("Password Doesn't Exist");
                    passwordInputLayout.setError("Password Doesn't Exist");
                }
                 if(!usernameInputLayout.isErrorEnabled()) {
                        if(!passwordInputLayout.isErrorEnabled()) {
                            MainActivity.username = username.getText().toString();
                            startActivity(new Intent(Login.this, MainPage.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            username.setText("");
                            password.setText("");
                            password.clearFocus();
                            finish();
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                 }
                }

        });
    }
    public void moveToSignUp(View view){
        startActivity(new Intent(Login.this , Signup.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
