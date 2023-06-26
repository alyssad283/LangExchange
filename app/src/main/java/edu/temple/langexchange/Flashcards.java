package edu.temple.langexchange;

import java.io.Serializable;

public class Flashcards implements Serializable {

    int id;
    String translatedWord;
    String originalWord;
    String definition;


    public Flashcards(){

    }

    public Flashcards(int id, String translatedWord, String originalWord, String definition){
        this.id = id;
        this.translatedWord = translatedWord;
        this.originalWord = originalWord;
        this.definition = definition;


    }

    @Override
    public String toString() {
        return "Flashcards{" +
                "id=" + id +
                ", translatedWord='" + translatedWord + '\'' +
                ", originalWord='" + originalWord + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
