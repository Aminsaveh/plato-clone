package com.example.ap_plato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ap_plato.fragments.game.CasualFragment;
import com.example.ap_plato.fragments.game.RankedFragment;
import com.example.ap_plato.fragments.game.RankingFragment;
import com.example.ap_plato.fragments.game.SectionsPageAdapter;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class Game extends AppCompatActivity {
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager mViewPager;
        public static String game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        game = title;
        TextView title1 = findViewById(R.id.gametitle);
        title1.setText(title);
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CasualFragment(),"Casual");
        adapter.addFragment(new RankedFragment(),"Ranked");
        adapter.addFragment(new RankingFragment(),"Ranking");
        viewPager.setAdapter(adapter);
    }
}