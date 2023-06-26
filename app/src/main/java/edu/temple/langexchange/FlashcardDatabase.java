package edu.temple.langexchange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDatabase extends SQLiteOpenHelper {


    public static final int VERSION = 1;
    public static final String FLASHCARD_TABLE = "FLASHCARD_TABLE";
    public static final String ID = "ID";
    public static final String ORIGINAL_WORD = "ORIGINAL_WORD";
    public static final String TRANSLATED_WORD = "TRANSLATED_WORD";
    public static final String DEFINITION = "DEFINITION";


    public FlashcardDatabase(@Nullable Context context) {
        super(context, "account.db", null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + FLASHCARD_TABLE + " (" + ID + " PRIMARY KEY AUTOINCREMENT, " + ORIGINAL_WORD + " TEXT, " + TRANSLATED_WORD + " TEXT, " + DEFINITION + " TEXT)";

        db.execSQL(CREATE_ACCOUNTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addFlashcard(Flashcards flashcard){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, flashcard.getId());
        values.put(ORIGINAL_WORD, flashcard.getOriginalWord());
        values.put(TRANSLATED_WORD, flashcard.getTranslatedWord());
        values.put(DEFINITION, flashcard.getDefinition());

        long insert = db.insert(FLASHCARD_TABLE, null, values);
        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }




    public List<Flashcards> getAll(){
        List<Flashcards> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + FLASHCARD_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int flashcardID = cursor.getInt(0);
                String flashcardOriginalWord = cursor.getString(1);
                String flashcardTranslatedWord = cursor.getString(2);
                String flashcardDefinition = cursor.getString(3);

                Flashcards flashcard= new Flashcards(flashcardID, flashcardOriginalWord, flashcardTranslatedWord, flashcardDefinition);
                returnList.add(flashcard);

            }while(cursor.moveToNext());
        }
        else{



        }

        cursor.close();
        db.close();


        return  returnList;
    }
}
