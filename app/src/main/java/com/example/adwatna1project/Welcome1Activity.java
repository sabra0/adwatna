package com.example.adwatna1project;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Welcome1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);


        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Language");
        arrayList.add("English");
        arrayList.add("Arabic");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 1) {

                    Toast.makeText(parent.getContext(),"You have selected English", Toast.LENGTH_SHORT).show();
                    setAppLocale("en");

                    restartActivity();

                } else if (pos == 2) {

                    Toast.makeText(parent.getContext(),"You have selected Arabic", Toast.LENGTH_SHORT).show();
                    setAppLocale("ar");

                    restartActivity();
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }
    // to change language
    public void setAppLocale (String localeCode) {
        Resources res=getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    // without this method the activity will not change after changing rhe language
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
