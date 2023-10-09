package com.example.ap_plato.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import androidx.fragment.app.Fragment;
import com.example.ap_plato.MainActivity;
import com.example.ap_plato.MainPage;
import com.example.ap_plato.R;
import com.example.ap_plato.Signup;

import java.io.IOException;

public class HomeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment , container , false);
        Button signupbtn = root.findViewById(R.id.SignupBtn);
        TextView hinttext = root.findViewById(R.id.Hinttext);
        if(MainActivity.username.startsWith("Guest")){
            signupbtn.setVisibility(View.VISIBLE);
            hinttext.setVisibility(View.VISIBLE);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return root;
    }
    public void moveToSignUpfromMain(View view){

        startActivity(new Intent(view.getContext() , Signup.class));

    }
}
