package edu.temple.langexchange;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static int grade = 0;

    Button button, submitBtn;
    ListView list;

    DatabaseReference ref;
    List<Flashcards> flashcardList;

    ArrayList<String> questions;
    ArrayList<String> answers;
    ArrayList<String> inputAnswers, toCompare;

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
        inputAnswers = new ArrayList<String>();
        toCompare = new ArrayList<String>();
        // retrieve passed in data
        int userId = ((MyAccount) getApplication()).getUserId();


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
                    questions.add(card.getOriginalWord());
                    answers.add(card.getTranslatedWord().toUpperCase());
                }

                QuizAdapter adapter = new QuizAdapter(QuizActivity.this, flashcardList);
                list.setAdapter(adapter);
                inputAnswers.addAll(answers);
                toCompare.addAll(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button = findViewById(R.id.quizDisplayBtn);
        submitBtn = findViewById(R.id.submitBtn);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuizActivity.this, QuizTaking.class);
                correctAnswer = answers.get(position);
                indexAnswered = position;
                getViewByPosition(position, list).setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                startActivityForResult(intent, 1);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < questions.size(); i++)
                {
                    questions.set(i, questions.get(i) + " - " + answers.get(i));
                    System.out.println(questions.get(i));
                }
                answers.removeAll(toCompare);
                grade = toCompare.size() - answers.size();
                Intent intent = new Intent(QuizActivity.this, QuizResult.class);
                intent.putExtra("grade", grade);
                intent.putExtra("questions", questions);
                intent.putExtra("stringCompare", toCompare);
                intent.putExtra("answers", inputAnswers);
                intent.putExtra("totalQuestions", questions.size());
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            res = data.getStringExtra("QuizAnswer");
            toCompare.set(indexAnswered, res);
        }
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
                Intent intent = new Intent(QuizActivity.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(QuizActivity.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
