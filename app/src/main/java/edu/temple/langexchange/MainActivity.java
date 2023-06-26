package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button, buttonQuiz, buttonMakeQuiz;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        buttonQuiz = findViewById(R.id.button2);
        buttonMakeQuiz = findViewById((R.id.makeQuizBtn));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlashcardActivity.class);

                startActivity(intent);
            }
        });

        buttonQuiz.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, QuizActivity.class);
              startActivity(intent);
          }
        });

        buttonMakeQuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManualQuiz.class);
                startActivity(intent);
            }
        });
    }
}