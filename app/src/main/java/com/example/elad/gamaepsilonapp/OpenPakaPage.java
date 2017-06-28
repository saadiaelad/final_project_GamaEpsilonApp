package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class OpenPakaPage extends AppCompatActivity implements ValueEventListener{

    private ArrayList<String> pakaNumberList = new ArrayList<>();
    private ArrayList<String> addressList = new ArrayList<>();
    private String permission;
    private TextView nameText;
    private ImageButton addPakaButton;
    private ListView pakaNumberListView;
    private ListView addressListView;
    private Button backButton;
    private Button signOutButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private String email = currentUser.getEmail();
    private onClickListener cl = new onClickListener();
    private String username;
    private ProgressDialog mProgressDialog;
    private User thisUser = new User();
    private Paka paka = new Paka();
    private String pakaKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_paka_page);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("טוען...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        nameText = (TextView)findViewById(R.id.nameText);
        addPakaButton = (ImageButton)findViewById(R.id.addPakaButton);
        pakaNumberListView = (ListView)findViewById(R.id.pakaNumberListView);
        addressListView = (ListView)findViewById(R.id.addressListView);
        backButton = (Button)findViewById(R.id.backButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        thisUser.setEmail(mAuth.getCurrentUser().getEmail());
        addPakaButton.setOnClickListener(cl);
        backButton.setOnClickListener(cl);
        signOutButton.setOnClickListener(cl);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefUser.addValueEventListener(this);
        dataRefPaka.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey()){
            case ("userTable"): {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    username = (String)d.child("firstName").getValue();
                    nameText.setText("שלום " + username);
                    if (((String)d.child("userMail").getValue()).equals(currentUser.getEmail()))
                        permission = (String) d.child("permission").getValue();
                }
                updateUI();
                mProgressDialog.dismiss();
            }
            case ("pakaTable"): {
                switch (permission){
                    case ("מנהל ראשי"): {
                        for (DataSnapshot d:dataSnapshot.getChildren()){
                            if (d.child("openOrClose").getValue() != null){
                                if (!(boolean)d.child("openOrClose").getValue()) {
                                    Paka paka = new Paka();
                                    paka = d.getValue(Paka.class);
                                    addPakaToList(paka);
                                }
                            }
                        }
                    }
                    case ("מנהל צוות"): {
                        for (DataSnapshot d:dataSnapshot.getChildren()){
                            if (d.child("openOrClose").getValue() != null){
                                if (!(boolean)d.child("openOrClose").getValue()){
                                    if (d.child("teamLeader").getValue() != null) {
                                        ArrayList<String> job = new ArrayList<>();
                                        job = (ArrayList<String>) d.child("teamLeader").getValue();
                                        if (job.get(job.size() - 1).equals(username)) {
                                            Paka p = new Paka();
                                            p = d.getValue(Paka.class);
                                            addPakaToList(p);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addPakaToList(Paka paka) {
        pakaNumberList.add(paka.getPakaNum());
        addressList.add(paka.getAddress());
        final ArrayAdapter<String> openPakaNumberList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pakaNumberList);
        pakaNumberListView.setAdapter(openPakaNumberList);
        pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
        final ArrayAdapter<String> openAddressList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(openAddressList);
        addressListView.setOnItemClickListener(new onItemCliclListener());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void updateUI() {
        if (!isAdmin()){
            backButton.setVisibility(View.INVISIBLE);
            addPakaButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isAdmin() {
        if (permission != null)
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
            i.putExtra("pakaKey", pakaKey);
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

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
    }


    private class onItemCliclListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int pos = adapterView.getSelectedItemPosition() + 1;
            Toast.makeText(OpenPakaPage.this, "" + pos, Toast.LENGTH_SHORT).show();
            pakaKey = pakaNumberList.get(pos);
            startActivityButton(2);
        }
    }
}
