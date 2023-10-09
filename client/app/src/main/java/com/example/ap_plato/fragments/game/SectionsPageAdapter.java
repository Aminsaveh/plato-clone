package com.example.ap_plato.fragments.game;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    public void addFragment(Fragment fragment , String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public SectionsPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
