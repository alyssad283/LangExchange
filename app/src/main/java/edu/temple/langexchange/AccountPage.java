package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountPage extends AppCompatActivity {



    Button button;
    EditText passwordUpdate, username;
    Spinner prefLangUpdate, learningLangUpdate;
    DatabaseReference ref;
    String userName,Password, learnLang, nativeLang;
    int learnSelection, nativeSelection;
  //  public String  key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);


        userName = ((MyAccount) getApplication()).getUsername();
        Password = ((MyAccount) getApplication()).getPassword();
      nativeLang= ((MyAccount) getApplication()).getPrefLang();
        learnLang = ((MyAccount) getApplication()).getLearnLang();

        button = findViewById(R.id.updateButton);
        passwordUpdate = findViewById(R.id.passwordUpdate);
        prefLangUpdate = findViewById(R.id.prefLangSpinner);
        learningLangUpdate = findViewById(R.id.learnLangSpinner);
        username = findViewById(R.id.usernameDisplay);

        username.setText(userName);
        passwordUpdate.setText(Password);

        setupBottomNavigationView();





        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        learningLangUpdate.setAdapter(targetAdapter);

        ArrayAdapter<String> nativeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Translator.getLanguages());
        prefLangUpdate.setAdapter(nativeAdapter);


        ArrayList lang = Translator.getLanguages();

        learningLangUpdate.setSelection(lang.indexOf(learnLang));

        prefLangUpdate.setSelection(lang.indexOf(nativeLang));


        int userNameController = userName.indexOf("@");
        System.out.println("username received: " + userName);
        ref = FirebaseDatabase.getInstance().getReference().child("Account");
        Map<String, Object> updates = new HashMap<String, Object>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int userId = ((MyAccount) getApplication()).getUserId();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Account");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            Account account = childSnapshot.getValue(Account.class);
                            if (account.id == userId) {
                                String key = childSnapshot.getKey();
                                account.username = username.getText().toString();
                                account.password = passwordUpdate.getText().toString();
                                account.prefLang = prefLangUpdate.getSelectedItem().toString();
                                account.learnLang = learningLangUpdate.getSelectedItem().toString();
                                ref.child(key).setValue(account);


                                Toast.makeText(AccountPage.this, "Your information has been updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



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



    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navBar);
        BottomNavigationHelper.enableNavigation(AccountPage.this, bottomNavigationViewEx);


        // BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
    }
}