package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WelcomePage extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference allDataReference;
    ImageView searchImageView,backImageView;
    EditText editText;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        editText =findViewById(R.id.search_text);
        searchImageView=findViewById(R.id.search_btn);
        backImageView=findViewById(R.id.back_to_all_btn);

        //recycler view
        mRecyclerView=findViewById(R.id.recyclerView);
       // mRecyclerView.setHasFixedSize(true);

        int numberOfColumns =3;
        //set layout as linear layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        allDataReference=mFirebaseDatabase.getReference("Data");

        //search on click on search icon
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getVisibility()==View.GONE)
                {
                    editText.setVisibility(View.VISIBLE);
                    //todo : change search icon color
                }
                else {
                    //after writing text to search for
                    text = editText.getText().toString();
                    fireBaseSearch(text);
                    editText.setVisibility(View.GONE);
                    backImageView.setVisibility(View.VISIBLE);
                }
            }
        });
        //after searching for specific item get back to all list
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireBaseSearch(null);//search again for all items
                backImageView.setVisibility(View.GONE);
            }
        });
    }
    //search function
    private void fireBaseSearch (String searchText){

        Query fireBaseSearchQuery = allDataReference.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");

        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row_item,
                        ViewHolder.class,
                        fireBaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getPrice(),model.getImage());
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
                        allDataReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getPrice(),model.getImage());
                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
// for actionBar to make search button and options menu

    /*@Override
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
    }*/
}
