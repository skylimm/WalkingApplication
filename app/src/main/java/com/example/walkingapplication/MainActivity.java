package com.example.walkingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    BottomNavigationView bottomNavigationView;
    PostingFragment postingFragment = new PostingFragment();
    RouteSuggestorFragment routeSuggestorFragment = new RouteSuggestorFragment();
    RouteHistoryFragment routeHistoryFragment = new RouteHistoryFragment();
    ProfileFragment profileFragment= new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser(); //get current user

        //if not active user/ user logged out, open login page
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,postingFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id=item.getItemId();
                if(id ==R.id.posting){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,postingFragment).commit();
                    return true;
                }
                else if(id== R.id.route_sug){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,routeSuggestorFragment).commit();
                    return true;
                }
                else if(id== R.id.route_hist){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,routeHistoryFragment).commit();
                    return true;
                }
                else if(id== R.id.profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}