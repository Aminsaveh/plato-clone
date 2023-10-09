package com.example.ap_plato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //**************************
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        de.hdodenhof.circleimageview.CircleImageView imageView = toolbar.findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        textView.setText("Profile");
        textView.setClickable(false);
        setSupportActionBar(toolbar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this , Profile.class));
                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            }
        });
        setSupportActionBar(toolbar);
        //***************************
        ArrayList<SettingsInfo> list = new ArrayList<SettingsInfo>();
        list.add(new SettingsInfo("About Us"));
        list.add(new SettingsInfo("Log Out"));
        ListView listView = findViewById(R.id.settingsListView);
        SettingsAdapter adapter = new SettingsAdapter(this, R.layout.settings_info_item_layout , list);
        listView.setAdapter(adapter);

    }
}
