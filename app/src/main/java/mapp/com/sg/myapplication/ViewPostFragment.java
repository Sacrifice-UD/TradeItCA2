//package mapp.com.sg.myapplication;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class ViewPostFragment extends Fragment {
//
//    private static final String TAG = "ViewPostFragment";
//
//    //widgets
//    private TextView mContactSeller, mTitle, mDescription, mPrice, mLocation, mSavePost;
//    private ImageView mClose, mWatchList, mPostImage;
//
//    //vars
//    private String mPostId;
//    private Post mPost;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPostId = (String) getArguments().get(getString(R.string.arg_post_id));
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_view_post, container, false);
//        mContactSeller = (TextView) view.findViewById(R.id.post_contact);
//        mTitle = (TextView) view.findViewById(R.id.post_title);
//        mDescription = (TextView) view.findViewById(R.id.post_description);
//        mPrice = (TextView) view.findViewById(R.id.post_price);
//        mLocation = (TextView) view.findViewById(R.id.post_location);
//        mClose = (ImageView) view.findViewById(R.id.post_close);
//        mWatchList = (ImageView) view.findViewById(R.id.add_watch_list);
//        mPostImage = (ImageView) view.findViewById(R.id.post_image);
//        mSavePost = (TextView) view.findViewById(R.id.save_post);
//
////        init();
////
////        hideSoftKeyboard();
//
//        return view;
//    }
//
//    private void hideSoftKeyboard(){
//        final Activity activity = getActivity();
//        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//}
