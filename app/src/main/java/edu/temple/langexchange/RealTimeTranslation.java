package edu.temple.langexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Locale;

public class RealTimeTranslation extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Spinner originalLang;
    private Spinner translateLang;
    private  EditText beforeTranslate;
    private TextView afterTranslate;
    private ImageButton micButtonAT, camera;
    private  Button translateButton, detectTextButton;
    private  String learningLangSelected;
    private SpeechRecognizer sr;
    private ImageView imageView;
    public static final Integer RecordAudioRequestCode = 1;
    private TextToSpeech tts;
    String audioMessage;
    private Bitmap photo;


Toolbar toolbar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_translation);

        setupBottomNavigationView();

        originalLang = findViewById(R.id.originalLang);
        translateLang = findViewById(R.id.translateLang);
        beforeTranslate = findViewById(R.id.beforeTranslate);
        beforeTranslate.setMovementMethod(new ScrollingMovementMethod());
        afterTranslate = findViewById(R.id.afterTranslate);
        afterTranslate.setMovementMethod(new ScrollingMovementMethod());
        micButtonAT = findViewById(R.id.micButtonAT);
        camera = findViewById(R.id.camera);
        translateButton = findViewById(R.id.translate);
        imageView = findViewById(R.id.photo);
        detectTextButton = findViewById(R.id.detectText);
        detectTextButton.setVisibility(View.INVISIBLE);
        String prefLang = ((MyAccount) getApplication()).getPrefLang();

        sr = SpeechRecognizer.createSpeechRecognizer(RealTimeTranslation.this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Translator.getLanguages());
        originalLang.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Translator.getLanguages());
        translateLang.setAdapter(adapter2);




        Intent intent = getIntent();
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


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
                learningLangSelected = originalLang.getSelectedItem().toString();
                String translatedText = Translator.translate(text, translateLang.getSelectedItem().toString(), RealTimeTranslation.this);
                afterTranslate.setText(translatedText);
            }
        });

        micButtonAT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    sr.stopListening();
                    beforeTranslate.setHint("");
                    micButtonAT.setBackground(getDrawable(R.drawable.baseline_mic_none_24));

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    afterTranslate.setText("");
                    if(ContextCompat.checkSelfPermission(RealTimeTranslation.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                        checkPermission();
                        return false;
                    }
                    micButtonAT.setBackground(getDrawable(R.drawable.baseline_mic_24));
                    System.out.println(originalLang.getSelectedItem().toString());
                    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Translator.getAudioCode(originalLang.getSelectedItem().toString()));
                    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Translator.getAudioCode(originalLang.getSelectedItem().toString()));
                    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, Translator.getAudioCode(originalLang.getSelectedItem().toString()));

                    sr.startListening(speechRecognizerIntent);

                }
                return false;
            }
        });

camera.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        { 
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            detectTextButton.setVisibility(View.VISIBLE);

        }
    }

});

        detectTextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PhotoTextDetector.detectText(RealTimeTranslation.this, beforeTranslate, photo);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);
        }
    }





    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }


        sr.destroy();
        tts.shutdown();
    }




    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navBar);
        BottomNavigationHelper.enableNavigation(RealTimeTranslation.this, bottomNavigationViewEx);


        // BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
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
                Intent intent = new Intent(RealTimeTranslation.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(RealTimeTranslation.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }



}