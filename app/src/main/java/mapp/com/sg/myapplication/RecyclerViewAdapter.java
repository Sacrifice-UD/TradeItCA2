package mapp.com.sg.myapplication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
//    debug
    private static final String TAG = "RecyclerViewAdapter";
    //vars
    private ArrayList<String> mImageNames;
    private ArrayList<String> mImages;
    private ArrayList<String> mId;
    private Context mContext;

    //widgets
    private FrameLayout mFrameLayout;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> id ) {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
        mId = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.container);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //add glide
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.imageName.setText(mImageNames.get(position));

        //add onclick listener
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "You clicked on:" + mImageNames.get(position),
//                        Toast.LENGTH_LONG).show();

//                add intent here to send  to the post detail
//                Intent intent = new Intent(mContext, ViewPostFragment.class);

//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext,ViewPostFragment.class);
                intent.putExtra("mId", mId.get(position));
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SquareImageView image;
        TextView imageName;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
