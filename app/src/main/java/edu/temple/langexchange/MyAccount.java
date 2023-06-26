package edu.temple.langexchange;

import android.app.Application;

public class MyAccount extends Application {

    private static int userId;
    private static String username;
    private static String prefLang;
    private static String key;

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        MyAccount.key = key;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrefLang(){ return prefLang; }

    public void setPrefLang(String prefLang){ this.prefLang = prefLang; }

}
