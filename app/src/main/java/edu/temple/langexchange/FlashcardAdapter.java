package edu.temple.langexchange;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlashcardAdapter extends BaseAdapter {

    Context context;
    List<Flashcards> items;

    public FlashcardAdapter(Context context, List items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArrayList<String> flashcards = new ArrayList<>();
        for (Flashcards card : items) {
            flashcards.add(card.originalWord);
        }

        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if ((view = (View) convertView) == null) {
            view = inflater.inflate(R.layout.grid_layout, null);

            TextView text = view.findViewById(R.id.gridText);
            ImageView image = view.findViewById(R.id.gridImage);

            text.setText(flashcards.get(position));
            image.setImageResource(R.drawable.flashcard_bg);

            text.setGravity(Gravity.CENTER);
            text.setTextSize(18);

            image.setPadding(5, 0, 5, 0);
        }

        return view;
    }
}
