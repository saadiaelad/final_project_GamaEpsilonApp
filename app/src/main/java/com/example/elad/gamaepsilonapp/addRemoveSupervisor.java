package com.example.elad.gamaepsilonapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addRemoveSupervisor extends AppCompatActivity {

    private EditText supervisorName;
    private EditText supervisorWorkPlace;
    private Button addSupervisor;
    private ListView supervisorListView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("supervisorTable");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_supervisor);

        supervisorName = (EditText)findViewById(R.id.supervisorName);
        supervisorWorkPlace = (EditText)findViewById(R.id.workPlace);
        addSupervisor = (Button)findViewById(R.id.addSupervisorButton);
        supervisorListView = (ListView)findViewById(R.id.supervisorListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
