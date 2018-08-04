package mapp.com.sg.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();

        //adding Listener to button
        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

    }//end of onCreate method

    //loginUser method
    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //if email is empty
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }//End of if

        //if email does not look like an email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }//End of if

        //if password length is empty
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }//End of if

        //if password length is less than 6
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }//End of if

        //view the progressBar that had been set to gone
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //we need to finish because if we press "back button" from profile we come back to login screen
                    finish();
                    //opens a new activity
                    Intent intent = new Intent(LoginActivity.this, Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }//end of if-else statement
            }
        });//end of mAuth
    } //End of loginUser method

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }//end of if statement
    }//end of onStart method

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //when user press on the textView
            case R.id.textViewSignup:
                finish();
                //redirect to Register activity
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            //when user press on login button
            case R.id.btnLogin:
                //login the user
                loginUser();
                break;
        }//end of switch case
    }//end of onClick method

}//end of Login class
