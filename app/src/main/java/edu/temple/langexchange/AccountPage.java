package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AccountPage extends AppCompatActivity {



    Button button;
    EditText passwordUpdate, username;
    Spinner prefLangUpdate, learningLangUpdate;
    DatabaseReference ref;
    String userName,Password;
  //  public String  key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);


        userName = ((MyAccount) getApplication()).getUsername();

        button = findViewById(R.id.updateButton);
        passwordUpdate = findViewById(R.id.passwordUpdate);
        prefLangUpdate = findViewById(R.id.prefLangSpinner);
        learningLangUpdate = findViewById(R.id.learnLangSpinner);
        username = findViewById(R.id.usernameDisplay);

        username.setText(userName);


        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        learningLangUpdate.setAdapter(targetAdapter);

        ArrayAdapter<String>nativeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        prefLangUpdate.setAdapter(nativeAdapter);


        int userNameController = userName.indexOf("@");
        System.out.println("username received: " + userName);
        ref = FirebaseDatabase.getInstance().getReference().child("Account");
        Map<String, Object> updates = new HashMap<String,Object>();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = passwordUpdate.getText().toString();
                String userNameUPdate = username.getText().toString();
                String learnLang = learningLangUpdate.getSelectedItem().toString();
                String prefLang = prefLangUpdate.getSelectedItem().toString();
                String keyName;



                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Account");


               // String key = ((MyAccount) getApplication()).getKey();


                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("Account")
                        .orderByChild("username")
                        .equalTo(userName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    String key = snapshot.getKey();
                                    updates.put(key +"/username" , userNameUPdate);
                                    updates.put(key +"/password" , pass);
                                    updates.put(key +"/learnLang" , learnLang);
                                    updates.put(key +"/prefLang" , prefLang);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                ref2.updateChildren(updates);
                Toast.makeText(AccountPage.this, "Your account information was successfully updated.", Toast.LENGTH_SHORT).show();

            }
        });



    }



    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;


    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch ((item.getItemId())){
            case R.id.ic_logout:{
                ((MyAccount) getApplication()).setUserId(-1);
                ((MyAccount) getApplication()).setUsername("");
                ((MyAccount) getApplication()).setPrefLang("");
                Intent intent = new Intent(AccountPage.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(AccountPage.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }


}