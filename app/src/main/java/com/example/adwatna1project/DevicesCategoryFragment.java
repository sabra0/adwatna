package com.example.adwatna1project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class DevicesCategoryFragment extends Fragment {
    static RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference allDataReference;

    public DevicesCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_devices_category, container, false);

        //recycler view
        mRecyclerView=rootView.findViewById(R.id.devices_category_rv);
        // mRecyclerView.setHasFixedSize(true);

        int numberOfColumns =3;
        //set layout as grid layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        allDataReference=mFirebaseDatabase.getReference("Data2").child("devices");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(allDataReference,Model.class)
                .build();

        FirebaseRecyclerAdapter<Model, AllCategoriesFragment.ViewHolder> adapter =
                new FirebaseRecyclerAdapter<Model, AllCategoriesFragment.ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AllCategoriesFragment.ViewHolder holder, int position, @NonNull final Model model) {
                        holder.tittleTextView.setText(model.getTitle());
                        holder.priceTextView.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.itemImageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),ItemDetailsActivity.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("description",model.getDescription());
                                intent.putExtra("price",model.getPrice());
                                Drawable mDrawable = holder.itemImageView.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable)mDrawable).getBitmap();
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                intent.putExtra("image",bytes);
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AllCategoriesFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
                        AllCategoriesFragment.ViewHolder viewHolder = new AllCategoriesFragment.ViewHolder(view);
                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tittleTextView , priceTextView;
        public ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittleTextView = itemView.findViewById(R.id.rTitleTv);
            priceTextView = itemView.findViewById(R.id.rPriceTv);
            itemImageView = itemView.findViewById(R.id.rImageView);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Model, ViewHolder>(
//                        Model.class,
//                        R.layout.row_item,
//                        ViewHolder.class,
//                        allDataReference
//                ) {
//                    @Override
//                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
//
//                        viewHolder.setDetails(getActivity(),model.getTitle(),model.getPrice(),model.getImage());
//                    }
//                };
//        //set adapter to recyclerView
//        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
}
