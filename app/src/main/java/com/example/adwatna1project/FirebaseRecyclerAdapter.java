//package com.example.adwatna1project;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class FirebaseRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{
//    FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
//    DatabaseReference allDataReference=mFirebaseDatabase.getReference("Data");
//
//
//    com.firebase.ui.database.FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
//            new com.firebase.ui.database.FirebaseRecyclerAdapter<Model, ViewHolder>(
//                    Model.class,
//                    R.layout.row_item,
//                    ViewHolder.class,
//                    allDataReference
//            ) {
//                @Override
//                protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
//
//                    viewHolder.setDetails(model.getTitle(),model.getPrice(),model.getImage());
//                }
//                @NonNull
//                @Override
//                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                    //ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
//                    ViewHolder vh = new ViewHolder (v);
//                    //viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
//                    vh.setOnClickListener(new ViewHolder.ClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//
////                Toast.makeText(getActivity(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onItemLongClick(View view, int position) {
////                Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    return vh;
//                }
//            };
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
