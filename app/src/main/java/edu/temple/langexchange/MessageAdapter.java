package edu.temple.langexchange;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    List<Message> messages = new ArrayList<>();
    List<Message> original = new ArrayList<>();
    List<Message> translated = new ArrayList<>();
    Context context;
    boolean isAudioMessage;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message, String prefLang, boolean isAutoTranslate) {
        this.original.add(message);
        String toTranslate = message.getText();
        System.out.println(toTranslate);
        toTranslate.replace("// audio //","");
        this.translated.add(new Message(Translator.translate(toTranslate, prefLang, context), message.getMemberData(), message.isBelongsToCurrentUser()));

        if (isAutoTranslate ? this.messages.add(translated.get(getCount())) : this.messages.add(original.get(getCount())));

       // isAudioMessage = audioMessage;
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public void getTranslated() {
        for (int i = 0; i < getCount(); i++) {
            messages.set(i, translated.get(i));
        }
        notifyDataSetChanged();
    }

    public void getOriginal() {
        for (int i = 0; i < getCount(); i++) {
            messages.set(i, original.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
            holder.translation = (TextView) convertView.findViewById(R.id.translation);
            holder.playButton = convertView.findViewById(R.id.playButton);


        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.incoming_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.translation = (TextView) convertView.findViewById(R.id.translation);
            holder.playButton = convertView.findViewById(R.id.playButton);


            holder.name.setText(message.getMemberData().getName());
            holder.messageBody.setText(message.getText());
            GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
        }
        if(holder.messageBody.getText().toString().contains("//audio//")){
            holder.messageBody.setVisibility(View.INVISIBLE);
            holder.playButton.setVisibility(View.VISIBLE);
        }
        if(holder.messageBody.getText().toString().contains("//autotranslate//")){
            holder.messageBody.setVisibility(View.INVISIBLE);
            holder.translation.setVisibility(View.VISIBLE);
        }

        return convertView;
    }



}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
    public TextView translation;
    public ImageView playButton;
}