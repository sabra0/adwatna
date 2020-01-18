package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class WelcomePage extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);


        /*//Actionbar (not working)

        //show actionbar
        getSupportActionBar().show();

        ActionBar actionBar = getSupportActionBar();

        //set title
        actionBar.setTitle("Tools list");*/

        //recycler view
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Data");

    }

    //search
    private void fireBaseSearch (String searchText){

        Query fireBaseSearchQuery = mRef.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");

        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row_item,
                        ViewHolder.class,
                        fireBaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(),model.getImage());


                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //load data into recycler view on start
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row_item,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(),model.getImage());
                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fireBaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter as you type in search
                fireBaseSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //for option item in activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==1){
            //do some thing
        }
        return super.onOptionsItemSelected(item);
    }
}
