package edu.temple.langexchange;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatRoom {
    public String channelId;
    public int usersNo;
    public String langChosen;

    public ChatRoom(String channelId, int usersNo, String langChosen)
    {
        this.channelId = channelId;
        this.usersNo = usersNo;
        this.langChosen = langChosen;
    }

    public ChatRoom()
    {

    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getUsersNo() {
        return usersNo;
    }

    public void setUsersNo(int usersNo) {
        this.usersNo = usersNo;
    }

    public String getLangChosen() {
        return langChosen;
    }

    public void setLangChosen(String langChosen) {
        this.langChosen = langChosen;
    }

    public void createRoom(ChatRoom chatroom)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(chatroom.getLangChosen());
        ref.setValue(chatroom);
    }

    public void deleteRoom(String lang)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(lang);
        ref.removeValue();
    }
}
