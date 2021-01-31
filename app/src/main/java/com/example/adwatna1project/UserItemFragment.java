package com.example.adwatna1project;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserItemFragment extends Fragment {

    static RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference allDataReference;
    String currentUserId;
    FirebaseAuth auth;
    DatabaseReference allDataRef;


    public UserItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_user_item, container, false);

        //recycler view
        mRecyclerView=rootView.findViewById(R.id.all_items_rv);
        // mRecyclerView.setHasFixedSize(true);


        int numberOfColumns =3;
        //set layout as grid layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        //send query to fireBase database
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        allDataReference=mFirebaseDatabase.getReference("Users").child(currentUserId).child("MyItems");
        allDataRef=mFirebaseDatabase.getReference("Data");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(allDataReference,Model.class)
                .build();

        final FirebaseRecyclerAdapter<Model, ViewHolder> adapter =
                new FirebaseRecyclerAdapter<Model,ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Model model) {
                        holder.tittleTextView.setText(model.getTitle());
                        holder.priceTextView.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.itemImageView);

                        final DatabaseReference itemRef = getRef(position);
                        final String myKey = itemRef.getKey();

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                //Toast.makeText(getContext(), "Testttttt", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure to delete this item?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        allDataReference.child(myKey).removeValue();
                                        allDataRef.child(myKey).removeValue();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                                return false;
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
}
