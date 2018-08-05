package mapp.com.sg.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    //widgets
    private ImageView mPostImage;
    private EditText mTitle, mDescription, mFor, mCountry, mPhoneNumber, mCity, mContactEmail;
    private Button mPost;
    private ProgressBar mProgressBar;


    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

}
