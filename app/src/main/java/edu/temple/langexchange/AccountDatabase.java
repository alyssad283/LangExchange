package edu.temple.langexchange;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import edu.temple.langexchange.data.model.LoggedInUser;

import static androidx.core.content.ContextCompat.startActivity;

public class AccountDatabase {

    DatabaseReference ref;

    long maxid;
    int currentUserId;

    public boolean addUser(Account account) {
        final boolean[] success = new boolean[1];
        ref = FirebaseDatabase.getInstance().getReference().child("Account");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //This adds the object account to the database
                ref.child(String.valueOf(maxid + 1)).setValue(account);
                success[0] = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                success[0] = false;
            }
        });
        return success[0];
    }

    public void findUser(String username, String password){
       /* final int[] userID = new int[1];
        userID[0] = -1;
        ref = FirebaseDatabase.getInstance().getReference().child("Account");
        Query query = ref.orderByChild("username").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("password").toString().equals(password)) {
                    userID[0] = Integer.parseInt(snapshot.child("id").getValue().toString());
                    startActivity(new Intent(LoginActivity.this, FlashcardActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }); */

    }
}