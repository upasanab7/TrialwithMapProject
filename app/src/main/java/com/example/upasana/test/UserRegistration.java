package com.example.upasana.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistration extends AppCompatActivity {
    private EditText memail, mname,mphone;
    private Button mregister;

    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        mauth= FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!= null){
                    Intent intent = new Intent(UserRegistration.this, maptest.class);
                    startActivity(intent);
                    finish();

                }
            }
        };

        mname=(EditText)findViewById(R.id.name);
        mphone=(EditText)findViewById(R.id.phone);
        memail= (EditText) findViewById(R.id.email);

        mregister= (Button) findViewById(R.id.register);

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name=mname.getText().toString();
                final String phone=mphone.getText().toString();
                final String email = memail.getText().toString();
                Log.i("UL","Email and name obtained "+email+name);
                mauth.createUserWithEmailAndPassword(email,name).addOnCompleteListener(UserRegistration.this, new OnCompleteListener<AuthResult>()
                {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.e("UL", "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(UserRegistration.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i("UL","Entered else block"+email+name);
                            String user_id = mauth.getCurrentUser().getUid();
                            DatabaseReference current_user_name  = FirebaseDatabase.getInstance().getReference().child("Customers").child(phone).child("Name");
                            current_user_name.setValue(name);
                            DatabaseReference current_user_email  = FirebaseDatabase.getInstance().getReference().child("Customers").child(phone).child("Email");
                            current_user_email.setValue(email);
                            DatabaseReference current_user_id  = FirebaseDatabase.getInstance().getReference().child("Customers").child(phone).child("User ID");
                            current_user_id.setValue(user_id);
                            Log.i("UL","Entered in database "+email+name);

                        }
                    }

                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(firebaseAuthListener);
    }

    }

