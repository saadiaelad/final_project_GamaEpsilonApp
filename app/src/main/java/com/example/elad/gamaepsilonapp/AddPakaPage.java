package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
    private int a = 0;
    double p = 0;
    double profit = 0;
    private boolean language = true;
    private ArrayList<String> arrayWorkers = new ArrayList<>();
    private ArrayList<String> arrayWorks = new ArrayList<>();
    private ArrayList<String> arraySupervisor = new ArrayList<>();
    private ArrayList<String> arrayTeamLeader = new ArrayList<>();
    private ArrayList<String> choosenWorkers = new ArrayList<>();
    private ArrayList<String> choosenWorksName = new ArrayList<>();
    private ArrayList<String> choosenWorksAmount = new ArrayList<>();
    private ArrayList<String> teamLeaderChoosen = new ArrayList<>();
    private ArrayList<WorkPerfomed> choosenWorks = new ArrayList<>();
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
    private ListView amountListView;
    private DateFormat formatDate = DateFormat.getDateInstance();
    private Calendar startDatePicker = Calendar.getInstance();
    private View focusView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefSupervisor = database.getReference("supervisorTable");
    private DatabaseReference dataRefWork = database.getReference("worksTable");
    private User thisUser = new User();
    ArrayAdapter<String> worksChoosenList;
    ArrayAdapter<String> amountAdapter;
    ArrayAdapter<String> workersChoosenList;


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
        amountListView = (ListView)findViewById(R.id.amountListView);
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
        workersChoosenList = new ArrayAdapter<String>(this,
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
                builder.setTitle("מחיקת משתמש")
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
        worksChoosenList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, choosenWorksName);
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
                builder.setTitle("מחיקת עבודה")
                        .setMessage("המערכת מכינה את מחיקת העבודה מהרשימה, האם ברצונך להמשיך?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                double p = 0;
                                for (int i = 0 ; i < cW.size() ; i++){
                                    if (worksChoosenList.getItem(position).equals(cW.get(i).getAbbriviatedName())) {
                                        p = Double.parseDouble(cW.get(i).getCost());
                                    }
                                }
                                int a = Integer.parseInt(choosenWorksAmount.get(position));
                                p = p*a;
                                profit = profit - p;
                                profitSumText.setText(String.valueOf(profit));
                                worksChoosenList.remove((String) parent.getItemAtPosition(position));
                                amountAdapter.remove((String) amountListView.getItemAtPosition(position));
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
        amountAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, choosenWorksAmount);
        amountListView.setAdapter(amountAdapter);
        amountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
        if (dataSnapshot.getKey().equals("pakaTable")){
            countChange ="" + dataSnapshot.getChildrenCount();
            for (DataSnapshot d:dataSnapshot.getChildren()){
                if (d.child("teamLeader").getValue() != null)
                    for (DataSnapshot ds:d.child("teamLeader").getChildren())
                        if (ds.getValue() != null)
                            if (d.child("pakaNum").getValue().toString().equals(pakaNum.getText().toString()))
                                teamLeaderChoosen.add(ds.getValue().toString());
            }
        }
        else if (dataSnapshot.getKey().equals("userTable")){
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
        else if (dataSnapshot.getKey().equals("supervisorTable")){
            for (DataSnapshot d:dataSnapshot.getChildren()){
                String s = (String)d.child("supervisorName").getValue();
                if (s != null)
                    arraySupervisor.add(s);
            }
        }
        else if (dataSnapshot.getKey().equals("worksTable")){
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                Works work = new Works((String)d.child("abbriviatedName").getValue(),
                        (String)d.child("arbicName").getValue(),
                        (String)d.child("cost").getValue(),
                        (String)d.child("fullName").getValue(),
                        (String)d.child("units").getValue(),
                        (String)d.child("workNum").getValue());
                String s = (String)d.child("abbriviatedName").getValue();
                if (s != null) {
                    arrayWorks.add(s);
                    cW.add(work);
                }
            }
            Collections.sort(arrayWorks, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);}
                });
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
                String workName = (String) works.getSelectedItem();
                if (workName != null) {
                    for (int i = 0; i < choosenWorks.size(); i++) {
                        if (choosenWorks.get(i).equals(workName)) {
                            works.setSelection(0);
                            return;
                        }
                    }
                    for (int j = 0; j < cW.size(); j++) {
                        if (cW.get(j).getAbbriviatedName().equals(workName)) {
                            addAmount(cW.get(j).getCost(), workName, cW.get(j).getArbicName());
                        }
                    }
                }
                works.setSelection(0);
            }
            else if (parent.getSelectedItem() == teamLeaderSpinner.getSelectedItem()){
                if (parent.getSelectedItemPosition() == 0)
                    return;
                for (int i = 0 ; i < choosenWorkers.size() ; i++) {
                    if (teamLeaderSpinner.getSelectedItem().equals(choosenWorkers.get(i)))
                        return;
                }
                choosenWorkers.add((String) teamLeaderSpinner.getSelectedItem());
                workersChoosenList.notifyDataSetChanged();
            }
        }
    }

    private void addAmount(final String cost, final String name, final String  arabic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("כמות");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    a = Integer.parseInt(input.getText().toString());
                    try {
                        p = Double.parseDouble(cost);
                    } catch(NumberFormatException nfe) {
                    }
                    p = p*a;
                    profit = profit + p;
                    String amount = String .valueOf(a);
                    String price = String.valueOf(p);
                    WorkPerfomed wP = new WorkPerfomed(name, arabic, price, amount);
                    choosenWorks.add(wP);
                    choosenWorksName.add(name);
                    choosenWorksAmount.add(amount);
                    worksChoosenList.notifyDataSetChanged();
                    amountAdapter.notifyDataSetChanged();
                    profitSumText.setText(String.valueOf(profit));
                } catch(NumberFormatException nfe) {
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
        Intent i = new Intent(this, OpenPakaPage.class);
        i.putExtra("language", language);
        finish();
        startActivity(i);
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
        ArrayList<WorkPerfomed> workPerfomed = new ArrayList<>();
        String price = profitSumText.getText().toString();
        String commit = pakaCommitText.getText().toString();
        for (int i=0 ; i<teamLeaderChoosen.size() ; i++)
            teamLeader.add(teamLeaderChoosen.get(i));
        teamLeader.add((String)teamLeaderSpinner.getSelectedItem());
        for (int i=0 ; i < choosenWorkers.size() ; i++)
            workers.add(choosenWorkers.get(i));
        for (int i=0 ; i < choosenWorks.size() ; i++) {
            workPerfomed.add(choosenWorks.get(i));
        }
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
        for (int i=0 ; i < workPerfomed.size() ; i++) {
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("workName").setValue(workPerfomed.get(i).getWorkName());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("workNameArabic").setValue(workPerfomed.get(i).getWorkNameArabic());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("price").setValue(workPerfomed.get(i).getPrice());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("amount").setValue(workPerfomed.get(i).getAmount());
        }
        dataRefPaka.child(countChange).child("price").setValue(price);
        dataRefPaka.child(countChange).child("commit").setValue(commit);
        teamLeaderChoosen.clear();
    }
}
