package com.example.ap_plato;



import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        de.hdodenhof.circleimageview.CircleImageView imageView = toolbar.findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        textView.setText("About us");
        textView.setClickable(false);
        setSupportActionBar(toolbar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this , Settings.class));
                overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            }
        });
        setSupportActionBar(toolbar);
    }
}
