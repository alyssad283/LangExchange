package edu.temple.langexchange;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends BaseAdapter {

    Context context;
    List<Flashcards> items;

    public QuizAdapter(Context context, List items) {
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
            flashcards.add(card.getOriginalWord());
        }

        TextView textView;

        if ((textView = (TextView) convertView) == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(22);
            textView.setPadding(8, 25, 0, 25);
        }

        textView.setText(flashcards.get(position));

        return textView;
    }
}
