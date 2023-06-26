package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private TextView mTextView;

    int newId;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextView = (TextView) findViewById(R.id.text);
        final EditText usernameEditText = findViewById(R.id.editTextEmail);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        final Spinner targetSpinner = findViewById(R.id.editTextTargetLang);
        final Spinner nativeSpinner = findViewById(R.id.editTextNativeLang);

        ArrayAdapter<String>targetAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        targetSpinner.setAdapter(targetAdapter);

        ArrayAdapter<String>nativeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        nativeSpinner.setAdapter(nativeAdapter);


        Button submitButton = (Button) findViewById(R.id.submitButton);

        // new list to store emails
        ArrayList<String> emailList = new ArrayList<String>();

        // connect to db
        ref = FirebaseDatabase.getInstance().getReference().child("Account");

        // we use this to count how many accounts there are
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear email list
                emailList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot accountData : snapshot.getChildren()) {
                        Account account = accountData.getValue(Account.class);
                        emailList.add(account.username.toUpperCase());
                        newId = account.id;
                    }

                } else {
                    newId = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account;

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String learnLang = targetSpinner.getSelectedItem().toString();
                String prefLang = nativeSpinner.getSelectedItem().toString();

                if (emailList.contains(username.toUpperCase())) {
                    Toast.makeText(Register.this, "That email is already associated with an account", Toast.LENGTH_SHORT).show();
                } else {
                    int userId = newId + 1;
                    account = new Account(userId, learnLang, password, prefLang, username);

                    // add new entry into db
                    ref.push().setValue(account);

                   // ((MyAccount) getApplication()).setKey(ref.push().getKey());


                    Toast.makeText(Register.this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();

                    ((MyAccount) getApplication()).setUserId(userId);
                    ((MyAccount) getApplication()).setUsername(username);
                    ((MyAccount) getApplication()).setPrefLang(prefLang);


                    setResult(RESULT_OK);

                    // destroy resources
                    finish();

                }
            }
        });
    }
}




                /*
                    try {
                        account = new Account(1, usernameEditText.getText().toString(), passwordEditText.getText().toString(), nativeEditText.getText().toString(), targetEditText.getText().toString());
                        //Toast.makeText(Register.this, account.toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e){
                        Toast.makeText(Register.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                        account = new Account(1, "error","error","error","error");
                    }
                    AccountDatabase accountDatabase = new AccountDatabase();
                    boolean success = accountDatabase.addUser(account);
                    if(success == true){
                        Toast.makeText(Register.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                    else {
                       // Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(Register.this, FlashcardActivity.class));
                }
            });
            }
}
        // Enables Always-on
        //setAmbientEnabled();
                 */