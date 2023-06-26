package edu.temple.langexchange;


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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class ChatSystem extends AppCompatActivity implements RoomListener {
    private String channelID = "";
    private final String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String userName, targetLang, prefLang, receivedLang;
    private CheckBox autoTranslate;
    private ImageButton micButton;
    private TextToSpeech tts;
    private SpeechRecognizer sr;
    private Intent speechRecognizerIntent;
    private boolean isAutoTranslate = false;
    public static final Integer RecordAudioRequestCode = 1;
    private String phrase;
    private ChatRoom chatRoom;
    String langCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagingtabgui);

        userName = ((MyAccount) getApplication()).getUsername();

        Intent intent = getIntent();
        receivedLang = intent.getStringExtra("langSelected");

        sr = SpeechRecognizer.createSpeechRecognizer(ChatSystem.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Translator.getAudioCode(receivedLang));
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Translator.getAudioCode(receivedLang));
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, Translator.getAudioCode(receivedLang));
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
                if (status != TextToSpeech.ERROR) {
                    System.out.println("Successful tts connection!");
                    langCode = Translator.languageCodes.get(receivedLang);
                    tts.setLanguage(new Locale(langCode));
                }
            }
        });
        System.out.println(receivedLang);
        final int[] updatedUser = {1};
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(receivedLang);
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    channelID = snapshot.child("channelId").getValue().toString();
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(receivedLang).child("usersNo");
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            updatedUser[0] = Integer.parseInt(snapshot.getValue().toString());
                            updatedUser[0] += 1;
                            System.out.println("usersNo from database: " + snapshot.getValue());
                            System.out.println("updated usersNo: " + updatedUser[0]);
                            ref1.setValue(updatedUser[0]);
                            ChatSystemFunction(channelID, speechRecognizerIntent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else if (!snapshot.hasChildren()) {
                    DatabaseReference channelRef = FirebaseDatabase.getInstance().getReference().child("Channels");
                    Query channelQuery = channelRef.orderByKey().limitToFirst(1);
                    channelQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(ChatSystem.this, "Maximum Number of Chatrooms reached", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    channelID = data.getValue().toString();
                                    data.getRef().removeValue();

                                }
                                ChatSystemFunction(channelID, speechRecognizerIntent);
                                chatRoom = new ChatRoom(channelID, updatedUser[0], receivedLang);
                                chatRoom.createRoom(chatRoom);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ChatSystemFunction(String channel, Intent intent) {
        int userNameController = userName.indexOf("@");
        System.out.println("username received: " + userName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Account");
        Query query = ref.orderByChild("username").equalTo(userName).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    targetLang = childSnapshot.child("learnLang").getValue().toString().toUpperCase();
                    prefLang = childSnapshot.child("prefLang").getValue().toString();
                    System.out.println("target lang is: " + targetLang);
                    System.out.println("pref lang is: " + prefLang);
                }
                String langController = receivedLang.toUpperCase();
                if (prefLang.toUpperCase().equals(langController)) {
                    userName = userName.substring(0, userNameController) + " - Native";
                } else {
                    userName = userName.substring(0, userNameController) + " - Learner";
                }

                if (channel == "") {
                    Toast.makeText(ChatSystem.this, "Unable to Connect to Chat", Toast.LENGTH_LONG).show();
                    finish();
                }

                if (!isFinishing()) {
                    editText = findViewById(R.id.editText);
                    micButton = findViewById(R.id.micButton);
                    messageAdapter = new MessageAdapter(ChatSystem.this);
                    messagesView = findViewById(R.id.messages_view);
                    messagesView.setAdapter(messageAdapter);

                    autoTranslate = findViewById(R.id.autoTranslate);


                    autoTranslate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                isAutoTranslate = true;
                                messageAdapter.getTranslated();
                            } else {
                                isAutoTranslate = false;
                                messageAdapter.getOriginal();
                            }
                        }
                    });

                    //Speech-to-text to send audio messages

                    micButton.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                sr.stopListening();
                                micButton.setBackground(getDrawable(R.drawable.baseline_mic_none_24));
                            }
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                if (ContextCompat.checkSelfPermission(ChatSystem.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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
                            if (view.findViewById(R.id.playButton).getVisibility() == View.VISIBLE) {
                                ImageView playButton = (ImageView) view.findViewById(R.id.playButton);
                                playButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
                                TextView message = (TextView) view.findViewById(R.id.message_body);
                                String toSpeak = message.getText().toString().replace("//audio//", "");
                                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                                playButton.setImageResource(R.drawable.baseline_play_circle_outline_24);
                            }
                        }
                    });
                  
                    messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView myTranslation = view.findViewById(R.id.translation);
                            TextView original = view.findViewById(R.id.message_body);
                          
                            Button flashcardMaker = view.findViewById(R.id.makeFlashcard);
                            Button hideFlashcardMaker = view.findViewById(R.id.hideFlashcardMaker);

                            if (myTranslation.getText().toString().isEmpty()) { // translation is empty
                                String translateView;
                                if (view.findViewById(R.id.playButton).getVisibility() == View.VISIBLE) { // input is audio
                                    String textToTranslate = original.getText().toString().replace("//audio//", "");
                                    phrase = textToTranslate;
                                    translateView = "\n\nTranslation: " + Translator.translate(textToTranslate, prefLang, ChatSystem.this);
                                } else {
                                    translateView = original.getText().toString() + "\n\nTranslation: " + Translator.translate(original.getText().toString(), prefLang, ChatSystem.this);
                                }
                                myTranslation.setText(translateView);
                            }

                            if (!myTranslation.getText().toString().isEmpty()) {
                                if (myTranslation.getVisibility() == View.VISIBLE) {
                                    myTranslation.setVisibility(View.GONE);
                                    flashcardMaker.setVisibility(View.GONE);
                                    hideFlashcardMaker.setVisibility(View.GONE);
                                } else {
                                    myTranslation.setVisibility(View.VISIBLE);
                                    flashcardMaker.setVisibility(View.VISIBLE);
                                    hideFlashcardMaker.setVisibility(View.VISIBLE);
                                    phrase = original.getText().toString();
                                }
                            }

                            flashcardMaker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChatSystem.this, CreateFlashcardFromChat.class);
                                    intent.putExtra("phrase", phrase);
                                    intent.putExtra("prefLang", prefLang);
                                    startActivity(intent);
                                }
                            });

                            hideFlashcardMaker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myTranslation.setVisibility(View.GONE);
                                    flashcardMaker.setVisibility(View.GONE);
                                    hideFlashcardMaker.setVisibility(View.GONE);
                                }
                            });

                            return false;
                        }
                    });

                    String welcomeString = "Welcome to " + receivedLang + " Channel";
                    System.out.println(welcomeString);
                    System.out.println("channelID received: " + channel);
                    System.out.println("targetLang received: " + targetLang);
                    scaledrone = new Scaledrone(channel, data);
                    Toast.makeText(ChatSystem.this, welcomeString, Toast.LENGTH_SHORT).show();
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

    public void sendVoiceMessage(String message) {
        if (!message.isEmpty()) {
            String audioMessage = message + "//audio//";
            scaledrone.publish(roomName, audioMessage);
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
                    messageAdapter.add(message, prefLang, isAutoTranslate);

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
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }
  
    @Override
    protected void onDestroy() {
        long[] updatedUser = {0};
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(receivedLang).child("usersNo");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    updatedUser[0] = (long) snapshot.getValue() - 1;
                    ref.setValue(updatedUser[0]);
                    if (updatedUser[0] <= 0) {
                        DatabaseReference channelRef = FirebaseDatabase.getInstance().getReference().child("Channels");
                        channelRef.push().setValue(channelID);
                        FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(receivedLang).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sr.destroy();
        tts.shutdown();
        super.onDestroy();
    }
}