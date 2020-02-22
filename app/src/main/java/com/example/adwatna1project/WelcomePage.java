package com.example.adwatna1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WelcomePage extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference allDataReference;
    ImageView searchImageView,backImageView;
    ImageButton optionMenuImageView;
    EditText editText;
    String text;
    TextView allCategories,engineeringCategory,artCategory,electronicsCategory,
            booksCategory, devicesCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        auth = FirebaseAuth.getInstance();

        userValidation();

        editText =findViewById(R.id.search_text);
        searchImageView=findViewById(R.id.search_btn);
        backImageView=findViewById(R.id.back_to_all_btn);
        allCategories=findViewById(R.id.all_categories);
        engineeringCategory=findViewById(R.id.engineering_category);
        artCategory=findViewById(R.id.art_category);
        electronicsCategory=findViewById(R.id.electronics_category);
        booksCategory=findViewById(R.id.books_category);
        devicesCategory = findViewById(R.id.devices_category);

        optionMenuImageView = findViewById(R.id.option_menu_btn);
        optionMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(WelcomePage.this, optionMenuImageView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.toolbar_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(WelcomePage.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        return true;
                        int id = item.getItemId();
                        if (id == R.id.chat){
                            startActivity(new Intent(WelcomePage.this, DisplayChatWithUsersActivity.class));
                            return true;
                        }
                        if (id == R.id.upload_item){
                            startActivity(new Intent(WelcomePage.this, UploadItemActivity.class));
                            return true;
                        }
                        if (id == R.id.profile){
                            startActivity(new Intent(WelcomePage.this, ProfileActivity.class));
                            return true;
                        }
                        if (id == R.id.logout){
                            signOut();
                            return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }

        });

        //to open allCategories fragment by default
        getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new AllCategoriesFragment()).commit();
        allCategories.setTextColor(getResources().getColor(R.color.selectFragment));

        //send query to fireBase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        allDataReference=mFirebaseDatabase.getReference("Data");

        //search on click on search icon
    //        searchImageView.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //
    //                if (editText.getVisibility()==View.GONE)
    //                {
    //                    editText.setVisibility(View.VISIBLE);
    //                    //todo : change search icon color
    //                }
    //                else {
    //                    //after writing text to search for
    //                    text = editText.getText().toString();
    //                    fireBaseSearch(text);
    //                    editText.setVisibility(View.GONE);
    //                    backImageView.setVisibility(View.VISIBLE);
    //
    //                }
    //            }
    //        });
    //        //after searching for specific item get back to all list
    //        backImageView.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                fireBaseSearch(null);//search again for all items
    //                backImageView.setVisibility(View.GONE);
    //            }
    //        });

        //for all categories fragment
        allCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCategories.setTextColor(getResources().getColor(R.color.selectFragment));
                engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                artCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new AllCategoriesFragment()).commit();

            }
        });

        engineeringCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engineeringCategory.setTextColor(getResources().getColor(R.color.selectFragment));
                allCategories.setTextColor(getResources().getColor(R.color.colorBlack));
                artCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new EngineeringCatigoryFragment()).commit();

            }
        });

        artCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artCategory.setTextColor(getResources().getColor(R.color.selectFragment));
                allCategories.setTextColor(getResources().getColor(R.color.colorBlack));
                engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new ArtCategoryFragment()).commit();

            }
        });

        electronicsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                electronicsCategory.setTextColor(getResources().getColor(R.color.selectFragment));
                allCategories.setTextColor(getResources().getColor(R.color.colorBlack));
                engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                artCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new ElectronicsCategoryFragment()).commit();

            }
        });
        booksCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booksCategory.setTextColor(getResources().getColor(R.color.selectFragment));
                allCategories.setTextColor(getResources().getColor(R.color.colorBlack));
                engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                artCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new BooksCategoryFragment()).commit();

            }
        });

        devicesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicesCategory.setTextColor(getResources().getColor(R.color.selectFragment));
                allCategories.setTextColor(getResources().getColor(R.color.colorBlack));
                engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                artCategory.setTextColor(getResources().getColor(R.color.colorBlack));

                getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new DevicesCategoryFragment()).commit();

            }
        });

    }
    //search function
//    private void fireBaseSearch (String searchText){
//
//        Query fireBaseSearchQuery = artDatataReference.orderByChild("title").startAt(searchText).endAt(searchText + "uf8ff");
//
//        FirebaseRecyclerAdapter<Model,ViewHolder2> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Model, ViewHolder2>(
//                        Model.class,
//                        R.layout.row_item,
//                        ViewHolder2.class,
//                        fireBaseSearchQuery
//                ) {
//                    @Override
//                    protected void populateViewHolder(ViewHolder2 viewHolder, Model model, int i) {
//
//                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getPrice(),model.getImage());
//                    }
//                };
//       /* //open all categories fragment after search
//        getSupportFragmentManager().beginTransaction().replace(R.id.welcome_page_frame,new AllCategoriesFragment()).commit();
//        //set  horizontal scroll view colors
//        allCategories.setTextColor(getResources().getColor(R.color.colorAccent));
//        engineeringCategory.setTextColor(getResources().getColor(R.color.colorBlack));
//        artCategory.setTextColor(getResources().getColor(R.color.colorBlack));
//        electronicsCategory.setTextColor(getResources().getColor(R.color.colorBlack));
//        booksCategory.setTextColor(getResources().getColor(R.color.colorBlack));
//        devicesCategory.setTextColor(getResources().getColor(R.color.colorBlack));*/
//
//        //set adapter to recyclerView
//        AllCategoriesFragment.mRecyclerView.setAdapter(firebaseRecyclerAdapter);
//
//    }
    private void userValidation(){
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(WelcomePage.this,HomeActivity.class));
            finish();
        }
    }

    private void signOut(){
        auth.signOut();
        userValidation();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings){
//            Toast.makeText(WelcomePage.this, "Setting....", Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


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
