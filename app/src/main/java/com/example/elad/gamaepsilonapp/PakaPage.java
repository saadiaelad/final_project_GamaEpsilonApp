package com.example.elad.gamaepsilonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PakaPage extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemClickListener {

    String countChange = "";
    private String pakaKey = null;
    private TextView nameText;
    private Spinner workOptionSpinner;
    private ListView worksList;
    private Button closePakaButton;
    private Button backButton;
    private Button signOutButton;
    private Button pakaDetailsButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("pakaTable");

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

        closePakaButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
        signOutButton.setOnClickListener(new onClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d: dataSnapshot.getChildren()){

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
            startActivity(i);
        }
    }

    @Override
        public void onItemClick(AdapterView<?> adapterVeiw, View v, int position, long id) {

        }
}
