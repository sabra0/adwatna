package com.example.adwatna1project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  class ViewHolder extends RecyclerView.ViewHolder{
    public TextView tittleTextView , priceTextView;
    public ImageView itemImageView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tittleTextView = itemView.findViewById(R.id.rTitleTv);
        priceTextView = itemView.findViewById(R.id.rPriceTv);
        itemImageView = itemView.findViewById(R.id.rImageView);
    }
}
