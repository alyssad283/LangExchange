package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeFlashcard extends AppCompatActivity {

    EditText originalWordInput;
    EditText translatedWordInput;
    EditText definitionInput;

    Button addFlashcardBtn;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_flashcard);

        originalWordInput = findViewById(R.id.originalWordInput);
        translatedWordInput = findViewById(R.id.translatedWordInput);
        definitionInput = findViewById(R.id.definitionInput);

        addFlashcardBtn = findViewById(R.id.addFlashcardBtn);

        // connect to the db
        ref = FirebaseDatabase.getInstance().getReference().child("Flashcards");

        addFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = ((MyAccount) getApplication()).getUserId();

                String original = originalWordInput.getText().toString();
                String translation = translatedWordInput.getText().toString();
                String definition = definitionInput.getText().toString();

                // creates a new flashcard object with the new values
                Flashcards flashcard = new Flashcards(userId, translation, original, definition);

                // this places the flashcard onto the database
                ref.push().setValue(flashcard);

                // result code for previous activity set to RESULT_OK
                setResult(RESULT_OK);

                // finish the activity
                finish();
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
                Intent intent = new Intent(MakeFlashcard.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(MakeFlashcard.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }
}
