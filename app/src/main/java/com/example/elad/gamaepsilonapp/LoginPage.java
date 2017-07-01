package com.example.elad.gamaepsilonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity implements ValueEventListener{

    private String permission = "";
    private EditText username;
    private EditText password;
    private Button acceptButton;
    private String usernameText;
    private String passwordText;
    private View focusView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("userTable");
    private User thisUser = new User();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText)findViewById(R.id.userNameText);
        password = (EditText)findViewById(R.id.passwordText);
        acceptButton = (Button)findViewById(R.id.addUserButton);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        acceptButton.setOnClickListener(new onClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("בודק חיבור...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dataRef.addValueEventListener(this);
        if (currentUser != null) {
            thisUser.setEmail(mAuth.getCurrentUser().getEmail());
        }
        else
            mProgressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        username.setText("");

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot d : dataSnapshot.getChildren()){
            String s = (String)d.child("userMail").getValue();
            if (s != null)
                if (s.equals(thisUser.getEmail()))
                    permission = (String)d.child("permission").getValue();
        }
        whoIsTheUser(currentUser);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private class onClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == acceptButton.getId())
                attemptLogin();
        }
    }

    private void whoIsTheUser(FirebaseUser currentUser) {
        if (permission.equals("מנהל צוות")){
            mProgressDialog.dismiss();
            startActivityButton(1);
        }
        if (permission.equals("מנהל ראשי")) {
            mProgressDialog.dismiss();
            startActivityButton(2);
        }
        mProgressDialog.dismiss();
    }

    private void startActivityButton(int butt) {
        if (butt == 1){
            Intent i = new Intent(this, OpenPakaPage.class);
            i.putExtra("language", true);
            finish();
            startActivity(i);
        }
        else if (butt == 2){
            Intent i = new Intent(this, MainPage.class);
            finish();
            startActivity(i);
        }
    }

    private void attemptLogin() {
        usernameText = username.getText().toString();
        passwordText = password.getText().toString();
        if (!isValid())
            return;
        mAuth.signInWithEmailAndPassword(usernameText, passwordText).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            thisUser.setEmail(currentUser.getEmail());
                            onStart();
                        }
                        else {
                            usernameError(2);
                            focusView = username;
                            focusView.requestFocus();
                        }
                    }
                });
    }

    private boolean isValid() {
        boolean valid = true;
        focusView = null;
        if (TextUtils.isEmpty(usernameText)) {
            usernameError(1);
            focusView = username;
            focusView.requestFocus();
            valid = false;
            return valid;
        }
        if (TextUtils.isEmpty(passwordText)) {
            usernameError(3);
            focusView = password;
            focusView.requestFocus();
            valid = false;
            return valid;
        }
        return valid;
    }

    private void usernameError(int num) {
        if (num == 1)
            Toast.makeText(this,"שם המשתמש אינו תקין", Toast.LENGTH_LONG).show();
        if (num == 2)
            Toast.makeText(this,"שם המשתמש לא קיים או הסיסמא אינה נכונה", Toast.LENGTH_LONG).show();
        if (num == 3)
            Toast.makeText(this,"סיסמא אינה נכונה", Toast.LENGTH_LONG).show();
    }
}
