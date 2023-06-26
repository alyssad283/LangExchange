package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.temple.langexchange.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button button, buttonQuiz, buttonStartChat;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        buttonQuiz = findViewById(R.id.button2);
        buttonStartChat = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);

                startActivity(intent);
            }
        });

        buttonStartChat.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, ChatSystem.class);
              startActivity(intent);
          }
        });

    }
}