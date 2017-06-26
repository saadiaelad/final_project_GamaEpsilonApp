package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class AddPakaPage extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener{

    String countChange = "";
    private ArrayList<String> arrayWorkers = new ArrayList<>();
    private ArrayList<String> arrayWorks = new ArrayList<>();
    private ArrayList<String> arraySupervisor = new ArrayList<>();
    private ArrayList<String> arrayTeamLeader = new ArrayList<>();
    private ArrayList<String> choosenWorkers = new ArrayList<>();
    private ArrayList<String> choosenWorks = new ArrayList<>();
    private ArrayList<String> teamLeaderCoosen = new ArrayList<>();
    private ArrayList<Works> cW = new ArrayList<>();
    private EditText pakaNum;
    private EditText addressText;
    private Spinner classNameText;
    private CheckBox openOrCloseCheckBox;
    private CheckBox tatOrNotCheckBox;
    private Spinner teamLeaderSpinner;
    private Spinner supervisorSpinner;
    private EditText periortyText;
    private EditText openDateText;
    private EditText startDateText;
    private EditText closeDateText;
    private EditText profitSumText;
    private EditText pakaCommitText;
    private Button acceptButton;
    private Button backToOpenPakaPage;
    private Spinner works;
    private Spinner workers;
    private TextView worksListText;
    private TextView workersListText;
    private ListView worksListView;
    private ListView workersListView;
    private DateFormat formatDate = DateFormat.getDateInstance();
    private Calendar startDatePicker = Calendar.getInstance();
    private View focusView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefSupervisor = database.getReference("supervisorTable");
    private DatabaseReference dataRefWork = database.getReference("work_table");
    private User thisUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paka_page);

        pakaNum = (EditText)findViewById(R.id.pakaNumber);
        addressText = (EditText)findViewById(R.id.addressText);
        classNameText = (Spinner) findViewById(R.id.classText);
        openOrCloseCheckBox = (CheckBox)findViewById(R.id.openOrClose);
        tatOrNotCheckBox = (CheckBox)findViewById(R.id.tatOrNot);
        teamLeaderSpinner = (Spinner)findViewById(R.id.teamLeader);
        supervisorSpinner = (Spinner)findViewById(R.id.supervisor);
        periortyText = (EditText)findViewById(R.id.periorty);
        openDateText = (EditText)findViewById(R.id.openDate);
        startDateText = (EditText)findViewById(R.id.makingDate);
        closeDateText = (EditText)findViewById(R.id.closingDate);
        profitSumText = (EditText)findViewById(R.id.profitSum);
        pakaCommitText = (EditText)findViewById(R.id.pakaCommit);
        acceptButton = (Button)findViewById(R.id.addPakaButton);
        backToOpenPakaPage = (Button)findViewById(R.id.backToOpenPakaPage);
        works = (Spinner)findViewById(R.id.works);
        workers = (Spinner)findViewById(R.id.workers);
        worksListText = (TextView)findViewById(R.id.workListText);
        workersListText = (TextView)findViewById(R.id.workersListText);
        worksListView = (ListView)findViewById(R.id.worksListView);
        workersListView = (ListView)findViewById(R.id.workersListView);
        ArrayAdapter adapterClass = ArrayAdapter.createFromResource(this, R.array.className,
                android.R.layout.simple_spinner_item);
        classNameText.setAdapter(adapterClass);
        classNameText.setOnItemSelectedListener(this);
        arraySupervisor.add("בחר מפקח");
        ArrayAdapter<String> adapterSupervisor = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySupervisor);
        supervisorSpinner.setAdapter(adapterSupervisor);
        supervisorSpinner.setOnItemSelectedListener(this);
        arrayTeamLeader.add("בחר מנהל צוות");
        ArrayAdapter<String> adapterTeamLeader = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayTeamLeader);
        teamLeaderSpinner.setAdapter(adapterTeamLeader);
        teamLeaderSpinner.setOnItemSelectedListener(this);
        arrayWorkers.add("בחר עובדים");
        ArrayAdapter<String> adapterWorkers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayWorkers);
        workers.setAdapter(adapterWorkers);
        workers.setOnItemSelectedListener(this);
        arrayWorks.add("הוסף עבודה");
        ArrayAdapter<String> adapterWorks = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayWorks);
        works.setAdapter(adapterWorks);
        works.setOnItemSelectedListener(this);
        final ArrayAdapter<String> workersChoosenList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, choosenWorkers);
        workersListView.setAdapter(workersChoosenList);
        workersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(view.getContext());
                }
                builder.setTitle("deleteWorker")
                        .setMessage("המערכת מכינה את מחיקת העובד מהרשימה, האם ברצונך להמשיך?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                workersChoosenList.remove((String)parent.getItemAtPosition(position));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        final ArrayAdapter<String> worksChoosenList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, choosenWorks);
        worksListView.setAdapter(worksChoosenList);
        worksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(view.getContext());
                }
                builder.setTitle("deleteWork")
                        .setMessage("המערכת מכינה את מחיקת העבודה מהרשימה, האם ברצונך להמשיך?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                worksChoosenList.remove((String)parent.getItemAtPosition(position));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        acceptButton.setOnClickListener(new onClickListener());
        backToOpenPakaPage.setOnClickListener(new onClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        focusView = pakaNum;
        pakaNum.requestFocus();
        dataRefPaka.addValueEventListener(this);
        dataRefUser.addValueEventListener(this);
        dataRefSupervisor.addValueEventListener(this);
        dataRefWork.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
            switch (dataSnapshot.getKey()){
                case ("pakaTable"):{
                    countChange ="" + dataSnapshot.getChildrenCount();
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        if (d.child("teamLeader").getValue() != null)
                            for (DataSnapshot ds:d.child("teamLeader").getChildren())
                                if (ds.getValue() != null)
                                    if (d.child("pakaNum").getValue().toString().equals(pakaNum.getText().toString()))
                                        teamLeaderCoosen.add(ds.getValue().toString());
                    }
                }
                case ("userTable"):{
                    for (DataSnapshot d:dataSnapshot.getChildren()) {
                        String s = (String) d.child("firstName").getValue();
                        if (s != null)
                            arrayWorkers.add(s);
                        if (d.child("permission").getValue() != null) {
                            if (((String) d.child("permission").getValue()).equals("מנהל ראשי") ||
                                    ((String) d.child("permission").getValue()).equals("מנהל צוות")) {
                                if (s != null)
                                    arrayTeamLeader.add(s);
                            }
                        }
                    }
                }
                case ("supervisorTable"):{
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        String s = (String)d.child("supervisorName").getValue();
                        if (s != null)
                            arraySupervisor.add(s);
                    }
                }
                case ("work_table"): {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Works work = new Works((String) d.child("abbriviatedName").getValue(),
                                (String) d.child("arabicName").getValue(),
                                (String) d.child("cost").getValue(),
                                (String) d.child("fullName").getValue(),
                                (String) d.child("units").getValue(),
                                (String) d.child("workNum").getValue());
                        String s = (String) d.child("abbreviated name").getValue();
                        if (s != null) {
                            arrayWorks.add(s);
                        }
                        if (work != null) {
                            cW.add(work);
                        }
                    }
                    Collections.sort(arrayWorks, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareToIgnoreCase(s2);
                        }
                    });
                }
            }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedItemPosition() != 0){
            if (parent.getSelectedItem() == workers.getSelectedItem()){
                String s = (String)workers.getSelectedItem();
                if (s != null)
                    for (int i = 0 ; i<choosenWorkers.size() ; i++)
                        if (choosenWorkers.get(i).equals(s)){
                            workers.setSelection(0);
                            return;
                        }
                    choosenWorkers.add(s);
                workers.setSelection(0);
            }
            else if (parent.getSelectedItem() == works.getSelectedItem()) {
                if (parent.getSelectedItem() == works.getSelectedItem()) {
                    String s = (String) works.getSelectedItem();
                    if (s != null)
                        for (int i = 0 ; i<choosenWorks.size() ; i++)
                            if (choosenWorks.get(i).equals(s)) {
                                works.setSelection(0);
                                return;
                            }
                        choosenWorks.add(s);
                    works.setSelection(0);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == acceptButton.getId()) {
                addToDatabase();
                startActivityButton();

            }
            else if (v.getId() == backToOpenPakaPage.getId()){
                startActivityButton();
            }
        }
    }

    private void startActivityButton() {
        finish();
    }

    public void openDateDialog(View v){
        if (v.getId() == openDateText.getId()){
            updateDate(1);
        }
        else if (v.getId() == startDateText.getId()){
            updateDate(2);
        }
        else if (v.getId() == closeDateText.getId()){
            updateDate(3);
        }
    }

    private void updateDate(int i) {
        if (i == 1) {
            new DatePickerDialog(this, R.style.openDateDialog, openDateDialog, startDatePicker.get(Calendar.YEAR),
                    startDatePicker.get(Calendar.MONTH), startDatePicker.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (i == 2) {
            new DatePickerDialog(this, R.style.openDateDialog, makingDateDialog, startDatePicker.get(Calendar.YEAR),
                    startDatePicker.get(Calendar.MONTH), startDatePicker.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (i == 3) {
            new DatePickerDialog(this, R.style.openDateDialog, closeDateDialog, startDatePicker.get(Calendar.YEAR),
                    startDatePicker.get(Calendar.MONTH), startDatePicker.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    DatePickerDialog.OnDateSetListener openDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDatePicker.set(Calendar.YEAR, year);
            startDatePicker.set(Calendar.MONTH, month);
            startDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            openDateText.setText(formatDate.format(startDatePicker.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener makingDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDatePicker.set(Calendar.YEAR, year);
            startDatePicker.set(Calendar.MONTH, month);
            startDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startDateText.setText(formatDate.format(startDatePicker.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener closeDateDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDatePicker.set(Calendar.YEAR, year);
            startDatePicker.set(Calendar.MONTH, month);
            startDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            closeDateText.setText(formatDate.format(startDatePicker.getTime()));
        }
    };

    private void addToDatabase() {
        String pakaNumber = pakaNum.getText().toString();
        String address = addressText.getText().toString();
        boolean openOrClose = openOrCloseCheckBox.isChecked();
        String openDate = openDateText.getText().toString();
        String  startingDate = startDateText.getText().toString();
        String  closingDate = closeDateText.getText().toString();
        ArrayList<String> teamLeader = new ArrayList<>();
        String supervisor = supervisorSpinner.getSelectedItem().toString();
        String periorty = periortyText.getText().toString();
        String className = classNameText.getSelectedItem().toString();
        boolean tat = tatOrNotCheckBox.isChecked();
        ArrayList<String> workers =  new ArrayList<>();
        ArrayList<String> workPerfomed = new ArrayList<>();
        String price = profitSumText.getText().toString();
        String commit = pakaCommitText.getText().toString();
        for (int i=0 ; i<teamLeaderCoosen.size() ; i++)
            teamLeader.add(teamLeaderCoosen.get(i));
        teamLeader.add((String)teamLeaderSpinner.getSelectedItem());
        for (int i=0 ; i < choosenWorkers.size() ; i++)
            workers.add(choosenWorkers.get(i));
        for (int i=0 ; i < choosenWorks.size() ; i++)
            workPerfomed.add(choosenWorks.get(i));
        dataRefPaka.child(countChange).child("pakaNum").setValue(pakaNumber);
        dataRefPaka.child(countChange).child("address").setValue(address);
        dataRefPaka.child(countChange).child("openOrClose").setValue(openOrClose);
        dataRefPaka.child(countChange).child("openDate").setValue(openDate);
        dataRefPaka.child(countChange).child("startingDate").setValue(startingDate);
        dataRefPaka.child(countChange).child("closingDate").setValue(closingDate);
        for (int i=0 ; i < teamLeader.size() ; i++)
            dataRefPaka.child(countChange).child("teamLeader").child("" + i).setValue(teamLeader.get(i));
        dataRefPaka.child(countChange).child("supervisor").setValue(supervisor);
        dataRefPaka.child(countChange).child("periorty").setValue(periorty);
        dataRefPaka.child(countChange).child("className").setValue(className);
        dataRefPaka.child(countChange).child("tat").setValue(tat);
        for (int i=0 ; i < workers.size() ; i++)
            dataRefPaka.child(countChange).child("workers").child("" + i).setValue(workers.get(i));
        for (int i=0 ; i < workPerfomed.size() ; i++)
            dataRefPaka.child(countChange).child("workPerfomed").child("" + i).setValue(workPerfomed.get(i));
        dataRefPaka.child(countChange).child("price").setValue(price);
        dataRefPaka.child(countChange).child("commit").setValue(commit);
        teamLeaderCoosen.clear();
    }
}
