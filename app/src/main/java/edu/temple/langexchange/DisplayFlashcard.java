package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class DisplayFlashcard extends AppCompatActivity {

    TextView original;
    TextView translation;
    TextView definition;

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
        definition = findViewById(R.id.flashcardDefinition);

        exitActivity = findViewById(R.id.exitActivity);
        showTranslation = findViewById(R.id.showTranslation);

        Intent intent = getIntent();

        original.setText(intent.getStringExtra("original"));

        translation.setText(intent.getStringExtra("translation"));
        translation.setVisibility(View.GONE);

       definition.setText(intent.getStringExtra("definition"));

        original.setTextSize(40);
        original.setGravity(Gravity.CENTER_HORIZONTAL);

        definition.setTextSize(28);
        definition.setGravity(Gravity.CENTER_HORIZONTAL);

        translation.setTextSize(40);
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

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navBar);
        BottomNavigationHelper.enableNavigation(DisplayFlashcard.this, bottomNavigationViewEx);

        // BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
    }
}
