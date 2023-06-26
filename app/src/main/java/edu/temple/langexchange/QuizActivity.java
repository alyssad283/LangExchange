package edu.temple.langexchange;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    private static String correctAnswer = "";
    private static String res = "";
    private static int indexAnswered = 0;

    Button button;
    ListView list;

    DatabaseReference ref;
    List<Flashcards> flashcardList;

    ArrayList<String> questions;
    ArrayList<String> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_quiz);
        getSupportActionBar().setTitle(R.string.quiz_title);
        list = findViewById(R.id.quizDisplay);

        // initialize variables
        flashcardList = new ArrayList<Flashcards>();
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();

        // retrieve intent
        Intent intent = getIntent();

        // retrieve passed in data
        int userId = intent.getIntExtra("userId", 0);

        // connect to the database
        ref = FirebaseDatabase.getInstance().getReference().child("Flashcards");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flashcardList.clear();

                for (DataSnapshot flashcardData : snapshot.getChildren()) {
                    Flashcards flashcard = flashcardData.getValue(Flashcards.class);

                    if (flashcard.id == userId) {
                        flashcardList.add(flashcard);
                    }
                }

                for (Flashcards card : flashcardList) {
                    questions.add(card.getTranslatedWord());
                    answers.add(card.getOriginalWord().toUpperCase());
                }

                QuizAdapter adapter = new QuizAdapter(QuizActivity.this, flashcardList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        if(getIntent().getSerializableExtra("flashcardArr") == null) {
//            //test = new Flashcards[5];
//            test.add(new Flashcards(1, "Hi", "Hola", "A Word You Use to Greet People in Spanish"));
//            test.add(new Flashcards(2, "One", "Uno", "Number One"));
//            test.add(new Flashcards(3, "Two", "Dos", "Number Two"));
//            test.add(new Flashcards(4, "Three", "Tres", "Number Three"));
//            test.add(new Flashcards(5, "Four", "Cuatro", "Number Four"));
//        }
//        else{
//            test = (ArrayList<Flashcards>) getIntent().getSerializableExtra("flashcardArr");
//        }

//        FlashcardAdapter adapter = new FlashcardAdapter(this, test);

        button = findViewById(R.id.quizDisplayBtn);


//        for(Flashcards card : test)
//        {
//            questions.add(card.definition);
//            correctAnswer.add(card.originalWord.toUpperCase());
//        }
//
//        System.out.println(correctAnswer);
//        QuizAdapter adapter = new QuizAdapter(this, questions);
//        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuizActivity.this, QuizTaking.class);
                correctAnswer = answers.get(position);
                indexAnswered = position;
                startActivityForResult(intent, 1);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            res = data.getStringExtra("QuizAnswer");
            if(res.equals(correctAnswer))
            {
                System.out.println("CORRECT");
                list.getChildAt(indexAnswered).setBackgroundColor(getResources().getColor(R.color.correct_color));

            }
            else{
                System.out.println("INCORRECT");
                list.getChildAt(indexAnswered).setBackgroundColor(getResources().getColor(R.color.incorrect_color));
            }
            System.out.println(res);
        }
    }
}
