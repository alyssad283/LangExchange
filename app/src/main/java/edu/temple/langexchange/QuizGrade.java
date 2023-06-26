package edu.temple.langexchange;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class QuizGrade {
    public int usersId;
    public int grade;

    public QuizGrade(int usersId, int grade)
    {
        this.usersId = usersId;
        this.grade = grade;
    }

    public QuizGrade(){

    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void postGrade(QuizGrade userGrade)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Grade");
        ref.push().setValue(userGrade);
    }
}
