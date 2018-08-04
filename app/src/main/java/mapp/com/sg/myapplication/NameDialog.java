package mapp.com.sg.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameDialog extends AppCompatDialogFragment {

    EditText editTextName, editTextAge;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Build a View
        View view = inflater.inflate(R.layout.name_dialog, null);

        //initializing Views
        editTextName = view.findViewById(R.id.editTextEditName);
        editTextAge = view.findViewById(R.id.editTextEditAge);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        builder.setView(view).setTitle("Change Information")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String name = editTextName.getText().toString();
                        String age = editTextAge.getText().toString();
                        final userInformation userinformation = new userInformation(name,age);

                        if (name.isEmpty()) {
                            editTextName.setError("Name required");
                            editTextName.requestFocus();
                            return;
                        }

                        databaseReference.child(user.getUid()).setValue(userInformation.class);
                    }
                });


        //return the dialog
        return builder.create();
    }
}
