package com.example.spokenglovesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class SignsFragment extends Fragment {
    ListView signList;
    Button btnAdd;
    SignsListAdapter signsListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_signs, container, false);
        signList=view.findViewById(R.id.signList);
        signsListAdapter=new SignsListAdapter(getContext());
        signList.setAdapter(signsListAdapter);
        btnAdd=view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                AddDialog addDialog = new AddDialog();
                addDialog.show(manager, null);



            }
        });
        return view;
    }
}