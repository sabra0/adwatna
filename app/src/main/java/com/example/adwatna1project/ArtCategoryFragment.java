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

public class ArtCategoryFragment extends Fragment {
    static RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference artDatataReference;

    ImageView searchImageView,backImageView;
    EditText searchEditText;
    String text;

    FirebaseRecyclerAdapter<Model,ViewHolder> adapter;

    public ArtCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_art_category, container, false);

        //recycler view
        mRecyclerView=rootView.findViewById(R.id.art_category_rv);
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
        artDatataReference =mFirebaseDatabase.getReference("Data2").child("art");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(artDatataReference,Model.class)
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
                                Intent intent = new Intent(getContext(),ItemDetailsActivity.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("description",model.getDescription());
                                intent.putExtra("price",model.getPrice());
                                Drawable mDrawable = holder.itemImageView.getDrawable();
                                if(mDrawable!=null){
                                    Bitmap bitmap = ((BitmapDrawable)mDrawable).getBitmap();
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                    byte[] bytes = byteArrayOutputStream.toByteArray();
                                    intent.putExtra("image",bytes);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getActivity(), "please wait!", Toast.LENGTH_SHORT).show();
                                }

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

    //search function
    private void fireBaseSearch (String searchText){

        Query fireBaseSearchQuery = artDatataReference.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");

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

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseRecyclerAdapter<Model,ViewHolder2> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Model, ViewHolder2>(
//                        Model.class,
//                        R.layout.row_item,
//                        ViewHolder2.class,
//                        artDatataReference
//                ) {
//                    @Override
//                    protected void populateViewHolder(ViewHolder2 viewHolder, Model model, int i) {
//
//                        viewHolder.setDetails(getActivity(),model.getTitle(),model.getPrice(),model.getImage());
//                    }
//                };
//        //set adapter to recyclerView
//        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
}
