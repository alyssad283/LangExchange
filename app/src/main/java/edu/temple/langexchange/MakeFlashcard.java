package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                Intent intent = getIntent();
                int userId = intent.getIntExtra("userId", 0);

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
}
