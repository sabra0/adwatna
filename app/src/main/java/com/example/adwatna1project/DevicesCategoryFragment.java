package com.example.adwatna1project;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class DevicesCategoryFragment extends Fragment {
    static RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference devicesDataReference;

    ImageView searchImageView,backImageView;
    EditText searchEditText;
    String text;

    FirebaseRecyclerAdapter<Model,ViewHolder> adapter;

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

        //search part
        searchImageView=getActivity().findViewById(R.id.search_btn);
        searchEditText =getActivity().findViewById(R.id.search_text);
        backImageView=getActivity().findViewById(R.id.back_to_all_btn);
        // search on click on search icon
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchEditText.getVisibility()==View.GONE)
                {
                    searchEditText.setVisibility(View.VISIBLE);
                    //todo : change search icon color
                }
                else {
                    //after writing text to search for
                    text = searchEditText.getText().toString();
                    fireBaseSearch(text);
                    searchEditText.setVisibility(View.GONE);
                    backImageView.setVisibility(View.VISIBLE);

                }
            }
        });
        //after searching for specific item get back to all list
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setAdapter(adapter);
                backImageView.setVisibility(View.GONE);
            }
        });

        int numberOfColumns =3;
        //set layout as grid layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        devicesDataReference =mFirebaseDatabase.getReference("Data2").child("devices");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(devicesDataReference,Model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Model,ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Model model) {

                        //to make loading gif not like just an image
                        Glide.with(getActivity())
                                .asGif()
                                .load(R.drawable.loading_image)
                                .into(holder.itemImageView);

                        holder.tittleTextView.setText(model.getTitle());
                        holder.priceTextView.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.itemImageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //using thread to prevent user from clicking on item until the image load
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try  {
                                            Intent intent = new Intent(getContext(),ItemDetailsActivity.class);
                                            intent.putExtra("title",model.getTitle());
                                            intent.putExtra("description",model.getDescription());
                                            intent.putExtra("price",model.getPrice());
                                            Drawable mDrawable = holder.itemImageView.getDrawable();
                                            Bitmap bitmap = ((BitmapDrawable)mDrawable).getBitmap();

//                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
//                                            byte[] bytes = byteArrayOutputStream.toByteArray();

                                            //Write file
                                            String filename = "bitmap.png";
                                            FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                            stream.close();

                                            intent.putExtra("image",filename);
                                            intent.putExtra("ownerID",model.getOwnerID());
                                            startActivity(intent);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread.start();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
                        ViewHolder viewHolder = new ViewHolder(view);
                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void fireBaseSearch (String searchText){

        Query fireBaseSearchQuery = devicesDataReference.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(fireBaseSearchQuery,Model.class)
                .build();


        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
                        ViewHolder viewHolder = new ViewHolder(view);
                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolder holder, int i, @NonNull final Model model) {
//                        viewHolder.setDetails(getActivity(),model.getTitle(),model.getPrice(),model.getImage());
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

                };

        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
}
