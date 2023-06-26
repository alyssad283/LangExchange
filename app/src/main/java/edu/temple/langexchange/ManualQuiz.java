package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManualQuiz extends AppCompatActivity {
    ArrayList<Flashcards> manualQuiz = new ArrayList<>();
    EditText originalWord, translatedWord, definition;
    Button addBtn, viewBtn;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_quiz_manual);
        originalWord = (EditText) findViewById(R.id.editOriginalWord);
        translatedWord = (EditText) findViewById(R.id.editTranslatedWord);
        definition = (EditText) findViewById(R.id.editDefinition);
        addBtn = (Button) findViewById(R.id.makeManualQuiz);
        viewBtn = (Button) findViewById(R.id.viewQuiz);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualQuiz.this, ViewListContents.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setOriginalWord = originalWord.getText().toString();
                String setTranslatedWord = translatedWord.getText().toString();
                String setDefinition = definition.getText().toString();

                if(setOriginalWord.length() != 0 && setDefinition.length() != 0 && setTranslatedWord.length() != 0)
                {
                    manualQuiz.add(new Flashcards(0, setOriginalWord, setTranslatedWord, setDefinition));
                    originalWord.setText("");
                    translatedWord.setText("");
                    definition.setText("");
                    //this is where you push the data to the database to be able to pull later on
                }
                else
                {
                    Toast.makeText(ManualQuiz.this, "Please fill out all the fields!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
