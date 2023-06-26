package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FlashcardActivity extends AppCompatActivity {

    TextView text;
    GridView gridView;

    Button makeFlashcardBtn;
    Button makeQuizBtn;

    ArrayList<Flashcards> flashcardArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().setTitle(R.string.flashcard_title);

        text = findViewById(R.id.flashcardText);
        gridView = findViewById(R.id.flashcardGrid);

        makeFlashcardBtn = findViewById(R.id.createFlashcardBtn);
        makeQuizBtn = findViewById(R.id.createQuizBtn);

        text.setText(R.string.flashcard_instructions);

        flashcardArr = new ArrayList();

        flashcardArr.add(new Flashcards(1, "Hello", "Hola", "Expression with which you greet"));
        flashcardArr.add(new Flashcards(2, "Hello", "Bonjour", "Expression with which you greet"));

        ArrayList<String> flashcards = new ArrayList();
        for (Flashcards card : flashcardArr) {
            flashcards.add(card.originalWord);
        }

        ArrayList<String> translation = new ArrayList();
        for (Flashcards card : flashcardArr) {
            translation.add(card.translatedWord);
        }

        FlashcardAdapter adapter = new FlashcardAdapter(this, flashcards);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlashcardActivity.this, DisplayFlashcard.class);
                intent.putExtra("original", flashcards.get(position));
                intent.putExtra("translation", translation.get(position));
                startActivity(intent);
            }
        });


        makeFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        makeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
