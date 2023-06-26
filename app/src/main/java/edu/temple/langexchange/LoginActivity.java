package edu.temple.langexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.temple.langexchange.ui.login.LoginViewModel;
import edu.temple.langexchange.ui.login.LoginViewModelFactory;

import static androidx.core.content.ContextCompat.startActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView mTextView;
    private int loggedInUserId;

    private static int userId = 0;
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        mTextView = (TextView) findViewById(R.id.text);
            final EditText usernameEditText = findViewById(R.id.username);
            final EditText passwordEditText = findViewById(R.id.password);
            final Button loginButton = findViewById(R.id.login);
            Button registerButton = (Button) findViewById(R.id.register);

            registerButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    startActivityForResult(new Intent(LoginActivity.this, Register.class), 1);
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //AccountDatabase db = new AccountDatabase();
                   // final int[] userID = new int[1];
                   // userID[0] = -1;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Account");
                    Query query = ref.orderByChild("username").equalTo(usernameEditText.getText().toString()).limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           // if(snapshot.child("password").toString().equals(passwordEditText.getText().toString())) {
                               // userID[0] = Integer.parseInt(snapshot.child("id").getValue().toString());
                            if(snapshot.hasChildren() == false){
                                Toast.makeText(LoginActivity.this, "Incorrect Email", Toast.LENGTH_SHORT).show();
                                passwordEditText.setText("");
                                usernameEditText.setText("");
                                return;
                            }
                            for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                int place = 0;
                                Account account = childSnapshot.getValue(Account.class);

                                if(account.password.equals(passwordEditText.getText().toString())) {
                                    String userName = account.username;
                                    userId = account.getId();
                                    System.out.println("username sent: " + userName);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("username", userName);
                                    intent.putExtra("userID", userId);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    passwordEditText.setText("");
                                    usernameEditText.setText("");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        // Enables Always-on

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent intent = new Intent(LoginActivity.this, FlashcardActivity.class);
            userId = data.getIntExtra("newId", 0);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }
}