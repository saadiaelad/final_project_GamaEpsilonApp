package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;

public class PakaPage extends AppCompatActivity implements ValueEventListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private boolean language = true;
    double profit = 0;
    int amount;
    String countChange = "";
    private ArrayList<String> worksArray = new ArrayList<>();
    private ArrayList<String> arabicWorksList = new ArrayList<>();
    private ArrayList<String> choosenWorksArray = new ArrayList<>();
    private ArrayList<String> arabicChoosenWorksArray = new ArrayList<>();
    private ArrayList<String> amountArray = new ArrayList<>();
    private ArrayList<Works> cW = new ArrayList<>();
    private ArrayList<WorkPerfomed> choosenWorks = new ArrayList<>();
    private String pakaKey = null;
    private String whereFrom = null;
    private TextView nameText;
    private Spinner workOptionSpinner;
    private ListView worksList;
    private ListView amountListView;
    private Button closePakaButton;
    private Button backButton;
    private Button signOutButton;
    private Button pakaDetailsButton;
    private Button backToSearchPage;
    private Button saveButton;
    private ImageButton changeLanguage;
    private String username;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRefUser = database.getReference("userTable");
    private DatabaseReference dataRefPaka = database.getReference("pakaTable");
    private DatabaseReference dataRefWorks = database.getReference("worksTable");
    private ArrayAdapter<String> adapterWorks;
    private ArrayAdapter<String> adapterWorksArabic;
    private ArrayAdapter<String> workNameHebrewAdapter;
    private ArrayAdapter<String> workNameArabicAdapter;
    private ArrayAdapter<String> amountAdapter;
    private Paka paka;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paka_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        worksArray.add("הוסף עבודה");
        arabicWorksList.add("أضف على وظيفة");
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("pakaKey") != null)
            pakaKey = bundle.getString("pakaKey");
        if (bundle.getString("whereFrom") != null)
            whereFrom = bundle.getString("whereFrom");
        if (bundle.getString("language") != null)
            language = bundle.getBoolean("language");
        nameText = (TextView)findViewById(R.id.nameText);
        workOptionSpinner = (Spinner)findViewById(R.id.workOptionSpinner);
        worksList = (ListView)findViewById(R.id.worksList);
        amountListView = (ListView)findViewById(R.id.amountListView);
        closePakaButton = (Button)findViewById(R.id.closePakaButton);
        backButton = (Button)findViewById(R.id.backButton);
        signOutButton = (Button)findViewById(R.id.signOutButton);
        pakaDetailsButton = (Button)findViewById(R.id.detailsButton);
        backToSearchPage = (Button)findViewById(R.id.backToSearchPage);
        saveButton = (Button)findViewById(R.id.saveButton);
        changeLanguage = (ImageButton)findViewById(R.id.changeLanguage);
        backToSearchPage.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);
        closePakaButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
        signOutButton.setOnClickListener(new onClickListener());
        pakaDetailsButton.setOnClickListener(new onClickListener());
        backToSearchPage.setOnClickListener(new onClickListener());
        saveButton.setOnClickListener(new onClickListener());
        changeLanguage.setOnClickListener(new onClickListener());
        amountAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, amountArray);
        amountListView.setAdapter(amountAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRefPaka.addValueEventListener(this);
        dataRefWorks.addValueEventListener(this);
        dataRefUser.addValueEventListener(this);
        languageChange();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("userTable")) {
            for (DataSnapshot d:dataSnapshot.getChildren()){
                if ((d.child("userMail").getValue()).equals(currentUser.getEmail())){
                    username = (String)d.child("firstName").getValue();
                    if (language)
                        nameText.setText("שלום " + username);
                    else
                        nameText.setText("سلام " + username);
                }
            }
        }
        else if (dataSnapshot.getKey().equals("pakaTable")) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (d.child("pakaNum").getValue() != null) {
                    if (d.child("pakaNum").getValue().equals(pakaKey)) {
                        for (DataSnapshot ds:d.child("workPerfomed").getChildren())
                            choosenWorks.add(ds.getValue(WorkPerfomed.class));
                        paka = d.getValue(Paka.class);
                        setView(paka);
                    }
                }
            }
        }
        else if (dataSnapshot.getKey().equals("worksTable")) {
            for (DataSnapshot d:dataSnapshot.getChildren()){
                if (d.child("abbriviatedName").getValue() != null){
                    Works work = new Works((String)d.child("abbriviatedName").getValue(),
                            (String)d.child("arbicName").getValue(),
                            (String)d.child("cost").getValue(),
                            (String)d.child("fullName").getValue(),
                            (String)d.child("units").getValue(),
                            (String)d.child("workNum").getValue());
                    if (work != null) {
                        cW.add(work);
                    }
                    worksArray.add((String) d.child("abbriviatedName").getValue());
                    arabicWorksList.add((String) d.child("arbicName").getValue());
                }
            }
        }
    }

    private void setView(Paka paka) {
        if (whereFrom.equals("OpenPakaPage"))
            backButton.setVisibility(View.VISIBLE);
        else if (whereFrom.equals("SearchPage"))
            backToSearchPage.setVisibility(View.VISIBLE);
        if (paka.getWorkPerfomed() != null){
            for (int i = 0 ; i < paka.getWorkPerfomed().size() ; i++){
                choosenWorksArray.add(paka.getWorkPerfomed().get(i).getWorkName());
                arabicChoosenWorksArray.add(paka.getWorkPerfomed().get(i).getWorkNameArabic());
                amountArray.add(paka.getWorkPerfomed().get(i).getAmount());
                amountAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItem().equals("הוסף עבודה"))
            return;
        if (adapterView.getSelectedItem().equals("أضف على وظيفة"))
            return;
        for (int j = 0 ; j < arabicWorksList.size() ; j++){
            if (adapterView.getSelectedItem().equals(arabicWorksList.get(j)))
                addToArabicChoosenWorks((String) adapterView.getSelectedItem());
        }
        for (int j = 0 ; j < worksArray.size() ; j++){
            if (adapterView.getSelectedItem().equals(worksArray.get(j)))
                addToHebrewChoosenWorks((String) adapterView.getSelectedItem());
        }
    }

    private void addToHebrewChoosenWorks(String name) {
        choosenWorksArray.add(name);
        addAmount(name);
        workNameHebrewAdapter.notifyDataSetChanged();
    }

    private void addAmount(final String name) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("כמות");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    double p;
                    double workPrice = 0;
                    String otherLanguageName = null;
                    amount = Integer.parseInt(String.valueOf(input.getText()));
                    try {
                        p = Double.parseDouble(paka.getPrice());
                        if (profit == 0)
                            profit = profit + p;
                    } catch (NumberFormatException nfe) {
                    }
                    for (int i = 0 ; i < cW.size() ; i++){
                        if (cW.get(i).getAbbriviatedName().equals(name)) {
                            workPrice = Double.parseDouble(cW.get(i).getCost());
                            otherLanguageName = cW.get(i).getArbicName();
                        }
                        if (cW.get(i).getArbicName().equals(name)) {
                            workPrice = Double.parseDouble(cW.get(i).getCost());
                            otherLanguageName = cW.get(i).getAbbriviatedName();
                        }
                    }
                    profit = profit + (workPrice*amount);
                    String a = String.valueOf(amount);
                    String realProfit = String.valueOf(profit);
                    WorkPerfomed wP = new WorkPerfomed(name, otherLanguageName, realProfit, a);
                    choosenWorks.add(wP);
                    amountArray.add(a);
                    amountAdapter.notifyDataSetChanged();
                    if (language)
                        arabicChoosenWorksArray.add(otherLanguageName);
                    else
                        choosenWorksArray.add(otherLanguageName);
                } catch (NumberFormatException nfe) {
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

    private void addToArabicChoosenWorks(String name) {
        arabicChoosenWorksArray.add(name);
        addAmount(name);
        workNameArabicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getId() == closePakaButton.getId()) {
                // send exl
                startActivityButton(1);
            }
            else if (v.getId() == backButton.getId()) {
                startActivityButton(1);
            }
            else if (v.getId() == signOutButton.getId()) {
                mAuth.signOut();
                startActivityButton(2);
            }
            else if (v.getId() == pakaDetailsButton.getId()){
                startActivityButton(3);
            }
            else if (v.getId() == backToSearchPage.getId()){
                startActivityButton(4);
            }
            else if (v.getId() == changeLanguage.getId()){
                if (language)
                    language = false;
                else
                    language = true;
                languageChange();
            }
            else if (v.getId() == saveButton.getId()){
                addToDb();
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
        pakaDetailsButton.setText("تفاصيل");
        backButton.setText("عودة");
        backToSearchPage.setText("عودة");
        signOutButton.setText("الخروج");
        closePakaButton.setText("مغلق");
        saveButton.setText("المحافظة");
        adapterWorksArabic = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arabicWorksList);
        workOptionSpinner.setAdapter(adapterWorksArabic);
        workOptionSpinner.setOnItemSelectedListener(this);
        workNameArabicAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arabicChoosenWorksArray);
        worksList.setAdapter(workNameArabicAdapter);
        worksList.setOnItemClickListener(new onItemClickListener());
        adapterWorksArabic.notifyDataSetChanged();
        workNameArabicAdapter.notifyDataSetChanged();
    }

    private void putHebrew() {
        nameText.setText("שלום " + username);
        pakaDetailsButton.setText("פרטים");
        backButton.setText("חזרה");
        backToSearchPage.setText("חזרה");
        signOutButton.setText("התנתקות");
        closePakaButton.setText("סגירה");
        saveButton.setText("שמירה");
        adapterWorks = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, worksArray);
        workOptionSpinner.setAdapter(adapterWorks);
        workOptionSpinner.setOnItemSelectedListener(this);
        workNameHebrewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, choosenWorksArray);
        worksList.setAdapter(workNameHebrewAdapter);
        worksList.setOnItemClickListener(new onItemClickListener());
        workOptionSpinner.setEnabled(true);
        adapterWorks.notifyDataSetChanged();
        workNameHebrewAdapter.notifyDataSetChanged();
    }

    private void startActivityButton(int butt) {
        if (butt == 1){
            Intent i = new Intent(this, OpenPakaPage.class);
            i.putExtra("language", language);
            finish();
            startActivity(i);
        }
        else if (butt == 2){
            Intent i = new Intent(this, LoginPage.class);
            finish();
            startActivity(i);
        }
        else if (butt == 3){
            Intent i = new Intent(this, PakaDetails.class);
            if (pakaKey != null)
                i.putExtra("pakaKey", pakaKey);
            i.putExtra("whereFrom", whereFrom);
            i.putExtra("language", language);
            finish();
            startActivity(i);
        }
        else if (butt == 4){
            Intent i = new Intent(this, SearchPage.class);
            finish();
            startActivity(i);
        }
    }

    private class onItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

    private void addToDb(){
        for (int i=0 ; i < choosenWorks.size() ; i++) {
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("workName").setValue(choosenWorks.get(i).getWorkName());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("workNameArabic").setValue(choosenWorks.get(i).getWorkNameArabic());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("price").setValue(choosenWorks.get(i).getPrice());
            dataRefPaka.child(countChange).child("workPerfomed").child
                    ("" + i).child("amount").setValue(choosenWorks.get(i).getAmount());
        }
        Toast.makeText(PakaPage.this, "המידע נשמר בהצלחה", Toast.LENGTH_SHORT).show();
    }
}
