package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRemoveWork extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    String countChange = "";
    private EditText fullNameText;
    private EditText smallNameText;
    private EditText arabicNameText;
    private EditText workNumberText;
    private EditText priceText;
    private Spinner unitsSpinner;
    private Button acceptButton;
    private Button backButton;
    private ListView worksListView;
    private View focusView;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("worksTable");
    private FirebaseListAdapter<Supervisor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_work);

        fullNameText = (EditText)findViewById(R.id.fullNameText);
        smallNameText = (EditText)findViewById(R.id.smallNameText);
        arabicNameText = (EditText)findViewById(R.id.arabicNameText);
        workNumberText = (EditText)findViewById(R.id.workNumText);
        priceText = (EditText)findViewById(R.id.priceText);
        unitsSpinner = (Spinner)findViewById(R.id.unitsSpinner);
        acceptButton = (Button)findViewById(R.id.acceptButton);
        backButton = (Button)findViewById(R.id.backToManagmentPage);
        worksListView = (ListView)findViewById(R.id.worksListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.unitsOption,
                android.R.layout.simple_spinner_item);
        unitsSpinner.setAdapter(spinnerAdapter);
        unitsSpinner.setOnItemSelectedListener(this);

        acceptButton.setOnClickListener(new OnClickListener());
        backButton.setOnClickListener(new OnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRef.addValueEventListener(this);
        focusView = fullNameText;
        focusView.requestFocus();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == acceptButton.getId()){
                addToDb();
            }
            else if (v.getId() == backButton.getId()){
                startActivityButton();
            }
        }
    }

    private void addToDb() {
        String fN = fullNameText.getText().toString();
        String sN = smallNameText.getText().toString();
        String aN = arabicNameText.getText().toString();
        String wN = workNumberText.getText().toString();
        String p = priceText.getText().toString();
        String u = unitsSpinner.getSelectedItem().toString();

        dataRef.child(countChange).child("fullName").setValue(fN);
        dataRef.child(countChange).child("abbriviatedName").setValue(sN);
        dataRef.child(countChange).child("arbicName").setValue(aN);
        dataRef.child(countChange).child("workNum").setValue(wN);
        dataRef.child(countChange).child("cost").setValue(p);
        dataRef.child(countChange).child("units").setValue(u);

        Toast.makeText(this, "העבודה נשמרה בהצלחה", Toast.LENGTH_SHORT).show();

        makeAnotherOne();
    }

    private void makeAnotherOne() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("הוספת עבודה נוספת")
                .setMessage("האם ברצונך להכניס עבודה נוספת?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clean();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    startActivityButton();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clean() {
        fullNameText.setText("");
        smallNameText.setText("");
        arabicNameText.setText("");
        priceText.setText("");
        workNumberText.setText("");
        focusView = fullNameText;
        focusView.requestFocus();
    }

    private void startActivityButton() {
        finish();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        displayList();
        progressDialog.dismiss();
        countChange ="" + dataSnapshot.getChildrenCount();
    }

    private void displayList() {
        adapter = new FirebaseListAdapter<Supervisor>(this, Supervisor.class,
                R.layout.supervisor_list_view, dataRef){

            @Override
            protected void populateView(View view, Supervisor supervisor, int i) {

            }
        };
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
