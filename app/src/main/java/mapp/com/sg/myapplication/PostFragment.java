package mapp.com.sg.myapplication;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    private double mProgress = 0;

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

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);

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
                    }
                } else {
                    Toast.makeText(getActivity(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadNewPhoto(Bitmap bitmap) {
        Log.d(TAG, "uploadNewPhoto: uploading a new image bitmap to storage");
        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    private void uploadNewPhoto(Uri imagePath) {
        Log.d(TAG, "uploadNewPhoto: uploading a new image uri to storage.");
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(imagePath);
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
            //check compression
            Log.d(TAG, "doInBackground: megabytes before compression " + mBitmap.getByteCount() / 1000000);
            bytes = getBytesFromBitmap(mBitmap, 100);
            Log.d(TAG, "doInBackground: megabytes after compression " + bytes.length / 1000000);
            return bytes;

        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;
            hideProgressBar();
            //execute the upload task
            executeuploadTask();
        }
    }

    private void executeuploadTask() {
        Toast.makeText(getActivity(), "Uploading Image..", Toast.LENGTH_SHORT).show();

        final String postId = FirebaseDatabase.getInstance().getReference().push().getKey();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("posts/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +
                        "/" + postId + "/post_image");

        //upload the file
        UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        //on Successful
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Post Success", Toast.LENGTH_SHORT).show();

                //insert the download url into the firebase database
                Uri firebaseUri = taskSnapshot.getDownloadUrl();

                Log.d(TAG, "onSuccess: firebase download url: " + firebaseUri.toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Post post = new Post();
                post.setImage(firebaseUri.toString());
                post.setPhone(mPhone.getText().toString());
                post.setContact_email(mContactEmail.getText().toString());
                post.setCountry(mContactEmail.getText().toString());
                post.setDescription(mDescription.getText().toString());
                post.setPost_id(postId);
                post.setTrade(mFor.getText().toString());
                post.setState_province(mStateProvince.getText().toString());
                post.setTitle(mTitle.getText().toString());
                post.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(getString(R.string.node_posts))
                        .child(postId)
                        .setValue(post);

                resetFields();

            }
            //on Failure
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show();
            }
            //on Progress
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //show the progress on how much % is completed
                double currentProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //this limits the amount being printed out
                if (currentProgress > (mProgress + 15)) {
                    mProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.d(TAG, "onProgress: upload is " + mProgress + "& done");
                    Toast.makeText(getActivity(), mProgress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //convert and compress to the quality
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
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