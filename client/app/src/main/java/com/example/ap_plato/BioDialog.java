package com.example.ap_plato;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;


import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BioDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private BioDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.bio_dialog_layout, null);

        builder.setView(view)
                .setTitle("Set Bio")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bio = editTextUsername.getText().toString();
                        listener.applyTexts(bio , "");
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket socket = new Socket(MainActivity.ipAddress , MainActivity.portNumber);
                                    DataOutputStream  dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                    dataOutputStream.writeUTF("Bio");
                                    dataOutputStream.writeUTF(MainActivity.username);
                                    dataOutputStream.writeUTF(editTextUsername.getText().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })).start();
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_bio);

        return builder.create();
    }

    @Override
    public void onAttach(Activity Activity) {
        super.onAttach(Activity);
        try {
            listener = (BioDialogListener) Activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(Activity.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface BioDialogListener {
        void applyTexts(String username, String password);
    }
}