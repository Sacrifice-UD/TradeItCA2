package mapp.com.sg.myapplication;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    //widgets
    private ImageView mPostImage;
    private EditText mTitle, mDescription, mFor, mCountry, mStateProvince, mPhone, mContactEmail;
    private Button mPost;
    private ProgressBar mProgressBar;

    //variables
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;

    public PostFragment() {
        // Required empty public constructor
    }

    //get from Selecting from gallery
    @Override
    public void getImagePath(Uri imagePath) {
        Log.d(TAG, "getImagePath: setting image to image view");
        UniversalImageLoader.setImage(imagePath.toString(), mPostImage);
        //Assign to global variable
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    //get from taking photo from camera
    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Log.d(TAG, "getImageBitmap: setting image to image view");
        mPostImage.setImageBitmap(bitmap);
        //Assign to a global variable
        mSelectedUri = null;
        mSelectedBitmap = bitmap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        mPostImage = view.findViewById(R.id.post_image);
        mTitle = view.findViewById(R.id.input_title);
        mDescription = view.findViewById(R.id.input_description);
        mFor = view.findViewById(R.id.input_for);
        mCountry = view.findViewById(R.id.input_country);
        mStateProvince = view.findViewById(R.id.input_state_province);
        mPhone = view.findViewById(R.id.input_phone);
        mContactEmail = view.findViewById(R.id.input_email);
        mPost = view.findViewById(R.id.btn_post);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();

        return view;
    }

    private void init() {

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog to choose new photo");

                SelectPhotoDialog dialog = new SelectPhotoDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(PostFragment.this, 1);
            }
        });


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to post...");
                //make sure all the fields are not empty before posting it
                if (!isEmpty(mTitle.getText().toString())
                        && !isEmpty(mDescription.getText().toString())
                        && !isEmpty(mFor.getText().toString())
                        && !isEmpty(mCountry.getText().toString())
                        && !isEmpty(mStateProvince.getText().toString())
                        && !isEmpty(mPhone.getText().toString())
                        && !isEmpty(mContactEmail.getText().toString())) {

                    //we have a bitmap and no Uri
                    if (mSelectedBitmap != null && mSelectedUri == null) {
                        uploadNewPhoto(mSelectedBitmap);
                    }
                    //we have no bitmap and a uri
                    else if (mSelectedBitmap == null && mSelectedUri != null) {
                        uploadNewPhoto(mSelectedUri);
                    }//end of nested if-else statement
                } else {
                    Toast.makeText(getActivity(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }//end of if-else statement
            }
        });//end of onClick listener
    }//end of init method

    private void uploadNewPhoto(Bitmap bitmap) {

    }

    private void uploadNewPhoto(Uri imagePath) {

    }

    //resizing the image/compressing the image
    //Doing it on the background task as it can slow down the Main UI

    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {
        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if (bitmap != null) {
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Image is being Compressed", Toast.LENGTH_SHORT).show();
            showProgressBar();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started");

            //if it is the Uri, convert it to bitmap and resize it
            //if it is a bitmap, no need to convert, but resize it
            if (mBitmap == null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), params[0]);
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: IOException" + e.getMessage());
                }
            }
            byte[] bytes = null;
            bytes = getBytesFromBitmap(mBitmap, 100);
            return bytes;

        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;
            hideProgressBar();
            //execute the upload task
        }
    }

    //convert and compress to the quality
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    private void resetFields() {
        UniversalImageLoader.setImage("", mPostImage);
        mTitle.setText("");
        mDescription.setText("");
        mFor.setText("");
        mCountry.setText("");
        mStateProvince.setText("");
        mPhone.setText("");
        mContactEmail.setText("");
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

}