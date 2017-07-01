package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

    private String pakaNumber;
    private String address;
    private String openDate;
    private String startingDate;
    private String closingDate;
    private String profit;
    private String supervisor;
    private String classString;
    private boolean openOrClose;
    private boolean tat;
    private String periorty;
    private String commit;
    private boolean language = true;
    private String pakaKey = null;
    private String whereFrom = null;
    private String permission;
    private ArrayList<String> teamLeaderList = new ArrayList<>();
    private ArrayList<String> workersList = new ArrayList<>();
    private ArrayList<String> arabicWorksList = new ArrayList<>();
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
    private TextView teamLeaderListTextView;
    private TextView workersListTextView;
    private TextView worksListTextView;
    private ListView teamLeaderListViewText;
    private ListView workersListViewText;
    private ListView worksListViewText;
    private Button backToPakaPage;
    private ImageButton changeLanguage;
    private FirebaseListAdapter<String> workerAdapter;
    private FirebaseListAdapter<String> workAdapter;
    private FirebaseListAdapter<String> teamLeaderAdapter;
    private ArrayAdapter<String> worksAdapter;
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
        supervisorText = (TextView)findViewById(R.id.supervisorText);
        periortyText = (TextView)findViewById(R.id.periortyText);
        openDateText = (TextView)findViewById(R.id.openDateText);
        startDateText = (TextView)findViewById(R.id.makingDateText);
        closeDateText = (TextView)findViewById(R.id.closingDateText);
        profitText = (TextView)findViewById(R.id.profitSumText);
        pakaCommitText = (TextView)findViewById(R.id.pakaCommitText);
        teamLeaderListTextView = (TextView)findViewById(R.id.teamLeaderTextView);
        workersListTextView = (TextView)findViewById(R.id.workersTextView);
        worksListTextView = (TextView)findViewById(R.id.worksTextView);
        teamLeaderListViewText = (ListView)findViewById(R.id.teamLeaderText);
        workersListViewText = (ListView)findViewById(R.id.workersListViewText);
        worksListViewText = (ListView)findViewById(R.id.worksListViewText);
        backToPakaPage = (Button)findViewById(R.id.backToOpenPakaPage);
        changeLanguage = (ImageButton)findViewById(R.id.changeLanguage);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("pakaKey") != null)
            pakaKey = bundle.getString("pakaKey");
        if (bundle.getString("whereFrom") != null)
        whereFrom = bundle.getString("whereFrom");
        if (bundle.getString("language") != null)
            language = bundle.getBoolean("language");
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

        backToPakaPage.setOnClickListener(new onClickListener());
        changeLanguage.setOnClickListener(new onClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefPaka.addValueEventListener(this);
        dataRefUser.addValueEventListener(this);
    }

    private void updateUi() {
        if (!isAdmin()){
            View v = new View(getApplicationContext());
            profitText.setVisibility(v.INVISIBLE);
        }
        languageChange();
    }

    private boolean isAdmin() {
        if (permission != null)
            if (permission.equals("מנהל ראשי"))
                return true;
        return false;
    }

    private class onClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == backToPakaPage.getId()){
                startActivityButton();
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
        backToPakaPage.setText("عودة");
        teamLeaderListTextView.setText("المسؤولين");
        workersListTextView.setText("اسماء العمال");
        worksListTextView.setText("اسماء العمل");
        pakaNumberText.setText("رقم الباقه:  " + pakaNumber);
        addressText.setText("عنوان:  " + address);
        classText.setText("قسم:  " + classString);
        if (openOrClose)
            openOrCloseText.setText("وضع:  مغلق");
        else
            openOrCloseText.setText("وضع:  فتح");
        if (tat)
            tatOrNotText.setText("شركه hot");
        else
            tatOrNotText.setText("ليس لهوتhot");
        openDateText.setText("تاريخ الورشه:  " + openDate);
        startDateText.setText("تاريخ انهاء الورشه:  " + startingDate);
        closeDateText.setText("تاريخ اغلاق الورشه:  " + closingDate);
        supervisorText.setText("اسم المسؤول عن العمل:  " + supervisor);
        periortyText.setText("الضروره:  " + periorty);
        profitText.setText("الربح:  " + profit);
        pakaCommitText.setText("ملاحظات:  " + commit);
        worksAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arabicWorksList);
        worksListViewText.setAdapter(worksAdapter);
    }

    private void putHebrew() {
        backToPakaPage.setText("חזרה");
        teamLeaderListTextView.setText("רשימת מנהלי צוותים");
        workersListTextView.setText("רשימת עובדים");
        worksListTextView.setText("רשימת עבודות");
        pakaNumberText.setText("מספר פקע:  " + pakaNumber);
        addressText.setText("כתובת:  " + address);
        classText.setText("מחלקה:  " + classString);
        if (openOrClose)
            openOrCloseText.setText("מצב:  סגור");
        else
            openOrCloseText.setText("מצב:  פתוח");
        if (tat)
            tatOrNotText.setText("בהוט");
        else
            tatOrNotText.setText("לא בהוט");
        openDateText.setText("תאריך פתיחה:  " + openDate);
        startDateText.setText("תאריך ביצוע:  " + startingDate);
        closeDateText.setText("תאריך סגירה:  " + closingDate);
        supervisorText.setText("שם המפקח:  " + supervisor);
        periortyText.setText("דחיפות:  " + periorty);
        profitText.setText("רווח:  " + profit);
        pakaCommitText.setText("הערות:  " + commit);
        worksAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worksList);
        worksListViewText.setAdapter(worksAdapter);
    }

    private void startActivityButton() {
        Intent i = new Intent(this, PakaPage.class);
        if (pakaKey != null)
            i.putExtra("pakaKey", pakaKey);
        i.putExtra("whereFrom", whereFrom);
        i.putExtra("language", language);
        finish();
        startActivity(i);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("pakaTable")) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (d.getValue() != null) {
                    if (d.child("pakaNum").getValue() != null) {
                        if (d.child("pakaNum").getValue().toString().equals(pakaKey)) {
                            pakaNumber = (String)d.child("pakaNum").getValue();
                            if (d.child("address").getValue() != null)
                                address = (String) d.child("address").getValue();
                            if (d.child("className").getValue() != null)
                                classString = (String)d.child("className").getValue();
                            openOrClose = d.child("openOrClose").getValue(boolean.class);
                            tat = d.child("tat").getValue(boolean.class);
                            if (d.child("openDate").getValue() != null)
                                openDate = (String) d.child("openDate").getValue();
                            if (d.child("startingDate").getValue() != null)
                                startingDate = (String) d.child("startingDate").getValue();
                            if (d.child("closingDate").getValue() != null)
                                closingDate = (String) d.child("closingDate").getValue();
                            if (d.child("supervisor").getValue() != null)
                                supervisor = (String) d.child("supervisor").getValue();
                            if (d.child("periorty").getValue() != null)
                                periorty = (String) d.child("periorty").getValue();
                            if (d.child("price").getValue() != null)
                                profit = (String) d.child("price").getValue();
                            if (d.child("commit").getValue() != null)
                                commit = (String) d.child("commit").getValue();
                            if (d.child("pakaNum").getValue() != null)
                                for (DataSnapshot ds : d.child("teamLeader").getChildren())
                                    if (ds.getValue() != null)
                                        teamLeaderList.add((String) ds.getValue());
                            if (d.child("pakaNum").getValue() != null)
                                for (DataSnapshot ds : d.child("workers").getChildren())
                                    if (ds.getValue() != null)
                                        workersList.add((String) ds.getValue());
                            if (d.child("pakaNum").getValue() != null) {
                                for (DataSnapshot ds : d.child("workPerfomed").getChildren()) {
                                    if (ds.child("workName").getValue() != null) {
                                        worksList.add(ds.child("workName").getValue() +
                                                "    כמות:   " + ds.child("amount").getValue());
                                    }
                                    if (ds.child("workNameArabic").getValue() != null)
                                        arabicWorksList.add(ds.child("workNameArabic").getValue() +
                                                "    الكميه. أو المبلغ:   " + ds.child("amount").getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (dataSnapshot.getKey().equals("userTable")){
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (d.child("userMail").getValue() != null && currentUser.getEmail() != null)
                    if (((String)d.child("userMail").getValue()).equals(currentUser.getEmail()))
                        permission = (String)d.child("permission").getValue();
            }
            updateUi();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
