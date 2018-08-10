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
//    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference myRef = mFirebaseDatabase.getReference().child("posts");
//    //declare database reference object
//    mFirebaseDatabase = FirebaseDatabase.getInstance();
//    myRef = mFirebaseDatabase.getReference().child("posts");
    //vars
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("posts");
//        initImageBitmaps();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                                mTitle.add(dataSnapshot.child("-LJD001z78vjxi2ElHBb").child("title").getValue(String.class));
//                mImage.add(dataSnapshot.child("-LJD001z78vjxi2ElHBb").child("image").getValue(String.class));

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initImageBitmaps();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_store, container, false);
        // get reference
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        // create adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter (this.getContext(),mTitle,mImage);
        //set adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            mTitle.add(snapshot.child("title").getValue(String.class));
            mImage.add(snapshot.child("image").getValue(String.class));
//            PostInformation pInfo = new PostInformation();
//            pInfo.setTitle(snapshot.child("title").getValue(PostInformation.class).getTitle());
//            pInfo.setImage(snapshot.child("image").getValue(PostInformation.class).getTitle());
//
//            //adding to array
//            mTitle.add(pInfo.getTitle());
//            mImage.add(pInfo.getImage());
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mTitle.clear();
//        mImage.clear();
//    }

    private void initImageBitmaps() {
        mImage.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mTitle.add("Havasu Falls");

        mImage.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mTitle.add("Trondheim");
    }
        //add items to array list
//        public void onDataChange(DataSnapshot dataSnapshot) {
//                mTitle.add(dataSnapshot.child("-LJD001z78vjxi2ElHBb").child("title").getValue(String.class));
//                mImage.add(dataSnapshot.child("-LJD001z78vjxi2ElHBb").child("image").getValue(String.class));
//                showData(dataSnapshot);
//            }

    }
