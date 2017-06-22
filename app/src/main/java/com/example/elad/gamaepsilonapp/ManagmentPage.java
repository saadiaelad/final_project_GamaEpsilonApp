package com.example.elad.gamaepsilonapp;

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
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("user_table");
    private String username;
    private Query q = dataRef.orderByChild("first name").equalTo("אלעד");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managment_page);

        nameText = (TextView)findViewById(R.id.nameText);
        addRemoveUser = (Button)findViewById(R.id.addRemoveUser);
        addRemoveSupervisor = (Button)findViewById(R.id.addRemoveSupervisor);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        backButton = (Button)findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        addRemoveUser.setOnClickListener(new OnClickListener());
        addRemoveSupervisor.setOnClickListener(new OnClickListener());
        signOutButton.setOnClickListener(new OnClickListener());
        backButton.setOnClickListener(new OnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        q.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
            username = postSnapshot.child("first name").getValue(String.class);
            nameText.setText("שלום " + username);
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
                finish();
        }
    }

    private void startActivityButton(int butt) {
        if (butt == 1)
        {
            Intent i = new Intent(this, LoginPage.class);
            startActivity(i);
        }
        if (butt == 2)
        {
            Intent i = new Intent(this, addRemoveUser.class);
            startActivity(i);
        }
        if (butt == 3)
        {
            Intent i = new Intent(this, addRemoveSupervisor.class);
            startActivity(i);
        }
    }
}
