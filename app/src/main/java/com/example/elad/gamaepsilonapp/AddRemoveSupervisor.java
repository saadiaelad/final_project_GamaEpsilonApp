package com.example.elad.gamaepsilonapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRemoveSupervisor extends AppCompatActivity implements ValueEventListener {

    String countChange = "";
    private EditText supervisorName;
    private EditText supervisorWorkPlace;
    private Button addSupervisor;
    private Button backButton;
    private ListView supervisorListView;
    private View focusView;
    private ProgressDialog progressDialog;
    private FirebaseListAdapter<Supervisor> adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataRef = database.getReference("supervisorTable");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_supervisor);

        supervisorName = (EditText)findViewById(R.id.supervisorName);
        supervisorWorkPlace = (EditText)findViewById(R.id.workPlace);
        addSupervisor = (Button)findViewById(R.id.addSupervisorButton);
        backButton = (Button)findViewById(R.id.backButton);
        supervisorListView = (ListView)findViewById(R.id.supervisorListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("טוען...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        addSupervisor.setOnClickListener(new OnClickListener());
        backButton.setOnClickListener(new OnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataRef.addValueEventListener(this);
        focusView = supervisorName;
        focusView.requestFocus();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        displayList();
        progressDialog.dismiss();
        countChange ="" + dataSnapshot.getChildrenCount();
    }

    private void displayList() {
        adapter = new FirebaseListAdapter<Supervisor>(this, Supervisor.class,
                R.layout.supervisor_list_view, dataRef) {
            @Override
            protected void populateView(View view, Supervisor supervisor, int i) {
                TextView supervisorName = (TextView)view.findViewById(R.id.supervisorNameText);
                TextView supervisorWorkPlace = (TextView)view.findViewById(R.id.workPlaceText);
                ImageButton deleteImageButton = (ImageButton)view.findViewById(R.id.deleteImageButton);
                ImageButton editImageButton = (ImageButton)view.findViewById(R.id.editImageButton);
                supervisorName.setText(supervisor.getName());
                supervisorWorkPlace.setText(supervisor.getWorkPlace());
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
        supervisorListView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == addSupervisor.getId()) {
                if (supervisorName.getText().toString().equals("")) {
                    userError(1);
                    return;
                }
                if (supervisorWorkPlace.getText().toString().equals("")) {
                    userError(2);
                    return;
                }
                addToDb(v);
            } else if (v.getId() == backButton.getId())
                startActivityButton();
        }
    }

    private void startActivityButton() {
        finish();
    }

    private void addToDb(View v) {
        String sN = supervisorName.getText().toString();
        String wP = supervisorWorkPlace.getText().toString();

        dataRef.child(countChange).child("supervisorName").setValue(sN);
        dataRef.child(countChange).child("workPlace").setValue(wP);

        Toast.makeText(this, "המפקח נשמר בהצלחה", Toast.LENGTH_SHORT).show();
        makeAnotherOne();
    }

    private void makeAnotherOne() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("הוספת מפקח נוסף")
                .setMessage("האם ברצונך להכניס מפקח נוסף?")
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
        supervisorName.setText("");
        supervisorWorkPlace.setText("");
        focusView = supervisorName;
        focusView.requestFocus();
    }

    private void userError(int i) {
        switch (i) {
            case 1: {
                Toast.makeText(AddRemoveSupervisor.this, "אנא רשום שם מפקח", Toast.LENGTH_SHORT).show();
                focusView = supervisorName;
                focusView.requestFocus();
            }
            case 2: {
                Toast.makeText(AddRemoveSupervisor.this, "אנא רשום מקום עבודה", Toast.LENGTH_SHORT).show();
                focusView = supervisorWorkPlace;
                focusView.requestFocus();
            }
        }
    }


}

