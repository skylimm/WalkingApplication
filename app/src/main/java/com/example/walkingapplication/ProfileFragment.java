package com.example.walkingapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    View view;
    View view2;
    Button contactUsBtn;
    Button settingBtn;
    Button logOutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView = view.findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        // acquire email
        String user_detail = user.getEmail();

        // extract name out of user's email to search database with "name@domain.com" -> "name"
        int atIndex = user_detail.indexOf("@");
        String username = (atIndex >= 0) ? user_detail.substring(0, atIndex) : "";

        if (user == null) {
            textView.setText("Null"); //gets user to input email
        }
        else {
            textView.setText(username); //gets user to input email
        }

        settingBtn = view.findViewById(R.id.SettingButton);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment settingFrag = new SettingFragment();
                FragmentTransaction fm= getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,settingFrag).commit();
            }
        });

        contactUsBtn =view.findViewById(R.id.ContactUsButton);
        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment contactUsFrag = new ContactUsFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container,contactUsFrag).commit();
            }
        });

        logOutBtn = view.findViewById(R.id.LogOutButton);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //signs out user from firebase

                //close current activity and open login activity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}