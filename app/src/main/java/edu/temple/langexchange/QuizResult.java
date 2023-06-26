package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizResult extends AppCompatActivity {
    TextView gradeText, totalText;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_result);

        gradeText = findViewById(R.id.avgGradeText);
        totalText = findViewById(R.id.numberOfAttempt);
        listView = findViewById(R.id.wrongAnswerList);

        Intent intent = getIntent();
        ArrayList<String> wrongAnswers = (ArrayList<String>) intent.getSerializableExtra("wrongAnswers");
        if(wrongAnswers.isEmpty())
        {
            wrongAnswers.add("NONE");
        }
        int grade = intent.getIntExtra("grade", 0);
        int userId = intent.getIntExtra("userId", 0);
        int total = intent.getIntExtra("totalQuestions", 0);

        String resultOutOf = "Out of " + total + " questions";
        System.out.println("Grade received: " + grade);
        System.out.println("Out of received: " + resultOutOf);

        gradeText.setText("" + grade);
        totalText.setText(resultOutOf);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                wrongAnswers);
        listView.setAdapter(arrayAdapter);

        QuizGrade userGrade = new QuizGrade(userId, grade);
        userGrade.postGrade(userGrade);

    }
}
