package mapp.com.sg.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameDialog extends AppCompatDialogFragment {

    EditText editTextName, editTextAge;
    NameDialogListener listener;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //initializing firebase authentication
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Build a View
        View view = inflater.inflate(R.layout.name_dialog, null);

        //initializing Views
        editTextName = view.findViewById(R.id.editTextEditName);
        editTextAge = view.findViewById(R.id.editTextEditAge);

        //When user presses the Cancel and OK button
        builder.setView(view)
                .setTitle("Change Information")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String name = editTextName.getText().toString();
                        String age = editTextAge.getText().toString();

                        changeName();
                        listener.applyTexts(name);
                    }
                });


        //return the dialog
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        try {
            listener = (NameDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement NameDialogListener");
        }
        super.onAttach(context);
    }//end of onAttach

    public interface NameDialogListener {
        void applyTexts(String name);
    }//end of NDL

    private void changeName() {

        FirebaseUser user = mAuth.getCurrentUser();

        userInformation userinformation = new userInformation();

        //get the name from the editTextName and age from userinformation method
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();


        //set the name and age
        userinformation.setName(name);
        userinformation.setAge(age);

        //store in firebase
        databaseReference.child(getString(R.string.node_users))
                .child(user.getUid())
                .setValue(userinformation);
    }//end of changeName method
}//end of NameDialog Class
