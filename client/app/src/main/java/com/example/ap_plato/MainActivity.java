package com.example.ap_plato;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    DataOutputStream dos;
    DataInputStream dis;
    Socket socket = null;
    Bitmap imagebitmap;
    public static String username;
    public static int portNumber = 9999;
    public static String ipAddress = "192.168.1.6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button guestbutton = findViewById(R.id.GuestButton);
        setTitle("Main");
        Thread mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
//                try{
//
//                    System.out.println("slm");
//                }catch (IOException e){
//
//                }
            }
        });
        mainthread.start();
        guestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket(MainActivity.ipAddress, MainActivity.portNumber);
                            dis = new DataInputStream(socket.getInputStream());
                            dos = new DataOutputStream(socket.getOutputStream());
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            if(imagebitmap==null){
                                imagebitmap =BitmapFactory.decodeResource(getResources(), R.drawable.avatar_default);
                            }
                            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                            byte[] imageinbytes = baos.toByteArray();
                            System.out.println("dddddddddddd"+imageinbytes.length);
                            dos.writeUTF("Guest");
                            int len = imageinbytes.length;
                            System.out.println("llleeeeee"+len);
                            dos.writeInt(len);
                            dos.write(imageinbytes,0,imageinbytes.length);
                            MainActivity.username = dis.readUTF();
                            dos.flush();
                            dos.close();
                            baos.reset();
                        }catch (IOException e){

                        }
                    }
                });
                thread3.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(MainActivity.this, MainPage.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }
    public void moveToLogin(View view) {
        startActivity(new Intent(MainActivity.this, Login.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void moveToSignUp(View view) {
        startActivity(new Intent(MainActivity.this, Signup.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /*
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


        }
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
                    dos.close();
                    baos.reset();
                }catch (IOException e){

                }
            }
        });
        thread3.start();
    }
    */
}
