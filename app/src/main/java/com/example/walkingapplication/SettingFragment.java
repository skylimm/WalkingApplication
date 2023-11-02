package com.example.walkingapplication;

import static android.widget.Toast.LENGTH_SHORT;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SettingFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    View view;
    Button saveBtn;
    Button backBtn;
    TextView emailHint;
    TextView weightHint;
    TextView heightHint;
    TextView email;
    TextView password;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_setting, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // point reference to database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // acquire email
        String email = user.getEmail();

        // extract name out of user's email to search database with "name@domain.com" -> "name"
        int atIndex = email.indexOf("@");
        String name = (atIndex >= 0) ? email.substring(0, atIndex) : "";

        emailHint = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        weightHint = view.findViewById(R.id.weight);
        heightHint = view.findViewById(R.id.height);

        // continuously retrieve values from database using email & update UI with database changes
        //the change is only reflected when you switch the view (e.g. go back then go settings again)
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Double weight = dataSnapshot.child("weight").getValue(Double.class);
                    Double height = dataSnapshot.child("height").getValue(Double.class);

                    // Update the UI components
                    emailHint.setHint(email);
                    weightHint.setHint(String.valueOf(weight));
                    heightHint.setHint(String.valueOf(height));
                } else {
                    Log.d("firebase", "Data not found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Error getting data", databaseError.toException());
            }
        };
        mDatabase.child("Users").child(name).addListenerForSingleValueEvent(userListener);
        ////////////// line 64 to 84 must come together ///////////////////////////

        saveBtn = view.findViewById(R.id.SaveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                            }
                        });

                // update email for authentication db
                if(!TextUtils.isEmpty(String.valueOf(emailHint.getText()))) {
                    // update authentication db
                    user.updateEmail(String.valueOf(emailHint.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });

                    // update realtime db
                    mDatabase.child("Users").child(name).child("email").setValue(String.valueOf(emailHint.getText()));
                }

                // update password
                if(!TextUtils.isEmpty(String.valueOf(password.getText()))) {
                    user.updatePassword(String.valueOf(password.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                            }
                        }
                    });
                    ;
                }

                // update height
                if(!TextUtils.isEmpty(String.valueOf(heightHint.getText()))) {
                    Double newHeight;
                    newHeight = Double.valueOf(String.valueOf(heightHint.getText()));
                    mDatabase.child("Users").child(name).child("height").setValue(newHeight);
                }

                // update weight
                if(!TextUtils.isEmpty(String.valueOf(weightHint.getText()))) {
                    Double newWeight;
                    newWeight = Double.valueOf(String.valueOf(weightHint.getText()));
                    mDatabase.child("Users").child(name).child("weight").setValue(newWeight);
                }

                Snackbar message = Snackbar.make(view, "Updated", LENGTH_SHORT);
                message.show();

            }
        });

        backBtn = view.findViewById(R.id.backBtn);

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