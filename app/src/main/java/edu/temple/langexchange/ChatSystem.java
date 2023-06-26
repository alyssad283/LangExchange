package edu.temple.langexchange;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class ChatSystem extends AppCompatActivity implements RoomListener {

    // replace this with a real channelID from Scaledrone dashboard
    private String channelID = "";
    private final String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String userName, targetLang, prefLang, receivedLang = "";
    private CheckBox autoTranslate;
    private ImageButton micButton;
   // private boolean isAudioMessage = false;
    private TextToSpeech tts;
    private SpeechRecognizer sr;
    private boolean isAutoTranslate=false;
    public static final Integer RecordAudioRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagingtabgui);

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        receivedLang = intent.getStringExtra("langSelected");

        sr = SpeechRecognizer.createSpeechRecognizer(ChatSystem.this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Translator.getAudioCode(receivedLang));
        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
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
                micButton.setBackground(getDrawable(R.drawable.baseline_mic_none_24));
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String audioMessage = data.get(0);
                sendVoiceMessage(audioMessage);
                editText.setHint("Write a message");


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        tts = new TextToSpeech(ChatSystem.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    System.out.println("Successful tts connection!");
                    switch (receivedLang){
                        case "ENGLISH":
                            tts.setLanguage(Locale.ENGLISH);
                            break;
                        case "SPANISH":
                            tts.setLanguage(new Locale("es"));
                            break;
                        case "FRENCH":
                            tts.setLanguage(Locale.FRENCH);
                            break;
                        default:
                            tts.setLanguage(Locale.GERMAN);
                            break;
                    }
                }
            }
        });
        System.out.println(receivedLang);
        channelID = intent.getStringExtra("channelID");
        int userNameController = userName.indexOf("@");
        System.out.println("username received: " + userName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Account");
        Query query = ref.orderByChild("username").equalTo(userName).limitToFirst(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    targetLang = childSnapshot.child("learnLang").getValue().toString().toUpperCase();
                    prefLang = childSnapshot.child("prefLang").getValue().toString();
                    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Translator.getAudioCode(prefLang.toUpperCase()));
                    System.out.println("target lang is: " + targetLang);
                    System.out.println("pref lang is: " + prefLang);
                }
                if(prefLang.toUpperCase().equals(receivedLang))
                {
                    userName = userName.substring(0, userNameController) + " - Native";
                }
                else
                {
                    userName = userName.substring(0, userNameController) + " - Learner";
                }

                if(channelID == "")
                {
                    Toast.makeText(ChatSystem.this, "Unable to Connect to Chat", Toast.LENGTH_LONG).show();
                    finish();
                }

                if(!isFinishing())
                {
                    editText = (EditText) findViewById(R.id.editText);
                    micButton = (ImageButton) findViewById(R.id.micButton);
                    messageAdapter = new MessageAdapter(ChatSystem.this);
                    messagesView = (ListView) findViewById(R.id.messages_view);
                    messagesView.setAdapter(messageAdapter);

                    autoTranslate = findViewById(R.id.autoTranslate);



                    autoTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                isAutoTranslate = true;
                                for(int i = 0; i < messagesView.getCount(); i++){
                                    View currentView = messagesView.getChildAt(i);
                                    TextView original = currentView.findViewById(R.id.message_body);
                                    TextView translation = currentView.findViewById(R.id.translation);
                                    if(translation.getText().toString().isEmpty()) {
                                        translation.setText(Translator.translate(original.getText().toString(), prefLang, ChatSystem.this));
                                    }
                                    original.setText(original.getText().toString() + "//autotranslate//");
                                    original.setVisibility(View.INVISIBLE);
                                    translation.setVisibility(View.VISIBLE);
                                }


                            } else {
                                for(int i = 0; i < messagesView.getCount(); i++){
                                    isAutoTranslate=false;
                                    View currentView = messagesView.getChildAt(i);
                                    TextView original = currentView.findViewById(R.id.message_body);
                                    String removeTag = original.getText().toString();
                                    removeTag.replace("//autotranslate//","");
                                    TextView translation = currentView.findViewById(R.id.translation);
                                    original.setVisibility(View.VISIBLE);
                                    translation.setVisibility(View.INVISIBLE);
                                }

                            }
                        }
                    });

                    //Speech-to-text to send audio messages

                    micButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                               sr.stopListening();
                                micButton.setBackground(getDrawable(R.drawable.baseline_mic_none_24));
                            }
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                                if(ContextCompat.checkSelfPermission(ChatSystem.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                                    checkPermission();
                                    return false;
                                }
                                micButton.setBackground(getDrawable(R.drawable.baseline_mic_24));
                                sr.startListening(speechRecognizerIntent);
                            }
                            return false;
                        }
                    });

                    MemberData data = new MemberData(userName, getRandomColor());

                    messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(view.findViewById(R.id.playButton).getVisibility() == View.VISIBLE){
                                ImageView playButton = (ImageView) view.findViewById(R.id.playButton);
                                playButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
                                TextView message = (TextView) view.findViewById(R.id.message_body);
                                String toSpeak = message.getText().toString().replace("//audio//","");
                                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                                playButton.setImageResource(R.drawable.baseline_play_circle_outline_24);
                            }
                        }
                    });
                            messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    //View myView = parent.getAdapter().getView(position,null, parent);
                                    TextView myTranslation = (TextView) view.findViewById(R.id.translation);
                                    TextView original = (TextView) view.findViewById(R.id.message_body);
                                    if (myTranslation.getText().toString().isEmpty()) {
                                        String translateView;
                                        if (view.findViewById(R.id.playButton).getVisibility() == View.VISIBLE) {
                                            String textToTranslate = original.getText().toString().replace("//audio//","");
                                            translateView = "\n\nTranslation: " + Translator.translate(textToTranslate, prefLang, ChatSystem.this);
                                        } else {
                                            translateView = original.getText().toString() + "\n\nTranslation: " + Translator.translate(original.getText().toString(), prefLang, ChatSystem.this);
                                        }
                                        myTranslation.setText(translateView);
                                    }
                                    if (myTranslation.getVisibility() == View.INVISIBLE) {
                                        myTranslation.setVisibility(View.VISIBLE);
                                        original.setVisibility(View.INVISIBLE);
                                    } else {
                                        myTranslation.setVisibility(View.INVISIBLE);
                                        if(view.findViewById(R.id.playButton).getVisibility() == View.INVISIBLE) {
                                            original.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    return true;
                                }
                            });


                    String welcomeString = "Welcome to " + receivedLang + " Channel";
                    System.out.println(welcomeString);
                    System.out.println("channelID received: " + channelID);
                    System.out.println("targetLang received: " + targetLang);
                    scaledrone = new Scaledrone(channelID, data);
                    Toast.makeText(ChatSystem.this, welcomeString, Toast.LENGTH_LONG).show();
                    scaledrone.connect(new Listener() {
                        @Override
                        public void onOpen() {
                            System.out.println("Scaledrone connection open");
                            scaledrone.subscribe(roomName, ChatSystem.this);
                        }

                        @Override
                        public void onOpenFailure(Exception ex) {
                            System.err.println(ex);
                        }

                        @Override
                        public void onFailure(Exception ex) {
                            System.err.println(ex);
                        }

                        @Override
                        public void onClosed(String reason) {
                            System.err.println(reason);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    public void sendVoiceMessage(String message){
        if(!message.isEmpty()){
            String audioMessage = message + "//audio//";
            scaledrone.publish(roomName, audioMessage);
          //  isAudioMessage = true;
        }
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
            final Message message = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);

                    messagesView.setSelection(messagesView.getCount() - 1);

                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }
    @Override
    protected void onDestroy() {
        sr.destroy();
        tts.shutdown();
        super.onDestroy();
    }
}