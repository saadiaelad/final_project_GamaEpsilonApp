package com.example.elad.gamaepsilonapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddPakaPage extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener{

    private EditText pakaNum;
    private EditText address;
    private Spinner className;
    private CheckBox openOrClose;
    private CheckBox tatOrNot;
    private Spinner teamLeader;
    private Spinner supervisor;
    private EditText periorty;
    private EditText openDate;
    private EditText startDate;
    private EditText closeDate;
    private EditText profitSum;
    private EditText pakaCommit;
    private Button acceptButton;
    private Spinner works;
    private Spinner workers;
    private TextView worksListText;
    private TextView workersListText;
    private ListView worksListView;
    private ListView workersListView;
    private View focusView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("pakaTable");
    private Query q;
    private User thisUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paka_page);

        pakaNum = (EditText)findViewById(R.id.pakaNumber);
        address = (EditText)findViewById(R.id.addressText);
        className = (Spinner) findViewById(R.id.classText);
        openOrClose = (CheckBox)findViewById(R.id.openOrClose);
        tatOrNot = (CheckBox)findViewById(R.id.tatOrNot);
        teamLeader = (Spinner)findViewById(R.id.teamLeader);
        supervisor = (Spinner)findViewById(R.id.supervisor);
        periorty = (EditText)findViewById(R.id.periorty);
        openDate = (EditText)findViewById(R.id.openDate);
        startDate = (EditText)findViewById(R.id.makingDate);
        closeDate = (EditText)findViewById(R.id.closingDate);
        profitSum = (EditText)findViewById(R.id.profitSum);
        pakaCommit = (EditText)findViewById(R.id.pakaCommit);
        acceptButton = (Button)findViewById(R.id.addUserButton);
        works = (Spinner)findViewById(R.id.works);
        workers = (Spinner)findViewById(R.id.workers);
        worksListText = (TextView)findViewById(R.id.workListText);
        workersListText = (TextView)findViewById(R.id.workersListText);
        worksListView = (ListView)findViewById(R.id.worksListView);
        workersListView = (ListView)findViewById(R.id.workersListView);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.className,
                android.R.layout.simple_spinner_item);
        className.setAdapter(adapter);
        className.setOnItemSelectedListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        acceptButton.setOnClickListener(new onClickListener());
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == acceptButton.getId())
            {
                addToDatabase();
                finish();
            }
        }
    }

    private void addToDatabase() {

    }
}
