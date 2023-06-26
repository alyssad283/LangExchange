package edu.temple.langexchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class RealTimeTranslation extends AppCompatActivity {


    private Spinner learning;
    private  EditText beforeTranslate;
    private TextView afterTranslate;
    private ImageButton micButtonAT, camera;
    private  Button translateButton;
    private  String learningLangSelected;
    private SpeechRecognizer sr;
    public static final Integer RecordAudioRequestCode = 1;
    private TextToSpeech tts;
    String audioMessage;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_translation);


        learning = findViewById(R.id.learning);
        beforeTranslate = findViewById(R.id.beforeTranslate);
        afterTranslate = findViewById(R.id.afterTranslate);
        micButtonAT = findViewById(R.id.micButtonAT);
        camera = findViewById(R.id.camera);
        translateButton = findViewById(R.id.translate);

        sr = SpeechRecognizer.createSpeechRecognizer(RealTimeTranslation.this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
        learning.setAdapter(adapter);





        Intent intent = getIntent();
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Translator.getAudioCode(learning.getSelectedItem().toString()));
        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                beforeTranslate.setText("");
                beforeTranslate.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                micButtonAT.setBackground(getDrawable(R.drawable.baseline_mic_none_24));
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                audioMessage = data.get(0);
                beforeTranslate.setText(audioMessage);


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = beforeTranslate.getText().toString();
                learningLangSelected = learning.getSelectedItem().toString();
                String translatedText = Translator.translate(text, learningLangSelected, RealTimeTranslation.this);
                afterTranslate.setText(translatedText);
            }
        });

        micButtonAT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sr.stopListening();

                    micButtonAT.setBackground(getDrawable(R.drawable.baseline_mic_none_24));

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    if(ContextCompat.checkSelfPermission(RealTimeTranslation.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                        checkPermission();
                        return false;
                    }
                    micButtonAT.setBackground(getDrawable(R.drawable.baseline_mic_24));
                    sr.startListening(intent);

                }
                return false;
            }
        });




    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }



        sr.destroy();
        tts.shutdown();
    }
}