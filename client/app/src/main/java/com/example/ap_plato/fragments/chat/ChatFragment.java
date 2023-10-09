package com.example.ap_plato.fragments.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.ap_plato.MainActivity;
import com.example.ap_plato.fragments.people.PeopleFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap_plato.MainPage;
import com.example.ap_plato.R;
import com.example.ap_plato.User;
import com.example.ap_plato.fragments.people.PeopleAdapter;
import com.example.ap_plato.fragments.people.PeopleFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ChatFragment extends Fragment {
    static RecyclerView recyclerView = null;
    static RecyclerView.Adapter recyclerviewAdapter = null;
    static RecyclerView.LayoutManager recyclerViewLayoutManger = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getFragmentManager().popBackStack();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_fragment , container , false);
        if (PeopleFragment.friends != null){
            recyclerView = root.findViewById(R.id.mainRecyclerView);
            recyclerView = root.findViewById(R.id.mainRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerViewLayoutManger = new LinearLayoutManager(getActivity());
            recyclerviewAdapter = new ChatAdapter(PeopleFragment.friends);
            recyclerView.setLayoutManager(recyclerViewLayoutManger);
            recyclerView.setAdapter(recyclerviewAdapter);
        }
        else{
            System.out.println("The list is empty");
        }
        return root;
    }
}
