package com.example.spokenglovesapp;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.spokenglovesapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabLayout=findViewById(R.id.main_tabLayout);
     Thread thread=new Thread(){
         @Override
         public void run() {
             final ArrayList<Fragment> mFragmentArrayList=new ArrayList<>();
             mFragmentArrayList.add(new InfoFragment());
             mFragmentArrayList.add(new HomeFragment());
             mFragmentArrayList.add(new SignsFragment());

             final ArrayList<Integer> mFragmentIconList=new ArrayList<>();
             mFragmentIconList.add(R.drawable.info_40px);
             mFragmentIconList.add(R.drawable.home_40px);
             mFragmentIconList.add(R.drawable.hand_peace_40px);

             ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(MainActivity.this,mFragmentArrayList);
             binding.viewPager.setAdapter(viewPagerAdapter);
             TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy=new TabLayoutMediator.TabConfigurationStrategy() {
                 @Override
                 public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                     for(int i=0;i<=position;i++){
                         tab.setIcon(mFragmentIconList.get(position));
                     }
                 }
             };
             new TabLayoutMediator(binding.mainTabLayout,binding.viewPager,tabConfigurationStrategy).attach();
             //int tabPosition= tabLayout.getSelectedTabPosition();
             TabLayout.Tab tab=tabLayout.getTabAt(1);
             tab.select();
             tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
             TabLayout.Tab tab0=tabLayout.getTabAt(0);
             tab0.removeBadge();

             tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                 @Override
                 public void onTabSelected(TabLayout.Tab tab) {
                     int position = tab.getPosition();
                     tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
                 }

                 @Override
                 public void onTabUnselected(TabLayout.Tab tab) {
                     int position = tab.getPosition();
                     tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.writingColor), PorterDuff.Mode.SRC_IN);

                 }


                 @Override
                 public void onTabReselected(TabLayout.Tab tab) {
                     int position = tab.getPosition();
                     tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
                 }
             });
         }
     };
     thread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
    /*
    public void home(){
        Intent serintent=new Intent(MainActivity.this,connection_bckground.class);
        startService(serintent);

    }*/
}