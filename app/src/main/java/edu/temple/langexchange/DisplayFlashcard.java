package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayFlashcard extends AppCompatActivity {

    TextView original;
    TextView translation;
    Button exitActivity;
    Button showTranslation;
    boolean showingTranslation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_flashcard);
        getSupportActionBar().setTitle(R.string.flashcard_title);

        original = findViewById(R.id.flashcardName);
        translation = findViewById(R.id.flashcardTranslation);

        exitActivity = findViewById(R.id.exitActivity);
        showTranslation = findViewById(R.id.showTranslation);

        Intent intent = getIntent();

        original.setText(intent.getStringExtra("original"));
        translation.setText(intent.getStringExtra("translation"));
        translation.setVisibility(View.GONE);

        original.setTextSize(32);
        original.setGravity(Gravity.CENTER_HORIZONTAL);

        translation.setTextSize(22);
        translation.setGravity(Gravity.CENTER_HORIZONTAL);

        exitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingTranslation == false) {
                    translation.setVisibility(View.VISIBLE);
                    showTranslation.setText("Hide Translation");
                    showingTranslation = true;
                } else {
                    translation.setVisibility(View.GONE);
                    showTranslation.setText("Show Translation");
                    showingTranslation = false;
                }
            }
        });
    }
}
