package mapp.com.sg.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements NameDialog.NameDialogListener {


    Button btnChangePassword, btnChangeName, btnSignOut;
    TextView textViewName;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    userInformation userinformation = new userInformation();

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Initializing database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //initializing views
        btnSignOut = (Button) view.findViewById(R.id.btnSignout);
        btnChangeName = (Button) view.findViewById(R.id.btnChangeName);
        textViewName = (TextView) view.findViewById(R.id.textViewName);


        //Signing out
        setupFirebaseListener();

        //Set up onCLickListener for Name
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }

        });


        //Set up onClick Listener for SignOut
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to sign out the user.");
                FirebaseAuth.getInstance().signOut();
            }
        });

        return view;

    }//End of onCreate method

    //when Opening the Dialog, and setting the value, this fragment will be changed
    public void openDialog() {
        Log.d(TAG, "onClick: opening dialog to change name");
        NameDialog nameDialog = new NameDialog();
        nameDialog.show(getFragmentManager(), "Change Name");
        nameDialog.setTargetFragment(AccountFragment.this, 1);
    }

    //When user is signing out
    private void setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up the auth state listener");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                    Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //Pressing back button wont do anything
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }//end of setupFirebaseListener method


    //onStart, retrieve the name from firebase and set it to TextView
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        DatabaseReference nameref;
        nameref = FirebaseDatabase.getInstance().getReference();
        nameref.child(getString(R.string.node_users))
                .child(user.getUid())
                .child("name")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewName.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database Error: " + databaseError);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    //apply name to textViewName
    @Override
    public void applyTexts(String name) {
        Log.d(TAG, "applyTexts: setting the name");
        textViewName.setText(name);
    }

}
