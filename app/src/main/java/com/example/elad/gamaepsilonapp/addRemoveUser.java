package com.example.elad.gamaepsilonapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

public class addRemoveUser extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private String countChange = "";
    private String tempPermission;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private Spinner permission;
    private EditText userEmail;
    private CheckBox constractor;
    private Button addUserButton;
    private EditText userPassword;
    private ListView workerListView;
    private View focusView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("userTable");
    private DatabaseReference dataRefChld = dataRef.child(countChange);
    private DatabaseReference dataFirstName = dataRefChld.child("firstName");
    private DatabaseReference dataLastName = dataRefChld.child("lastName");
    private DatabaseReference dataUserEmail= dataRefChld.child("userMail");
    private DatabaseReference dataPhoneNumber = dataRefChld.child("phoneNumber");
    private DatabaseReference dataPermission = dataRefChld.child("permission");
    private DatabaseReference dataConstractor = dataRefChld.child("constractor");
    private Query mQuery;
    private UserListView mMyAdapter;
    private ArrayList<User> mAdapterUser;
    private ArrayList<String> mAdapterKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_user);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        permission = (Spinner) findViewById(R.id.permission);
        userEmail = (EditText)findViewById(R.id.emailText);
        constractor = (CheckBox) findViewById(R.id.constractorCheckbox);
        addUserButton = (Button) findViewById(R.id.addUserButton);
        userPassword = (EditText)findViewById(R.id.userPassword);
      //  workerListView = (ListView) findViewById(R.id.workerListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.permissionOptions,
                android.R.layout.simple_spinner_item);
        permission.setAdapter(adapter);
        permission.setOnItemSelectedListener(this);
        addUserButton.setOnClickListener(new onClickListener());
        handleInstanceState(savedInstanceState);
        setupFirebase();
        setupRecyclerview();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        for (DataSnapshot postSnapshot: dataSnapshot.) {
//            // TODO: handle the post
//        }
        countChange ="" + dataSnapshot.getChildrenCount();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tempPermission = (String)parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == addUserButton.getId()){
                if (firstName.getText().toString().equals("")){
                    userError(1);
                    return;
                }
                if (lastName.getText().toString().equals("")){
                    userError(2);
                    return;
                }
                if (phoneNumber.getText().toString().equals("")){
                    userError(3);
                    return;
                }
                if (tempPermission.equals("בחר סוג עובד")){
                    userError(4);
                    return;
                }
                if (userEmail.getText().toString().equals("")){
                    userError(5);
                    return;
                }
                if (userPassword.getText().toString().equals("")){
                    userError(6);
                    return;
                }
                addUser(v);
            }
        }
    }
    private void userError(int i) {
        switch (i){
            case 1:{
                Toast.makeText(addRemoveUser.this, "אנא רשום שם פרטי", Toast.LENGTH_SHORT).show();
                focusView = firstName;
                focusView.requestFocus();
            }
            case 2:{
                Toast.makeText(addRemoveUser.this, "אנא רשום שם משפחה", Toast.LENGTH_SHORT).show();
                focusView = lastName;
                focusView.requestFocus();
            }
            case 3:{
                Toast.makeText(addRemoveUser.this, "אנא רשום מספר טלפון", Toast.LENGTH_SHORT).show();
                focusView = phoneNumber;
                focusView.requestFocus();
            }
            case 4:{
                Toast.makeText(addRemoveUser.this, "אנא בחר סוג עובד", Toast.LENGTH_SHORT).show();
                focusView = permission;
                focusView.requestFocus();
            }
            case 5:{
                Toast.makeText(addRemoveUser.this, "אנא רשום אימייל", Toast.LENGTH_SHORT).show();
                focusView = userEmail;
                focusView.requestFocus();
            }
            case 6:{
                Toast.makeText(addRemoveUser.this, "אנא בחר סיסמא", Toast.LENGTH_SHORT).show();
                focusView = userPassword;
                focusView.requestFocus();
            }
        }
    }

    private void addUser(View v) {
        String fN = firstName.getText().toString();
        String lN = lastName.getText().toString();
        String pN = phoneNumber.getText().toString();
        String per = tempPermission;
        String userMail = userEmail.getText().toString();
        boolean con = constractor.isChecked();
        String pass = userPassword.getText().toString();
        if (emailAndPasswordCheck(userMail, pass)) {
            mAuth.createUserWithEmailAndPassword(userMail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        Toast.makeText(addRemoveUser.this, "הרישום הצליח", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(addRemoveUser.this, "הרישום נכשל", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            //dialog if to open worker user
        }
        dataRef.child(countChange).child("firstName").setValue(fN);
        dataRef.child(countChange).child("lastName").setValue(lN);
        dataRef.child(countChange).child("userMail").setValue(userMail);
        dataRef.child(countChange).child("phoneNumber").setValue(pN);
        dataRef.child(countChange).child("permission").setValue(per);
        dataRef.child(countChange).child("constractor").setValue(con);
    }

    private boolean emailAndPasswordCheck(String email, String password) {
        // check email exsist in database
        //check email is correct
        //check password length
        return true;
    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_ADAPTER_ITEMS) &&
                savedInstanceState.containsKey(SAVED_ADAPTER_KEYS)) {
            mAdapterUser = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ADAPTER_ITEMS));
            mAdapterKeys = savedInstanceState.getStringArrayList(SAVED_ADAPTER_KEYS);
        } else {
            Toast.makeText(this,"123" , Toast.LENGTH_SHORT).show();
            mAdapterUser = new ArrayList<User>();
            mAdapterKeys = new ArrayList<String>();
        }
    }

    private void setupFirebase() {
        String firebaseLocation = getResources().getString(R.string.firebase_location);
        mQuery = database.getReferenceFromUrl(firebaseLocation);
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = new RecyclerView(this);
        mMyAdapter = new UserListView(mQuery,User.class, mAdapterUser, mAdapterKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ADAPTER_ITEMS, Parcels.wrap(mMyAdapter.getItems()));
        outState.putStringArrayList(SAVED_ADAPTER_KEYS, mMyAdapter.getKeys());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyAdapter.destroy();
    }
}
