package edu.temple.langexchange;

import java.util.List;

public class Account {

    public int id;
    public String username;
    public String password;
    public String prefLang;
    public String learnLang;
    public List<Flashcards> flashcards;



    public Account(int id, String username, String password, String prefLang, String learnLang, List<Flashcards> flashcards) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.prefLang = prefLang;
        this.learnLang = learnLang;
        this.flashcards = flashcards;
    }

    public Account(int id, String username, String password, String prefLang, String learnLang) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.prefLang = prefLang;
        this.learnLang = learnLang;
    }

    public Account(int id, String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(List<Flashcards> flashcards) {
        this.flashcards = flashcards;
    }

    public Account(){

    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", prefLang='" + prefLang + '\'' +
                ", learnLang='" + learnLang + '\'' +
                ", flashcards=" + flashcards +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrefLang() {
        return prefLang;
    }

    public void setPrefLang(String prefLang) {
        this.prefLang = prefLang;
    }

    public String getLearnLang() {
        return learnLang;
    }

    public void setLearnLang(String learnLang) {
        this.learnLang = learnLang;
    }

    public List<Flashcards> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcards> flashcards) {
        this.flashcards = flashcards;
    }
}
