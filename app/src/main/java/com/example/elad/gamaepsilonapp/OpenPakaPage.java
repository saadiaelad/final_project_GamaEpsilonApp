package com.example.elad.gamaepsilonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

public class OpenPakaPage extends AppCompatActivity implements ValueEventListener{

    private String permission;
    private TextView nameText;
    private ImageButton addPakaButton;
    private ListView pakaList;
    private Button backButton;
    private Button signOutButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private onClickListener cl = new onClickListener();
    private String username;
    private User thisUser = new User();
    private Paka paka = new Paka();
    private String pakaParent = "";

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
        addPakaButton.setOnClickListener(cl);
        backButton.setOnClickListener(cl);
        signOutButton.setOnClickListener(cl);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefUser.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d: dataSnapshot.getChildren()){
            username = d.child("first name").getValue(String.class);
            nameText.setText("שלום " + username);
            if (((String)d.child("userMail").getValue()).equals(currentUser.getEmail()))
                permission = (String)d.child("permission").getValue();
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
        if (!isAdmin()){
            View v = new View(getApplicationContext());
            v.getRootView().findViewById(R.id.profitSumText);
            v.setVisibility(v.INVISIBLE);
        }
    }

    private boolean isAdmin() {
        if (permission.equals("מנהל ראשי"))
            return true;
        return false;
    }

    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == addPakaButton.getId())
                startActivityButton(3);
            else if (v.getId() == backButton.getId())
                startActivityButton(4);
            else if (v.getId() == signOutButton.getId())
            {
                mAuth.signOut();
                startActivityButton(1);
            }
        }
    }


    private void startActivityButton(int butt) {
        if (butt == 1) {
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 2) {
            Intent i = new Intent(this, PakaPage.class);
            i.putExtra("pakaKey", pakaParent);
            finish();
            startActivity(i);
        }
        else if (butt == 3) {
            Intent i = new Intent(this, AddPakaPage.class);
            startActivity(i);
        }
        else if (butt == 4){
            Intent i = new Intent(this, MainPage.class);
            finish();
            startActivity(i);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id)
    {
        startActivityButton(2);
    }


}
