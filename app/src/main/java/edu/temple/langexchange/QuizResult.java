package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
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
    int selected = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_result);

        gradeText = findViewById(R.id.avgGradeText);
        totalText = findViewById(R.id.numberOfAttempt);
        listView = findViewById(R.id.wrongAnswerList);

        Intent intent = getIntent();
        ArrayList<String> shownList = (ArrayList<String>) intent.getSerializableExtra("questions");
        if(shownList.isEmpty())
        {
            shownList.add("NONE");
        }
        ArrayList<String> answers = (ArrayList<String>) intent.getSerializableExtra("answers");
        ArrayList<String> toCompare = (ArrayList<String>) intent.getSerializableExtra("stringCompare");
        int grade = intent.getIntExtra("grade", 0);
        int userId = intent.getIntExtra("userId", 0);
        int total = intent.getIntExtra("totalQuestions", 0);

        for(String i : answers)
        {
            System.out.println("answers ArrayList: " + i);
        }
        for(String i : toCompare)
        {
            System.out.println("toCompare ArrayList: " + i);
        }
        String resultOutOf = "Out of " + total + " questions";
        System.out.println("Grade received: " + grade);
        System.out.println("Out of received: " + resultOutOf);

        gradeText.setText("" + grade);
        totalText.setText(resultOutOf);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                shownList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(answers.get(position).equals(toCompare.get(position)))
                {
                    listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.correct_color));
                }
                else
                {
                    listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.incorrect_color));
                }
            }
        });

        QuizGrade userGrade = new QuizGrade(userId, grade);
        userGrade.postGrade(userGrade);

    }
}
