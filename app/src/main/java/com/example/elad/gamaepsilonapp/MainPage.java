package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity implements ValueEventListener{

    private TextView nameText;
    private Button openPakaButton;
    private Button searchButton;
    private Button managmentButton;
    private Button signOutButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("userTable");
    private String email = currentUser.getEmail();
    private String username;
    private ProgressDialog mProgressDialog;
    private Query q = dataRef.orderByChild("userMail").equalTo(email);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("טוען...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        nameText = (TextView)findViewById(R.id.nameText);
        openPakaButton = (Button)findViewById(R.id.openPakaButton);
        searchButton = (Button)findViewById(R.id.searchButton);
        managmentButton = (Button)findViewById(R.id.managmentButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        openPakaButton.setOnClickListener(new onClckListener());
        searchButton.setOnClickListener(new onClckListener());
        managmentButton.setOnClickListener(new onClckListener());
        signOutButton.setOnClickListener(new onClckListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        q.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
            username = (String)postSnapshot.child("firstName").getValue();
            nameText.setText("שלום " + username);
            while (nameText.getText().equals(""));
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    private class onClckListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == openPakaButton.getId()) {
                startActivityButton(1);
            }
            else if (v.getId() == searchButton.getId()) {
                startActivityButton(2);
            }
            else if (v.getId() == managmentButton.getId()) {
                startActivityButton(3);
            }
            else if (v.getId() == signOutButton.getId())
            {
                mAuth.signOut();
                startActivityButton(4);
            }
        }
    }

    private void startActivityButton(int butt) {
        if (butt == 1) {
            Intent i = new Intent(this, OpenPakaPage.class);
            i.putExtra("language", true);
            finish();
            startActivity(i);
        }
        else if (butt == 2) {
            Intent i = new Intent(this, SearchPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 3) {
            Intent i = new Intent(this, ManagmentPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 4){
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
    }
}
