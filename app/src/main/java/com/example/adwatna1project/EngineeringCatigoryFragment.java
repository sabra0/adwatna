package com.example.adwatna1project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EngineeringCatigoryFragment extends Fragment {
    static RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference allDataReference;

    public EngineeringCatigoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_engineering_catigory, container, false);

        //recycler view
        mRecyclerView=rootView.findViewById(R.id.engineering_category_rv);
        // mRecyclerView.setHasFixedSize(true);

        int numberOfColumns =3;
        //set layout as grid layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        allDataReference=mFirebaseDatabase.getReference("Data2").child("engineering");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row_item,
                        ViewHolder.class,
                        allDataReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getActivity(),model.getTitle(),model.getPrice(),model.getImage());
                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
