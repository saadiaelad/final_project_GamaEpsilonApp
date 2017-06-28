package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRemoveUser extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private boolean emailExist = false;
    private String countChange = "";
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private Spinner permission;
    private EditText userEmail;
    private CheckBox constractor;
    private Button addUserButton;
    private Button backButton;
    private EditText userPassword;
    private ListView workerListView;
    private View focusView;
    private ProgressDialog progressDialog;
    private FirebaseListAdapter<User> userAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("userTable");

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
        backButton = (Button) findViewById(R.id.backButton);
        userPassword = (EditText)findViewById(R.id.userPassword);
        workerListView = (ListView) findViewById(R.id.workerListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.permissionOptions,
                android.R.layout.simple_spinner_item);
        permission.setAdapter(adapter);
        permission.setOnItemSelectedListener(this);
        addUserButton.setOnClickListener(new onClickListener());
        backButton.setOnClickListener(new onClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        focusView = firstName;
        focusView.requestFocus();
        dataRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        displayList();
        progressDialog.dismiss();
        countChange ="" + dataSnapshot.getChildrenCount();
        for (DataSnapshot d:dataSnapshot.getChildren()){
            if (userEmail.getText() != null && d.child("userMail").getValue() != null) {
                if (((String) d.child("userMail").getValue()).equals(userEmail.getText().toString()))
                    emailExist = true;
            }
        }
    }

    private void displayList() {
        userAdapter = new FirebaseListAdapter<User>(this, User.class,
                R.layout.user_list_view, dataRef){

            @Override
            protected void populateView(View view, final User user, int i) {
                TextView firstName = (TextView)view.findViewById(R.id.firstNameText);
                TextView lastName = (TextView)view.findViewById(R.id.lastNameText);
                TextView permission = (TextView)view.findViewById(R.id.jobTitleText);
                TextView phoneNumber = (TextView)view.findViewById(R.id.phoneNumberText);
                TextView email = (TextView)view.findViewById(R.id.emailText);
                ImageButton deleteImageButton = (ImageButton)view.findViewById(R.id.deleteImageButton);
                ImageButton editImageButton = (ImageButton)view.findViewById(R.id.editImageButton);
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                permission.setText(user.getPermission());
                phoneNumber.setText(user.getPhoneNumber());
                email.setText(user.getEmail());
                deleteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                editImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        workerListView.setAdapter(userAdapter);
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
                if (permission.getSelectedItemPosition() == 0){
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
                addUser();
            }
            else if (v.getId() == backButton.getId())
                startActivityButton();
        }
    }

    private void startActivityButton() {
        Intent i = new Intent(this, ManagmentPage.class);
        finish();
        startActivity(i);
    }

    private void userError(int i) {
        switch (i){
            case 1:{
                Toast.makeText(AddRemoveUser.this, "אנא רשום שם פרטי", Toast.LENGTH_SHORT).show();
                focusView = firstName;
                focusView.requestFocus();
            }
            case 2:{
                Toast.makeText(AddRemoveUser.this, "אנא רשום שם משפחה", Toast.LENGTH_SHORT).show();
                focusView = lastName;
                focusView.requestFocus();
            }
            case 3:{
                Toast.makeText(AddRemoveUser.this, "אנא רשום מספר טלפון", Toast.LENGTH_SHORT).show();
                focusView = phoneNumber;
                focusView.requestFocus();
            }
            case 4:{
                Toast.makeText(AddRemoveUser.this, "אנא בחר סוג עובד", Toast.LENGTH_SHORT).show();
                focusView = permission;
                focusView.requestFocus();
            }
            case 5:{
                Toast.makeText(AddRemoveUser.this, "אנא רשום אימייל", Toast.LENGTH_SHORT).show();
                focusView = userEmail;
                focusView.requestFocus();
            }
            case 6:{
                Toast.makeText(AddRemoveUser.this, "אנא בחר סיסמא", Toast.LENGTH_SHORT).show();
                focusView = userPassword;
                focusView.requestFocus();
            }
        }
    }

    private void addUser() {
        final String thisUserName = currentUser.getEmail().toString();
        final String[] thisUserPassword = new String[1];
        final String fN = firstName.getText().toString();
        final String lN = lastName.getText().toString();
        final String pN = phoneNumber.getText().toString();
        final String per = permission.getSelectedItem().toString();
        final String userMail = userEmail.getText().toString();
        final boolean con = constractor.isChecked();
        String pass = userPassword.getText().toString();
        if (emailExist)
            Toast.makeText(AddRemoveUser.this, "הרישום נכשל, המייל כבר קיים במערכת", Toast.LENGTH_SHORT).show();
        if (userMail.matches("@gmail.com")) {
            mAuth.createUserWithEmailAndPassword(userMail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddRemoveUser.this, "הרישום הצליח", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        thisUserPassword[0] = openDialog();
                        mAuth.signInWithEmailAndPassword(thisUserName, thisUserPassword[0]);
                    }
                    else
                        Toast.makeText(AddRemoveUser.this, "הרישום נכשל, המייל אינו תקין", Toast.LENGTH_SHORT).show();
                }
            });
            dataRef.child(countChange).child("firstName").setValue(fN);
            dataRef.child(countChange).child("lastName").setValue(lN);
            dataRef.child(countChange).child("userMail").setValue(userMail);
            dataRef.child(countChange).child("phoneNumber").setValue(pN);
            dataRef.child(countChange).child("permission").setValue(per);
            dataRef.child(countChange).child("constractor").setValue(con);
            makeAnotherOne();
        }
        else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("הוספת עובד ללא מייל תקין")
                    .setMessage("המייל או הסיסמא אינם תקינים, המערכת מכינה משתמש מסוג -עובד- האם ברצונך להמשיך?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dataRef.child(countChange).child("firstName").setValue(fN);
                            dataRef.child(countChange).child("lastName").setValue(lN);
                            dataRef.child(countChange).child("userMail").setValue(userMail);
                            dataRef.child(countChange).child("phoneNumber").setValue(pN);
                            dataRef.child(countChange).child("permission").setValue(per);
                            dataRef.child(countChange).child("constractor").setValue(con);
                            Toast.makeText(AddRemoveUser.this, "העובד נשמר במערכת", Toast.LENGTH_SHORT).show();
                            makeAnotherOne();
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
    }

    private String openDialog() {
        String pass;
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alert.setView(input);
        alert.setTitle("Password Required");
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pass = input.getText().toString();
        if (pass == null)
            pass = "gamagama";
        return pass;
    }


    private void makeAnotherOne() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("הוספת משתמש נוסף")
                .setMessage("האם ברצונך להכניס משתמש נוסף?")
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
        firstName.setText("");
        lastName.setText("");
        userEmail.setText("");
        phoneNumber.setText("");
        userPassword.setText("");
        permission.setSelection(0);
        constractor.setChecked(false);
        focusView = firstName;
        focusView.requestFocus();
    }
}
