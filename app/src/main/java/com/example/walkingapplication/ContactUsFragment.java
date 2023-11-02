package com.example.walkingapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ContactUsFragment extends Fragment {

    Button backBtn;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_contact_us, container, false);


        backBtn = view.findViewById(R.id.backButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment profileFrag = new ProfileFragment();
                FragmentTransaction fm= getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container, profileFrag).commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}