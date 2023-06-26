package edu.temple.langexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ManualQuizAdapter extends ArrayAdapter<Flashcards> {
    private LayoutInflater mInflater;
    private ArrayList<Flashcards> flashcards;
    private int mViewResourceId;

    public ManualQuizAdapter(@NonNull Context context, int textViewResourceId, @NonNull ArrayList<Flashcards> flashcards) {
        super(context, textViewResourceId, flashcards);
        this.flashcards = flashcards;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public void Show_Manual_Quiz_Adapter(Context context, int textViewResourceId, ArrayList<Flashcards> flashcards)
    {
        this.flashcards = flashcards;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents)
    {
        convertView = mInflater.inflate(mViewResourceId, null);
        Flashcards flashcard = flashcards.get(position);
        if(flashcard != null)
        {
            TextView originalWord = (TextView) convertView.findViewById(R.id.showOriginalWord);
            TextView translatedWord = (TextView) convertView.findViewById(R.id.showTranslatedWord);
            TextView definition = (TextView) convertView.findViewById(R.id.showDefinition);

            if(originalWord != null)
            {
                originalWord.setText(flashcard.getOriginalWord());
            }
            if(translatedWord != null)
            {
                translatedWord.setText(flashcard.getTranslatedWord());
            }
            if(definition != null)
            {
                definition.setText(flashcard.getDefinition());
            }

        }
        return convertView;
    }
}
