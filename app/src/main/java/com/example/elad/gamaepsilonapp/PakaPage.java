package com.example.elad.gamaepsilonapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PakaPage extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView nameText;
    private Spinner workOptionSpinner;
    private ListView worksList;
    private Button closePakaButton;
    private Button backButton;
    private Button signOutButton;

    private String[] worksOptions = {
            "בור באספלט  3  מטרים",
            "בור בכורכר  1.5  מטרים",
            "השחלת כבל 11  140  מטרים",
            "שליפת כבל 11  280  מטרים"
    };
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paka_page);

        nameText = (TextView)findViewById(R.id.nameText);
        workOptionSpinner = (Spinner)findViewById(R.id.workOptionSpinner);
        worksList = (ListView)findViewById(R.id.worksList);
        closePakaButton = (Button)findViewById(R.id.closePakaButton);
        backButton = (Button)findViewById(R.id.backButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);

        closePakaButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
        signOutButton.setOnClickListener(new onClickListener());

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, worksOptions);
        worksList.setAdapter(arrayAdapter);
        worksList.setOnItemClickListener(this);
    }


    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getId() == closePakaButton.getId())
            {
                // send exl
                finish();
            }
            else if (v.getId() == backButton.getId())
            {
                finish();
            }
            else if (v.getId() == signOutButton.getId())
            {

            }
        }
    }

        @Override
        public void onItemClick(AdapterView<?> adapterVeiw, View v, int position, long id) {

        }
}
