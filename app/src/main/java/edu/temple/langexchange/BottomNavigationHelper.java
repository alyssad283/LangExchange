package edu.temple.langexchange;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationHelper {


    public static final String TAG = "BottomNavigationHelper";

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_chat:
                        Intent intentChat = new Intent(context, ChatRoomChoice.class);
                        context.startActivity(intentChat);


                        break;
                    case R.id.ic_flashcard:
                        Intent intentFlashcard = new Intent(context, FlashcardActivity.class);
                        context.startActivity(intentFlashcard);
                        break;
                    case R.id.ic_translation:
                        Intent intentTranslate = new Intent(context, RealTimeTranslation.class);
                        context.startActivity(intentTranslate);
                        break;
                }

                return false;
            }
        });
    }

}
