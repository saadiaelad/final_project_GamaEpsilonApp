package com.example.elad.gamaepsilonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PakaPage extends AppCompatActivity implements ValueEventListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    String countChange = "";
    private Paka paka;
    private ArrayList<String> worksArray = new ArrayList<>();
    private ArrayList<String> choosenWorksArray = new ArrayList<>();
    private String pakaKey = null;
    private TextView nameText;
    private Spinner workOptionSpinner;
    private ListView worksList;
    private Button closePakaButton;
    private Button backButton;
    private Button signOutButton;
    private Button pakaDetailsButton;
    private String username;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private DatabaseReference dataRefWorks = database.getReference("worksTable");


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
        pakaDetailsButton = (Button)findViewById(R.id.detailsButton);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("pakaKey") != null)
            pakaKey = bundle.getString("pakaKey");

        worksArray.add("הוסף עבודה");
        ArrayAdapter<String> adapterWorks = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, worksArray);
        workOptionSpinner.setAdapter(adapterWorks);
        workOptionSpinner.setOnItemSelectedListener(this);

        closePakaButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
        signOutButton.setOnClickListener(new onClickListener());
        pakaDetailsButton.setOnClickListener(new onClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefPaka.addValueEventListener(this);
        dataRefWorks.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey()) {
            case "userTable":{
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if (((String)d.child("userMail").getValue()).equals(currentUser.getEmail())){
                        username = (String)d.child("firstName").getValue();
                        nameText.setText("שלום " + username);
                    }
                }
            }
            case "pakaTable": {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getKey().equals(pakaKey)) {
                        paka = d.getValue(Paka.class);
                        setView();
                    }
                }
            }
            case "worksTable":{
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if (d.child("abbriviatedName").getValue() != null){
                        worksArray.add(d.child("abbriviatedName").getValue().toString());
                    }
                }
            }
        }
    }

    private void setView() {
        if (paka.getWorkPerfomed() != null){
            for (int i = 0 ; i > paka.getWorkPerfomed().size() ; i++){
                choosenWorksArray.add(paka.getWorkPerfomed().get(i));
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        choosenWorksArray.add(adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getId() == closePakaButton.getId()) {
                // send exl
                startActivityButton(1);
            }
            else if (v.getId() == backButton.getId()) {
                startActivityButton(1);
            }
            else if (v.getId() == signOutButton.getId()) {
                mAuth.signOut();
                startActivityButton(2);
            }
            else if (v.getId() == pakaDetailsButton.getId()){
                startActivityButton(3);
            }
        }
    }

    private void startActivityButton(int butt) {
        if (butt == 1){
            Intent i = new Intent(this, OpenPakaPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 2){
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 3){
            Intent i = new Intent(this, PakaDetails.class);
            if (pakaKey != null)
                i.putExtra("pakaKey", pakaKey);
            Toast.makeText(this, "" + pakaKey, Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    }
}
