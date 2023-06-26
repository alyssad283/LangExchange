package edu.temple.langexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateFlashcardFromChat extends AppCompatActivity {

    DatabaseReference ref;
    ListView listView;
    String word, translate, prefLang, definiton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_from_chat);

        ref = FirebaseDatabase.getInstance().getReference().child("Flashcards");
        listView = findViewById(R.id.listView);

        int userID = ((MyAccount) getApplication()).getUserId();

        Intent intent = getIntent();
        String phrase = intent.getExtras().getString("phrase");
        String prefLang = intent.getExtras().getString("prefLang");



        ArrayList<String> arrayList = new ArrayList<>();
        String[] result = phrase.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        String firstWord = result[0];
       //arrayList.add(result[0]);
       // result = phrase.split("\\s+");




            for (int count = 0; count < result.length; count++) {
                arrayList.add(result[count]);
            }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word = (String) listView.getItemAtPosition(position);

                translate = Translator.translate(word, prefLang, CreateFlashcardFromChat.this);



                Flashcards flashcard = new Flashcards(userID, translate, word, phrase);

                // this places the flashcard onto the database
                ref.push().setValue(flashcard);

                // result code for previous activity set to RESULT_OK
                setResult(RESULT_OK);

                Toast.makeText(CreateFlashcardFromChat.this, "Added flashcard to database", Toast.LENGTH_SHORT).show();
                // finish the activity

                finish();
            }
        });
    }



}