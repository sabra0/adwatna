package com.example.adwatna1project;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;
    }

    //set details to recyclerView row
    public void setDetails(Context context,String titile, String desccription,String image){

        TextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        TextView mDetailsTv = mView.findViewById(R.id.rDescriptionTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);

        mTitleTv.setText(titile);
        mDetailsTv.setText(desccription);
        Picasso.get().load(image).into(mImageIv);
    }
}
