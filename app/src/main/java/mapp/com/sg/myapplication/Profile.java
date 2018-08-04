package mapp.com.sg.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private TextView textView;
    private EditText editTextName;
    private EditText editTextAge;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Initializing views
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextName = (EditText) findViewById(R.id.editTextDisplayName);
        buttonSave = (Button) findViewById(R.id.btnSave);
        textView = (TextView) findViewById(R.id.textViewName);

        //Load user information
        loadUserInformation();

        //Save user information
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

    }//end of onCreate method

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is not logged in
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }//end of onStart method


    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        String displayName = user.getDisplayName();

        if (user != null) {
            if (user.getDisplayName() != null) {
                startActivity(new Intent(this, MainActivity.class));
            }

        }
    }//end of loadUserInformation method

    private void saveUserInformation() {
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();

        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        final userInformation userinformation = new userInformation(name,age);

        final FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        databaseReference.child(user.getUid()).setValue(userinformation);
                        Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        //Pressing back wont go back to this screen
                        finish();
                        //opens a new activity
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }//end of onComplete method
            });//end of checking user Login
        }//end of if statement
    }//end of saveUserInformation class

}//end of Profile class
