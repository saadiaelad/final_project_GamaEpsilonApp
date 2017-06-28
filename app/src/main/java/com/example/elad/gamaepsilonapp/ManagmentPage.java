package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ManagmentPage extends AppCompatActivity implements ValueEventListener {

    private TextView nameText;
    private Button addRemoveUser;
    private Button addRemoveSupervisor;
    private Button signOutButton;
    private Button backButton;
    private Button addRemoveWork;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseUser currentUser = mAuth.getCurrentUser();;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("userTable");
    private String email = currentUser.getEmail();;
    private String username;
    private ProgressDialog mProgressDialog;
    private Query q = dataRef.orderByChild("userMail").equalTo(email);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managment_page);

        nameText = (TextView)findViewById(R.id.nameText);
        addRemoveUser = (Button)findViewById(R.id.addRemoveUser);
        addRemoveSupervisor = (Button)findViewById(R.id.addRemoveSupervisor);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        backButton = (Button)findViewById(R.id.backButton);
        addRemoveWork = (Button)findViewById(R.id.addRemoveWork);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("טוען...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        addRemoveUser.setOnClickListener(new OnClickListener());
        addRemoveSupervisor.setOnClickListener(new OnClickListener());
        signOutButton.setOnClickListener(new OnClickListener());
        backButton.setOnClickListener(new OnClickListener());
        addRemoveWork.setOnClickListener(new OnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        q.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d: dataSnapshot.getChildren()){
            if (d.child("firstName").getValue() != null) {
                username = (String) d.child("firstName").getValue();
                nameText.setText("שלום " + username);
                while (nameText.getText().equals(""));
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == addRemoveUser.getId())
                startActivityButton(2);
            else if (v.getId() == addRemoveSupervisor.getId())
                startActivityButton(3);
            else if (v.getId() == signOutButton.getId()){
                mAuth.signOut();
                startActivityButton(1);
            }
            else if (v.getId() == backButton.getId())
                startActivityButton(4);
            else if (v.getId() == addRemoveWork.getId())
                startActivityButton(5);
        }
    }

    private void startActivityButton(int butt) {
        if (butt == 1) {
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 2) {
            Intent i = new Intent(this, AddRemoveUser.class);
            finish();
            startActivity(i);
        }
        else if (butt == 3) {
            Intent i = new Intent(this, AddRemoveSupervisor.class);
            startActivity(i);
        }
        else if (butt == 4) {
            Intent i = new Intent(this, MainPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 5) {
            Intent i = new Intent(this, AddRemoveWork.class);
            startActivity(i);
        }
    }
}
