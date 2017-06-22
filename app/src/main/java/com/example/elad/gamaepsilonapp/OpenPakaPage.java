package com.example.elad.gamaepsilonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OpenPakaPage extends AppCompatActivity implements ValueEventListener{
    private TextView nameText;
    private ImageButton addPakaButton;
    private ListView pakaList;
    private Button backButton;
    private Button signOutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("user_table");
    private onClickListener cl = new onClickListener();
    private String username;
    private Query q;
    private User thisUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_paka_page);

        nameText = (TextView)findViewById(R.id.nameText);
        addPakaButton = (ImageButton)findViewById(R.id.addPakaButton);
        pakaList = (ListView)findViewById(R.id.pakasList);
        backButton = (Button)findViewById(R.id.backButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        thisUser.setEmail(mAuth.getCurrentUser().getEmail());
        q = dataRef.orderByChild("userEmail").equalTo(thisUser.getEmail());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        addPakaButton.setOnClickListener(cl);
        backButton.setOnClickListener(cl);
        signOutButton.setOnClickListener(cl);
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


    private void whoIstheUser() {
//        switch (currentUser.getUid()){
//            case "ZOjgToMldhejfnHeQPRb1sv1SLg2":
//                break;
//            case "Tq5SuScNhOXvBOqOsN1tDQBbz2o2":
//                break;
//            default:
//                updateUI(this.addPakaButton);
//        }
    }

    private void updateUI(View view) {
        addPakaButton.setVisibility(view.INVISIBLE);
    }

    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == addPakaButton.getId())
                startActivityButton(3);
            else if (v.getId() == backButton.getId())
                finish();
            else if (v.getId() == signOutButton.getId())
            {
                mAuth.signOut();
                startActivityButton(1);
            }
        }
    }


    private void startActivityButton(int butt) {
        if (butt == 1)
        {
            Intent i = new Intent(this, LoginPage.class);
            startActivity(i);
        }
        else if (butt == 2)
        {
            Intent i = new Intent(this, PakaPage.class);
            startActivity(i);
        }
        else if (butt == 3)
        {
            Intent i = new Intent(this, AddPakaPage.class);
            startActivity(i);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id)
    {
        startActivityButton(2);
    }


}
