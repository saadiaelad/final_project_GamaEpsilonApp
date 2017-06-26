package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PakaDetails extends AppCompatActivity implements ValueEventListener {

    private String pakaKey;
    private String permission;
    private ArrayList<String > teamLeaderList = new ArrayList<>();
    private ArrayList<String > workersList = new ArrayList<>();
    private ArrayList<String > worksList = new ArrayList<>();
    private TextView pakaNumberText;
    private TextView addressText;
    private TextView classText;
    private TextView openOrCloseText;
    private TextView tatOrNotText;
    private TextView supervisorText;
    private TextView periortyText;
    private TextView openDateText;
    private TextView startDateText;
    private TextView closeDateText;
    private TextView profitText;
    private TextView pakaCommitText;
    private ListView teamLeaderListViewText;
    private ListView workersListViewText;
    private ListView worksListViewText;
    private Button backButton;
    private FirebaseListAdapter<String> workerAdapter;
    private FirebaseListAdapter<String> workAdapter;
    private FirebaseListAdapter<String> teamLeaderAdapter;
    private ProgressDialog progressDialog;
    private FirebaseListAdapter<User> userAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private DatabaseReference dataRefUser = database.getReference("userTable");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paka_details);

        pakaNumberText = (TextView)findViewById(R.id.pakaNumberText);
        addressText = (TextView)findViewById(R.id.addressText);
        classText = (TextView)findViewById(R.id.classText);
        openOrCloseText = (TextView)findViewById(R.id.openOrCloseText);
        tatOrNotText = (TextView)findViewById(R.id.tatOrNotText);
        supervisorText = (TextView)findViewById(R.id.supervisorNameText);
        periortyText = (TextView)findViewById(R.id.periortyText);
        openDateText = (TextView)findViewById(R.id.openDateText);
        startDateText = (TextView)findViewById(R.id.makingDateText);
        closeDateText = (TextView)findViewById(R.id.closingDateText);
        profitText = (TextView)findViewById(R.id.profitSumText);
        pakaCommitText = (TextView)findViewById(R.id.pakaCommitText);
        teamLeaderListViewText = (ListView)findViewById(R.id.teamLeaderText);
        workersListViewText = (ListView)findViewById(R.id.workersListViewText);
        worksListViewText = (ListView)findViewById(R.id.worksListViewText);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("pakaKey") != null)
            pakaKey = bundle.getString("pakaKey");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final ArrayAdapter<String> teamLeaderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, teamLeaderList);
        teamLeaderListViewText.setAdapter(teamLeaderAdapter);
        final ArrayAdapter<String> workersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, workersList);
        workersListViewText.setAdapter(workersAdapter);
        final ArrayAdapter<String> worksAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, worksList);
        worksListViewText.setAdapter(worksAdapter);

        backButton.setOnClickListener(new onClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefPaka.addValueEventListener(this);
        dataRefUser.addValueEventListener(this);
        updateUi();
        progressDialog.dismiss();
    }

    private void updateUi() {
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

    private class onClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == backButton.getId()){
                finish();
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey()) {
            case "pkaTable":
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getKey().equals(pakaKey)) {
                        pakaNumberText.setText((String) d.child("pakaNum").getValue());
                        addressText.setText((String) d.child("address").getValue());
                        classText.setText((String) d.child("className").getValue());
                        openOrCloseText.setText((String) d.child("openOrClose").getValue());
                        tatOrNotText.setText((String) d.child("tat").getValue());
                        openDateText.setText((String) d.child("openDate").getValue());
                        startDateText.setText((String) d.child("startingDate").getValue());
                        closeDateText.setText((String) d.child("closingDate").getValue());
                        supervisorText.setText((String) d.child("supervisor").getValue());
                        periortyText.setText((String) d.child("periorty").getValue());
                        profitText.setText((String) d.child("price").getValue());
                        pakaCommitText.setText((String) d.child("commit").getValue());
                        for (DataSnapshot ds : d.child("teamLeader").getChildren())
                            teamLeaderList.add((String) ds.getValue());
                        for (DataSnapshot ds : d.child("workers").getChildren())
                            workersList.add((String) ds.getValue());
                        for (DataSnapshot ds : d.child("workPerfomed").getChildren())
                            worksList.add((String) ds.getValue());
                    }
                }
            case "userTable":
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (((String)d.child("userMail").getValue()).equals(currentUser.getEmail()))
                        permission = (String)d.child("permission").getValue();
                }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}