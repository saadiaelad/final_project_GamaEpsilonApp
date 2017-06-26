package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
        progressDialog.setMessage("אנא המתן, טוען נתונים");
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
            if (userEmail.getText().toString() != null)
                if (((String)d.child("userMail").getValue()).equals(userEmail.getText().toString()))
                    emailExist = true;
        }
    }

    private void displayList() {
        userAdapter = new FirebaseListAdapter<User>(this, User.class,
                R.layout.user_list_view, dataRef){

            @Override
            protected void populateView(View view, User user, int i) {
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
//                deleteImageButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//                editImageButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
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
                addUser(v);
            }
            else if (v.getId() == backButton.getId())
                startActivityButton();
        }
    }

    private void startActivityButton() {
        finish();
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

    private void addUser(View v) {
        final String fN = firstName.getText().toString();
        final String lN = lastName.getText().toString();
        final String pN = phoneNumber.getText().toString();
        final String per = permission.getSelectedItem().toString();
        final String userMail = userEmail.getText().toString();
        final boolean con = constractor.isChecked();
        String pass = userPassword.getText().toString();
        if (!emailExist) {
            mAuth.createUserWithEmailAndPassword(userMail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                        Toast.makeText(AddRemoveUser.this, "הרישום הצליח", Toast.LENGTH_SHORT).show();
                    else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException)
                            Toast.makeText(AddRemoveUser.this, "הרישום נכשל, המייל קיים במערכת", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddRemoveUser.this, "הרישום נכשל, המייל אינו תקין", Toast.LENGTH_SHORT).show();
                    }
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
            builder.setTitle("addWithoutMail")
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

    private void makeAnotherOne() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("addAnotherUser")
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

    private boolean emailAndPasswordCheck(String email, String password) {
        // check email exsist in database
        //check email is correct
        //check password length
        return true;
    }
}
