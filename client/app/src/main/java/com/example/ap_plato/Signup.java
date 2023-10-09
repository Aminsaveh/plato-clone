package com.example.ap_plato;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.textfield.TextInputLayout;

import java.io.*;
import java.net.Socket;

public class Signup extends AppCompatActivity {
    DataOutputStream dos;
    DataInputStream dis;
    Socket socket = null;
    String servermessage;
    Bitmap imagebitmap;
    ImageView profilepicture;
    private static final int RESULT_SELECT_PICTURE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign up");
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        profilepicture = findViewById(R.id.signup_image);
        final TextInputLayout usernameInputLayout = findViewById(R.id.signupusernameInputLayout);
        final TextInputLayout passwordInputLayout = findViewById(R.id.signuppasswordInputLayout);
        final EditText username = findViewById(R.id.signupusernameInputEditText);
        final EditText password = findViewById(R.id.signuppasswordInputEditText);
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                }catch (IOException e){

                }
            }
        });
        mainthread.start();
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dos.writeUTF("Sign Up");
                                if(MainActivity.username!=null&&MainActivity.username.startsWith("Guest")){
                                    dos.writeUTF("Guest:" + MainActivity.username);
                                }
                                dos.writeUTF("Username:" + username.getText().toString());
                                System.out.println(username.getText());
                                servermessage = dis.readUTF();
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
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().length()==0){
                    usernameInputLayout.setError("Username field is empty");
                    usernameInputLayout.setErrorEnabled(true);
                    username.setError("Username field is empty");
                }
                if(password.getText().length()<5){
                    passwordInputLayout.setError("Password must be at least 5 characters");
                    passwordInputLayout.setErrorEnabled(true);
                    password.setError("Password must be at least 5 characters");
                }
                if(username.getText().length()>0&&!usernameInputLayout.isErrorEnabled()){
                    usernameInputLayout.setErrorEnabled(false);
                }
                if(password.getText().length()>=5){
                    passwordInputLayout.setErrorEnabled(false);
                }
                if (!usernameInputLayout.isErrorEnabled()&&!passwordInputLayout.isErrorEnabled()) {
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("Sign Up");
                                dos.writeUTF("FinalUsername:" + username.getText().toString());
                                dos.writeUTF("Sign Up");
                                dos.writeUTF("FinalPassword:" + password.getText().toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread2.start();
                    try {
                        sendImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final EditText username = findViewById(R.id.signupusernameInputEditText);
                    MainActivity.username = username.getText().toString();
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(Signup.this, MainPage.class));
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
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_SELECT_PICTURE&&resultCode==RESULT_OK&&null!=data){
            Uri selectedimage  = data.getData();
            try {
                InputStream imagestream = getContentResolver().openInputStream(selectedimage);
                imagebitmap =   BitmapFactory.decodeStream(imagestream);
                profilepicture.setImageBitmap(imagebitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            String [] filepathcolumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedimage,filepathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex = cursor.getColumnIndex(filepathcolumn[0]);
            String picturepath =cursor.getString(columnindex);
           profilepicture.setImageBitmap(BitmapFactory.decodeFile(picturepath));
           */

        }
    }

    public void moveToLogin(View view) {
        startActivity(new Intent(Signup.this, Login.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void selectImage(View view){
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(checkPermission()==true) {
            startActivityForResult(intent, RESULT_SELECT_PICTURE);
        }
    }
    public  boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }
    public void sendImage() throws IOException {
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if(imagebitmap==null){
                        BitmapDrawable drawable = (BitmapDrawable) profilepicture.getDrawable();
                        imagebitmap = drawable.getBitmap();
                    }
                    imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageinbytes = baos.toByteArray();
                    System.out.println("dddddddddddd"+imageinbytes.length);
                    dos.writeUTF("Sign Up");
                    dos.writeUTF("Profile");
                    int len = imageinbytes.length;
                    System.out.println("llleeeeee"+len);
                    dos.writeInt(len);
                    dos.write(imageinbytes,0,imageinbytes.length);
                    dos.flush();
                    baos.reset();
                }catch (IOException e){

                }
            }
        });
        thread3.start();
    }
}