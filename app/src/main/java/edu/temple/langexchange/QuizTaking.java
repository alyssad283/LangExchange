package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizTaking extends AppCompatActivity {

    EditText answer;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_taking);
        getSupportActionBar().setTitle(R.string.quiz_title);

        answer = findViewById(R.id.editAnswer);
        submit = findViewById(R.id.quizAnswerSubmitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myAnswer = answer.getText().toString().toUpperCase();

                // start a new intent to return data
                Intent intent = new Intent();

                // place my answer into the intent
                intent.putExtra("QuizAnswer", myAnswer);

                // send data back
                setResult(RESULT_OK, intent);

                // finish activity
                finish();
            }
        });

    }
}
