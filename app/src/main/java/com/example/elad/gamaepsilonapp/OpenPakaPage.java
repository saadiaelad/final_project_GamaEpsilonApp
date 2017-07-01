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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OpenPakaPage extends AppCompatActivity implements ValueEventListener{

    private boolean language = true;
    private ArrayList<String> pakaNumberList = new ArrayList<>();
    private ArrayList<String> addressList = new ArrayList<>();
    private String permission;
    private TextView nameText;
    private TextView pakaNumberTextView;
    private TextView addressTextView;
    private ImageButton addPakaButton;
    private ImageButton changeLanguage;
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
    private ArrayAdapter<String> openPakaNumberList;
    private ArrayAdapter<String> openAddressList;

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
        pakaNumberTextView = (TextView)findViewById(R.id.pakaNumberTextView);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        addPakaButton = (ImageButton)findViewById(R.id.addPakaButton);
        changeLanguage = (ImageButton)findViewById(R.id.changeLanguage);
        pakaNumberListView = (ListView)findViewById(R.id.pakaNumberListView);
        addressListView = (ListView)findViewById(R.id.addressListView);
        backButton = (Button)findViewById(R.id.backButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("language") != null)
            language = bundle.getBoolean("language");
        thisUser.setEmail(mAuth.getCurrentUser().getEmail());
        addPakaButton.setOnClickListener(cl);
        changeLanguage.setOnClickListener(cl);
        backButton.setOnClickListener(cl);
        signOutButton.setOnClickListener(cl);
        openPakaNumberList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, pakaNumberList);
        pakaNumberListView.setAdapter(openPakaNumberList);
        pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
        openAddressList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(openAddressList);
        addressListView.setOnItemClickListener(new onItemCliclListener());
        languageChange();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefUser.addValueEventListener(this);
        dataRefPaka.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("userTable")){
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if ((d.child("userMail").getValue()).equals(email)) {
                    permission = (String) d.child("permission").getValue();
                    username = (String)d.child("firstName").getValue();
                    if (language)
                        nameText.setText("שלום " + username);
                    else
                        nameText.setText("سلام " + username);
                }
            }
            updateUI();
            mProgressDialog.dismiss();
        }
        else if (dataSnapshot.getKey().equals("pakaTable")){
            if (permission.equals("מנהל ראשי")){
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if (d.child("openOrClose").getValue() != null){
                        if (!(boolean)d.child("openOrClose").getValue()) {
                            Paka paka = d.getValue(Paka.class);
                            addPakaToList(paka);
                        }
                    }
                }
            }
            else if (permission.equals("מנהל צוות")){
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if (d.child("openOrClose").getValue() != null){
                        if (!(boolean)d.child("openOrClose").getValue()){
                            if (d.child("teamLeader").getValue() != null) {
                                ArrayList<String> job = (ArrayList<String>) d.child("teamLeader").getValue();
                                if (job.get(job.size() - 1).equals(username)) {
                                    Paka p = d.getValue(Paka.class);
                                    addPakaToList(p);
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
        openPakaNumberList.notifyDataSetChanged();
        openAddressList.notifyDataSetChanged();
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
            else if (v.getId() == changeLanguage.getId()){
                if (language)
                    language = false;
                else
                    language = true;
                languageChange();
            }
        }
    }

    private void languageChange() {
        if (language)
            putHebrew();
        else
            putArabic();
    }

    private void putArabic() {
        nameText.setText("سلام " + username);
        backButton.setText("عودة");
        signOutButton.setText("الخروج");
        pakaNumberTextView.setText("رقم الباقه");
        addressTextView.setText("عنوان");
    }

    private void putHebrew() {
        nameText.setText("שלום " + username);
        backButton.setText("חזרה");
        signOutButton.setText("התנתקות");
        pakaNumberTextView.setText("מספר פקע");
        addressTextView.setText("כתובת");
    }


    private void startActivityButton(int butt) {
        if (butt == 1) {
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 2) {
            String whereFrom = "OpenPakaPage";
            Intent i = new Intent(this, PakaPage.class);
            i.putExtra("pakaKey", pakaKey);
            i.putExtra("whereFrom", whereFrom);
            i.putExtra("language", language);
            finish();
            startActivity(i);
        }
        else if (butt == 3) {
            Intent i = new Intent(this, AddPakaPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 4){
            Intent i = new Intent(this, MainPage.class);
            finish();
            startActivity(i);
        }
    }

    private class onItemCliclListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            pakaKey = pakaNumberList.get((int)l);
            startActivityButton(2);
        }
    }
}
