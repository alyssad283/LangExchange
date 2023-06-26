package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ChatRoomChoice extends AppCompatActivity {

    Spinner spin;
    Button submitBtn;
    ListView listView;
    ArrayList<String> availableRooms = new ArrayList<>();

    DatabaseReference ref;

    ArrayAdapter sAdapter;
    ArrayAdapter lAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_choice);

        System.out.println("New instance of ChatRoomChoice");

        setupBottomNavigationView();
        spin = findViewById(R.id.spinner);
        submitBtn = findViewById(R.id.selectLangBtn);
        listView = findViewById(R.id.availableRoom);

        sAdapter = new ArrayAdapter(ChatRoomChoice.this, android.R.layout.simple_spinner_item, Translator.getLanguages());
        spin.setAdapter(sAdapter);

        lAdapter = new ArrayAdapter(ChatRoomChoice.this, android.R.layout.simple_list_item_1, availableRooms);
        listView.setAdapter(lAdapter);

        ref = FirebaseDatabase.getInstance().getReference().child("ChatRoom");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableRooms.clear();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        availableRooms.add(childSnap.child("langChosen").getValue().toString());
                    }
                }

                if (!availableRooms.isEmpty()) {
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ChatRoomChoice.this, ChatSystem.class);
                            intent.putExtra("langSelected", availableRooms.get(position));
                            System.out.println("Available Room passed: " + availableRooms.get(position));
                            startActivity(intent);
                        }
                    });
                }

                lAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLang = spin.getSelectedItem().toString();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> usedLang = new ArrayList<>();
                        for (DataSnapshot childSnap : snapshot.getChildren()) {
                            if (snapshot.getChildren() != null) {
                                usedLang.add(childSnap.child("langChosen").getValue().toString());
                                System.out.println("used lang:" + usedLang);
                            }
                        }

                        if (!usedLang.contains(selectedLang)) {
                            Intent intent = new Intent(ChatRoomChoice.this, ChatSystem.class);
                            intent.putExtra("langSelected", selectedLang);
                            startActivity(intent);
                        } else if (usedLang.contains(selectedLang)) {
                            Toast.makeText(ChatRoomChoice.this, "Room already exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navBar);
        BottomNavigationHelper.enableNavigation(this, bottomNavigationViewEx);
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
                Intent intent = new Intent(ChatRoomChoice.this, edu.temple.langexchange.LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.ic_account_settings:{
                Intent intent = new Intent(ChatRoomChoice.this, AccountPage.class);
                startActivity(intent);
                break;
            }
        }
        return  super.onOptionsItemSelected(item);
    }
}
