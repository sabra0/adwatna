package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class WelcomePage extends AppCompatActivity {
    private static final String TAG = "MyMessigingService";

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
                            startActivity(new Intent(getApplicationContext(), DisplayChatWithUsersActivity.class));
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
                            status("offline");
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
        //for notification
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.w(TAG,"getInstanceId failed",task.getException());
                            return;
                        }
                        //get new instance id token
                        String token =task.getResult().getToken();

                        //log and toast
                        String msg ="instance id token: "+token;
                        Log.d(TAG,msg);
                        Toast.makeText(WelcomePage.this, "msg", Toast.LENGTH_SHORT).show();
                    }
                });

    }
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


    private void status(String status){
        allDataReference=FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        allDataReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {

        super.onPause();
        if(auth.getCurrentUser()!=null)
        {
            status("offline");
        }
    }


}
