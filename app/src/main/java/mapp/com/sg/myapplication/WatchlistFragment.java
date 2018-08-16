package mapp.com.sg.myapplication;


import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WatchlistFragment extends Fragment {
    // Add firebase database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    FirebaseAuth mAuth;

    //vars
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mId = new ArrayList<>();
    private ArrayList<String> mForTitle = new ArrayList<>();
    private ArrayList<String> mPostId = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        // get reference
        mRecyclerView = view.findViewById(R.id.recycler_view);

        init();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mListener);
    }

    private void init() {
        setupPostList();

        mReference = FirebaseDatabase.getInstance().getReference()
                .child("watch_list")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mReference.addValueEventListener(mListener);
    }

    private void setupPostList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        // create adapter
        mAdapter = new RecyclerViewAdapter(this.getContext(), mTitle, mImage, mId, mForTitle);
        //set adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getWatchListIds() {
        if (mPostId != null) {
            mPostId.clear();
            mTitle.clear();
            mImage.clear();
            mId.clear();
            mForTitle.clear();
        }
        mPostId = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("watch_list")
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren().iterator().hasNext()) {
                    DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                    for (DataSnapshot snapshot : singleSnapshot.getChildren()) {
                        String id = snapshot.child(getString(R.string.field_post_id)).getValue().toString();
                        mPostId.add(id);
                    }
                    getPosts();
                } else {
                    getPosts();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPosts() {
        if (mPostId.size() > 0) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            for (int i = 0; i < mPostId.size(); i++) {
                Query query = reference.child(getString(R.string.node_posts))
                        .orderByKey()
                        .equalTo(mPostId.get(i));

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                        Post post = singleSnapshot.getValue(Post.class);
                        mTitle.add(post.getTitle());
                        mImage.add(post.getImage());
                        mForTitle.add(post.getTrade());
                        mId.add(post.getPost_id());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    ValueEventListener mListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            getWatchListIds();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
