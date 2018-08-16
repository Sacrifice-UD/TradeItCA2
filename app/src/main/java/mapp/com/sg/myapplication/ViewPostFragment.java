package mapp.com.sg.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewPostFragment extends AppCompatActivity {

    //widgets
    private TextView mContactEmail, mCountry, mDescription, mPhone, mStateProvince, mTitle, mTrade;
    private ImageView back, mImage;
    private Button contactBtn;

    //vars
    private String Image, PostId, contactEmail;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_post);
        getIncomingIntent();
        back = findViewById(R.id.backButton);
        init();
        loadData();

//        mTitle.setText("hello");
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("mId")){
            PostId = getIntent().getStringExtra("mId");
        }
    }

    private void setImage(String mId){
        TextView title = findViewById(R.id.view_post_title);
        title.setText(mId);
    }

    private void init (){

        // for upbutton
        back.setClickable(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //for floating action btn
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToWatchList();
                Snackbar.make(view, "Added to watchlist", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //for contact email button
        contactBtn = findViewById(R.id.btn_contact);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {contactEmail});
                startActivity(emailIntent);
            }
        });
    }

    private void addItemToWatchList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(getString(R.string.node_watch_list))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(PostId)
                .child(getString(R.string.field_post_id))
                .setValue(PostId);
    }

    private void loadData(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("posts").child(PostId);

        mContactEmail = findViewById(R.id.view_post_email);
        mCountry = findViewById(R.id.view_post_country);
        mDescription = findViewById(R.id.view_post_description);
        mPhone = findViewById(R.id.view_post_phone);
        mStateProvince = findViewById(R.id.view_post_state_province);
        mTitle = findViewById(R.id.view_post_title);
        mTrade = findViewById(R.id.view_post_for);
        mImage = findViewById(R.id.view_post_image);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTitle.setText(dataSnapshot.child("title").getValue(String.class));
                mTrade.setText(dataSnapshot.child("trade").getValue(String.class));
                mStateProvince.setText(dataSnapshot.child("state_province").getValue(String.class));
                mPhone.setText(dataSnapshot.child("phone").getValue(String.class));
                mDescription.setText(dataSnapshot.child("description").getValue(String.class));
                mCountry.setText(dataSnapshot.child("country").getValue(String.class));
                contactEmail = dataSnapshot.child("contact_email").getValue(String.class);
                mContactEmail.setText(contactEmail);
                Image = dataSnapshot.child("image").getValue(String.class);

                Glide.with(ViewPostFragment.this)
                        .asBitmap()
                        .load(Image)
                        .into(mImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
