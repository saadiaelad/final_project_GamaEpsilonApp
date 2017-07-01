package com.example.elad.gamaepsilonapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchPage extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private ArrayList<String> arrayWorkers = new ArrayList<>();
    private ArrayList<String> arraySupervisor = new ArrayList<>();
    private ArrayList<String> searchBy1ArrayPaka = new ArrayList<>();
    private ArrayList<String> searchBy1ArrayAddress = new ArrayList<>();
    private ArrayList<String> searchBy2ArrayPaka = new ArrayList<>();
    private ArrayList<String> searchBy2ArrayAddress = new ArrayList<>();
    private ArrayList<String> searchBy3ArrayPaka = new ArrayList<>();
    private ArrayList<String> searchBy3ArrayAddress = new ArrayList<>();
    private ArrayList<String> searchBy4ArrayPaka = new ArrayList<>();
    private ArrayList<String> searchBy4ArrayAddress = new ArrayList<>();
    private ArrayList<Paka> pakasArray = new ArrayList<>();
    private int searchBy = 0;
    private TextView pakaNumberTextView;
    private TextView addressTextView;
    private EditText pakaNumberSearchText;
    private EditText startingDateSearchText;
    private EditText closingDateSearchText;
    private Spinner searchOptionsSpinner;
    private Spinner supervisorSearchSpinner;
    private Spinner workersSearchSpinner;
    private Spinner monthSearchSpinner;
    private Button searchButton;
    private Button backButton;
    private ListView pakaNumberListView;
    private ListView addressListView;
    private DateFormat formatDate = DateFormat.getDateInstance();
    private Calendar startDatePicker = Calendar.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefSupervisor = database.getReference("supervisorTable");
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private String pakaKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        pakaNumberTextView = (TextView)findViewById(R.id.pakaNumberText);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        pakaNumberSearchText = (EditText)findViewById(R.id.pakaNumSearchText);
        startingDateSearchText = (EditText)findViewById(R.id.startingDateSearchText);
        closingDateSearchText = (EditText)findViewById(R.id.closingDateSearchText);
        searchOptionsSpinner = (Spinner)findViewById(R.id.searchOptionsSpinner);
        supervisorSearchSpinner = (Spinner)findViewById(R.id.supervisorSearchSpinner);
        workersSearchSpinner = (Spinner)findViewById(R.id.workerSearchSpinner);
        monthSearchSpinner = (Spinner)findViewById(R.id.monthSearchSpinner);
        searchButton = (Button)findViewById(R.id.acceptSearchButton);
        backButton = (Button)findViewById(R.id.backButton);
        pakaNumberListView = (ListView)findViewById(R.id.pakaNumberListView);
        addressListView = (ListView)findViewById(R.id.addressSearchListView);
        pakaNumberTextView.setVisibility(View.INVISIBLE);
        addressTextView.setVisibility(View.INVISIBLE);
        pakaNumberSearchText.setVisibility(View.INVISIBLE);
        startingDateSearchText.setVisibility(View.INVISIBLE);
        closingDateSearchText.setVisibility(View.INVISIBLE);
        supervisorSearchSpinner.setVisibility(View.INVISIBLE);
        monthSearchSpinner.setVisibility(View.INVISIBLE);
        workersSearchSpinner.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        pakaNumberListView.setVisibility(View.INVISIBLE);
        addressListView.setVisibility(View.INVISIBLE);
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
        arrayWorkers.add("בחר עובדים");
        ArrayAdapter<String> adapterWorkers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayWorkers);
        workersSearchSpinner.setAdapter(adapterWorkers);

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
        if (dataSnapshot.getKey().equals("userTable")){
            for (DataSnapshot d:dataSnapshot.getChildren()) {
                String s = (String) d.child("firstName").getValue();
                if (s != null)
                    arrayWorkers.add(s);
            }
        }
        else if (dataSnapshot.getKey().equals("supervisorTable")){
            for (DataSnapshot d:dataSnapshot.getChildren()){
                String s = (String)d.child("supervisorName").getValue();
                if (s != null)
                    arraySupervisor.add(s);
            }
        }
        else if (dataSnapshot.getKey().equals("pakaTable")){
            for (DataSnapshot d:dataSnapshot.getChildren()){
                if (d.getValue() != null)
                    pakasArray.add(d.getValue(Paka.class));
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchBy = parent.getSelectedItemPosition();
        if (searchBy != 0){
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
            String whereFrom = "SearchPage";
            Intent i = new Intent(this, PakaPage.class);
            i.putExtra("pakaKey", pakaKey);
            i.putExtra("whereFrom", whereFrom);
            i.putExtra("language", true);
            finish();
            startActivity(i);
        }
    }

    private void searchByKey(int sB) {
        if (sB == 0)
            Toast.makeText(this, "אנא בחר סוג חיפוש", Toast.LENGTH_SHORT).show();
        else if (sB == 1){
            searchBy1ArrayPaka.clear();
            searchBy1ArrayAddress.clear();
            for (int i = 0 ; i < pakasArray.size() ; i++){
                if (pakasArray.get(i).getPakaNum().toString().equals(pakaNumberSearchText.getText().toString())){
                    searchBy1ArrayPaka.add(pakasArray.get(i).getPakaNum());
                    searchBy1ArrayAddress.add(pakasArray.get(i).getAddress());
                }
            }
            final ArrayAdapter<String> pakaNumberListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy1ArrayPaka);
            pakaNumberListView.setAdapter(pakaNumberListAdapter);
            pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
            final ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy1ArrayAddress);
            addressListView.setAdapter(addressListAdapter);
            addressListView.setOnItemClickListener(new onItemCliclListener());
        }
        else if (sB == 2){
            searchBy2ArrayPaka.clear();
            searchBy2ArrayAddress.clear();
            String supervisor = supervisorSearchSpinner.getSelectedItem().toString();
            for ( int i =0 ; i < pakasArray.size() ; i++){
                if (pakasArray.get(i).getSupervisor().toString().equals(supervisor)) {
                    SimpleDateFormat df = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
                    String sD = startingDateSearchText.getText().toString();
                    String cD = closingDateSearchText.getText().toString();
                    String cDP;
                    Date d = null;
                    Date d1 = null;
                    Date d2 = null;
                    if (pakasArray.get(i).getClosingDate().equals(""))
                        cDP = pakasArray.get(i).getOpenDate().toString();
                    else
                        cDP = pakasArray.get(i).getClosingDate().toString();
                    try {
                        d1 = df.parse(sD);
                        d2 = df.parse(cD);
                        d = df.parse(cDP);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int cDPCheck;
                    try {
                        cDPCheck = d1.compareTo(d) * d.compareTo(d2);
                    }
                    catch (NullPointerException e){
                        cDPCheck = 0;
                    }
                    String pN = pakasArray.get(i).getPakaNum();
                    String pA = pakasArray.get(i).getAddress();
                    if (cDPCheck >= 0) {
                        searchBy2ArrayPaka.add(pN);
                        searchBy2ArrayAddress.add(pA);
                    }
                }
            }

            final ArrayAdapter<String> pakaNumberListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy2ArrayPaka);
            pakaNumberListView.setAdapter(pakaNumberListAdapter);
            pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
            final ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy2ArrayAddress);
            addressListView.setAdapter(addressListAdapter);
            addressListView.setOnItemClickListener(new onItemCliclListener());
        }
        else if (sB == 3){
            searchBy3ArrayPaka.clear();
            searchBy3ArrayAddress.clear();

            for (int i = 0 ; i < pakasArray.size() ; i++){
                if (pakasArray.get(i).isTat()){
                    SimpleDateFormat df = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
                    String sD = startingDateSearchText.getText().toString();
                    String cD = closingDateSearchText.getText().toString();
                    String cDP;
                    Date d = null;
                    Date d1 = null;
                    Date d2 = null;
                    if (pakasArray.get(i).getClosingDate().equals(""))
                        cDP = pakasArray.get(i).getOpenDate().toString();
                    else
                        cDP = pakasArray.get(i).getClosingDate().toString();
                    try {
                        d1 = df.parse(sD);
                        d2 = df.parse(cD);
                        d = df.parse(cDP);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int cDPCheck;
                    try {
                        cDPCheck = d1.compareTo(d) * d.compareTo(d2);
                    }
                    catch (NullPointerException e){
                        cDPCheck = 0;
                    }
                    String pN = pakasArray.get(i).getPakaNum();
                    String pA = pakasArray.get(i).getAddress();
                    if (cDPCheck >= 0) {
                        searchBy3ArrayPaka.add(pN);
                        searchBy3ArrayAddress.add(pA);
                    }
                }
            }
            final ArrayAdapter<String> pakaNumberListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy3ArrayPaka);
            pakaNumberListView.setAdapter(pakaNumberListAdapter);
            pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
            final ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy3ArrayAddress);
            addressListView.setAdapter(addressListAdapter);
            addressListView.setOnItemClickListener(new onItemCliclListener());
        }
        else if (sB >= 4){
            searchBy4ArrayPaka.clear();
            searchBy4ArrayAddress.clear();
            String worker = workersSearchSpinner.getSelectedItem().toString();
            for (int i = 0 ; i < pakasArray.size() ; i++){
                ArrayList<String> workers = new ArrayList<>();
                if (pakasArray.get(i).getWorkers() != null)
                    for (int j =0 ; j < pakasArray.get(i).getWorkers().size() ; j++)
                        workers.add(pakasArray.get(i).getWorkers().get(j));
                for (int j = 0 ; j < workers.size() ; j++) {
                    if (pakasArray.get(i).isOpenOrClose()) {
                        if (workers.get(i).equals(worker)) {
                            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                            int m = monthSearchSpinner.getSelectedItemPosition();
                            SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                            String sD;
                            String cD;
                            String cSD;
                            Date d = null;
                            Date d1 = null;
                            Date d2 = null;
                        }
                    }
                }
            }
            final ArrayAdapter<String> pakaNumberListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy4ArrayPaka);
            pakaNumberListView.setAdapter(pakaNumberListAdapter);
            pakaNumberListView.setOnItemClickListener(new onItemCliclListener());
            final ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchBy4ArrayAddress);
            addressListView.setAdapter(addressListAdapter);
            addressListView.setOnItemClickListener(new onItemCliclListener());
        }
        pakaNumberTextView.setVisibility(View.VISIBLE);
        addressTextView.setVisibility(View.VISIBLE);
        pakaNumberListView.setVisibility(View.VISIBLE);
        addressListView.setVisibility(View.VISIBLE);
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

    private class onItemCliclListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (searchBy == 1)
                pakaKey = searchBy1ArrayPaka.get((int)l);
            else if (searchBy == 2)
                pakaKey = searchBy2ArrayPaka.get((int)l);
            else if (searchBy == 3)
                pakaKey = searchBy3ArrayPaka.get((int)l);
            else if (searchBy == 4)
                pakaKey = searchBy4ArrayPaka.get((int)l);
            startActivityButton(2);
        }
    }
}
