package edu.temple.langexchange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class AccountDatabase extends SQLiteOpenHelper {

    public AccountDatabase(@Nullable Context context){
        super(context, "account.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    /*
    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String PREFLANG = "PREFLANG";
    public static final String ID = "ID";
    public static final String LEARNLANG = "LEARNLANG";
    public static final int VERSION = 1;

    public AccountDatabase(@Nullable Context context){
        super(context, "account.db", null, VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + " ("  + ID  +"INTEGER PRIMARY KEY AUTOINCREMENT REFERENCES FLASHCARD_DATABASE(ID), " + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD + " TEXT, " + PREFLANG + " TEXT, " + LEARNLANG + " TEXT)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(USERNAME, account.getUsername());
        content.put(PASSWORD, account.getPassword());
        content.put(PREFLANG, account.getPrefLang());
        content.put(LEARNLANG, account.getLearnLang());

        long insert = db.insert(ACCOUNT_TABLE, null, content);
        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public List<Account> getAll(){
        List<Account> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int accountID = cursor.getInt(0);
                String accountUsername = cursor.getString(1);
                String accountPassword = cursor.getString(2);
                String accountPrefLang = cursor.getString(3);
                String accountLearnLang = cursor.getString( 4);

                Account account= new Account(accountID, accountUsername, accountPassword, accountPrefLang, accountLearnLang);
                returnList.add(account);

            }while(cursor.moveToNext());
        }
        else{



        }

        cursor.close();
        db.close();


        return  returnList;
    }

     */
}