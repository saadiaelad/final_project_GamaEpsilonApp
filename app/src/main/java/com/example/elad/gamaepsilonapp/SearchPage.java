package com.example.elad.gamaepsilonapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchPage extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private ArrayList<String> arrayWorkers = new ArrayList<>();
    private ArrayList<String> arraySupervisor = new ArrayList<>();
    private int searchBy = 0;
    private EditText pakaNumberSearchText;
    private EditText startingDateSearchText;
    private EditText closingDateSearchText;
    private Spinner searchOptionsSpinner;
    private Spinner supervisorSearchSpinner;
    private Spinner workersSearchSpinner;
    private Spinner monthSearchSpinner;
    private Button searchButton;
    private Button backButton;
    private DateFormat formatDate = DateFormat.getDateInstance();
    private Calendar startDatePicker = Calendar.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefSupervisor = database.getReference("supervisorTable");
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        pakaNumberSearchText = (EditText)findViewById(R.id.pakaNumSearchText);
        startingDateSearchText = (EditText)findViewById(R.id.startingDateSearchText);
        closingDateSearchText = (EditText)findViewById(R.id.closingDateSearchText);
        searchOptionsSpinner = (Spinner)findViewById(R.id.searchOptionsSpinner);
        supervisorSearchSpinner = (Spinner)findViewById(R.id.supervisorSearchSpinner);
        workersSearchSpinner = (Spinner)findViewById(R.id.workerSearchSpinner);
        monthSearchSpinner = (Spinner)findViewById(R.id.monthSearchSpinner);
        searchButton = (Button)findViewById(R.id.acceptSearchButton);
        backButton = (Button)findViewById(R.id.backButton);
        pakaNumberSearchText.setVisibility(View.INVISIBLE);
        startingDateSearchText.setVisibility(View.INVISIBLE);
        closingDateSearchText.setVisibility(View.INVISIBLE);
        supervisorSearchSpinner.setVisibility(View.INVISIBLE);
        monthSearchSpinner.setVisibility(View.INVISIBLE);
        workersSearchSpinner.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        ArrayAdapter adapterSearchOption = ArrayAdapter.createFromResource(this, R.array.searchOption,
                android.R.layout.simple_spinner_item);
        searchOptionsSpinner.setAdapter(adapterSearchOption);
        searchOptionsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterMonthOption = ArrayAdapter.createFromResource(this, R.array.month,
                android.R.layout.simple_spinner_item);
        monthSearchSpinner.setAdapter(adapterMonthOption);
        monthSearchSpinner.setOnItemSelectedListener(this);
        arraySupervisor.add("בחר מפקח");
        ArrayAdapter<String> adapterSupervisor = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySupervisor);
        supervisorSearchSpinner.setAdapter(adapterSupervisor);
        supervisorSearchSpinner.setOnItemSelectedListener(this);
        arrayWorkers.add("בחר עובדים");
        ArrayAdapter<String> adapterWorkers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayWorkers);
        workersSearchSpinner.setAdapter(adapterWorkers);
        workersSearchSpinner.setOnItemSelectedListener(this);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefPaka.addValueEventListener(this);
        dataRefUser.addValueEventListener(this);
        dataRefSupervisor.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        switch (dataSnapshot.getKey()){
            case ("userTable"):{
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    String s = (String) d.child("firstName").getValue();
                    if (s != null)
                        arrayWorkers.add(s);
                }
            }
            case ("supervisorTable"):{
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    String s = (String)d.child("supervisorName").getValue();
                    if (s != null)
                        arraySupervisor.add(s);
                }
            }
            case ("pakaTable"):{

            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchBy = parent.getSelectedItemPosition();
        if (parent.getSelectedItemPosition() != 0){
            if (parent.getSelectedItem() == searchOptionsSpinner.getSelectedItem()){
                if (parent.getSelectedItemPosition() == 1){
                    pakaNumberSearchText.setVisibility(view.VISIBLE);
                    searchButton.setVisibility(view.VISIBLE);
                    startingDateSearchText.setVisibility(View.INVISIBLE);
                    closingDateSearchText.setVisibility(View.INVISIBLE);
                    supervisorSearchSpinner.setVisibility(View.INVISIBLE);
                    monthSearchSpinner.setVisibility(View.INVISIBLE);
                    workersSearchSpinner.setVisibility(View.INVISIBLE);
                }
                else if (parent.getSelectedItemPosition() == 2){
                    pakaNumberSearchText.setVisibility(view.INVISIBLE);
                    searchButton.setVisibility(view.VISIBLE);
                    startingDateSearchText.setVisibility(View.VISIBLE);
                    closingDateSearchText.setVisibility(View.VISIBLE);
                    supervisorSearchSpinner.setVisibility(View.VISIBLE);
                    monthSearchSpinner.setVisibility(View.INVISIBLE);
                    workersSearchSpinner.setVisibility(View.INVISIBLE);
                    openDateDialog(new View(getApplicationContext()));
                }
                else if (parent.getSelectedItemPosition() == 3){
                    pakaNumberSearchText.setVisibility(view.INVISIBLE);
                    searchButton.setVisibility(view.VISIBLE);
                    startingDateSearchText.setVisibility(View.VISIBLE);
                    closingDateSearchText.setVisibility(View.VISIBLE);
                    supervisorSearchSpinner.setVisibility(View.INVISIBLE);
                    monthSearchSpinner.setVisibility(View.INVISIBLE);
                    workersSearchSpinner.setVisibility(View.INVISIBLE);
                    openDateDialog(new View(getApplicationContext()));
                }
                else if (parent.getSelectedItemPosition() == 4){
                    pakaNumberSearchText.setVisibility(view.INVISIBLE);
                    searchButton.setVisibility(view.VISIBLE);
                    startingDateSearchText.setVisibility(View.INVISIBLE);
                    closingDateSearchText.setVisibility(View.INVISIBLE);
                    supervisorSearchSpinner.setVisibility(View.INVISIBLE);
                    monthSearchSpinner.setVisibility(View.VISIBLE);
                    workersSearchSpinner.setVisibility(View.VISIBLE);
                }
            }
        }
        else{
            pakaNumberSearchText.setVisibility(view.INVISIBLE);
            searchButton.setVisibility(view.INVISIBLE);
            startingDateSearchText.setVisibility(View.INVISIBLE);
            closingDateSearchText.setVisibility(View.INVISIBLE);
            supervisorSearchSpinner.setVisibility(View.INVISIBLE);
            monthSearchSpinner.setVisibility(View.INVISIBLE);
            workersSearchSpinner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == backButton.getId()){
                startActivityButton(1);
            }
            else if (v.getId() == searchButton.getId()){
                searchByKey(searchBy);
            }

        }
    }

    private void startActivityButton(int butt) {
        if (butt == 1){
            Intent i = new Intent(this, MainPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 2){
            Intent i = new Intent(this, PakaPage.class);
            finish();
            startActivity(i);
        }
    }

    private void searchByKey(int sB) {
        switch (sB){
            case 0:{
                Toast.makeText(this, "אנא בחר סוג חיפוש", Toast.LENGTH_SHORT).show();
            }
            case 1:{

            }
            case 2:{

            }
            case 3:{

            }
            case 4:{

            }
        }
    }

    public void openDateDialog(View v){
        if (v.getId() == startingDateSearchText.getId()){
            updateDate(1);
        }
        else if (v.getId() == closingDateSearchText.getId()){
            updateDate(2);
        }
    }

    private void updateDate(int i) {
        if (i == 1) {
            new DatePickerDialog(this, R.style.openDateDialog, startDateDialog, startDatePicker.get(Calendar.YEAR),
                    startDatePicker.get(Calendar.MONTH), startDatePicker.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (i == 2) {
            new DatePickerDialog(this, R.style.openDateDialog, closeDateDialog, startDatePicker.get(Calendar.YEAR),
                    startDatePicker.get(Calendar.MONTH), startDatePicker.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    DatePickerDialog.OnDateSetListener startDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDatePicker.set(Calendar.YEAR, year);
            startDatePicker.set(Calendar.MONTH, month);
            startDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startingDateSearchText.setText(formatDate.format(startDatePicker.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener closeDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDatePicker.set(Calendar.YEAR, year);
            startDatePicker.set(Calendar.MONTH, month);
            startDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            closingDateSearchText.setText(formatDate.format(startDatePicker.getTime()));
        }
    };
}
