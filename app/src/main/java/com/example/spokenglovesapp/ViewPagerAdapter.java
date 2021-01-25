package com.example.spokenglovesapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

   ArrayList<Fragment> myFragmentList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> myFragmentList) {
        super(fragmentActivity);
        this.myFragmentList=myFragmentList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
      return myFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return myFragmentList.size();
    }
}
