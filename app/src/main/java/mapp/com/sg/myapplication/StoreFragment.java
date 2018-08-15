package mapp.com.sg.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import mapp.com.sg.myapplication.PostInformation;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    // Add firebase database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    //vars
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mId = new ArrayList<>();

    public StoreFragment() {
        // Required empty public constructor
        initImageBitmaps();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_store, container, false);
        // get reference
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        // create adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter (this.getContext(),mTitle,mImage, mId);
        //set adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Toast.makeText(getActivity(),"Array size:" + mTitle.size(), Toast.LENGTH_LONG).show();
        return view;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            mTitle.add(snapshot.child("title").getValue(String.class));
            mImage.add(snapshot.child("image").getValue(String.class));
            mId.add(snapshot.child("post_id").getValue(String.class));
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mTitle.clear();
//        mImage.clear();
//    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        mTitle.clear();
//        mImage.clear();
//    }

    private void initImageBitmaps() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTitle.clear();
                mImage.clear();
                mId.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mTitle.add(snapshot.child("title").getValue(String.class));
                    mImage.add(snapshot.child("image").getValue(String.class));
                    mId.add(snapshot.child("post_id").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
