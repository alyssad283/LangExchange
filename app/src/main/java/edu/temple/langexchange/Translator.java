package edu.temple.langexchange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Translator {
    private static String originalText;
    private static String translatedText;
    private static boolean connected;
    private static Translate translate;
    final static Map<String, String> languageCodes = new HashMap<String, String>(){{
        put("Arabic", "ar");
        put("Armenian", "hy");
        put("Azerbaijani", "az");
        put("Bengali", "bn");
        put("Chinese", "zh");
        put("Chinese(Simplified)", "zh");
        put("Chinese(Traditional)", "zh-TW");
        put("Danish", "da");
        put("English", "en");
        put("Finnish", "fi");
        put("French", "fr");
        put("German", "de");
        put("Gujarati", "gu");
        put("Hebrew", "he");
        put("Hindi", "hi");
        put("Hungarian", "hu");
        put("Italian", "it");
        put("Japanese", "ja");
        put("Korean", "ko");
        put("Malay", "ms");
        put("Pashto", "ps");
        put("Persian", "fa");
        put("Portuguese", "pt");
        put("Punjabi", "pa");
        put("Russian", "ru");
        put("Spanish", "es");
        put("Swahili", "sw");
        put("Swedish", "sv");
        put("Tagalog", "tl");
        put("Tamil", "ta");
        put("Thai", "th");
        put("Turkish", "tr");
        put("Urdu", "ur");
        put("Vietnamese", "vi");
        put("Xhosa", "xh");
        put("Yoruba", "yo");
        put("Zulu", "zu");


    }};

    final static Map<String, String> audioLanguageCodes = new HashMap<String, String>(){{
        put("ENGLISH", "en-US");
        put("FRENCH", "fr-FR");
        put("SPANISH", "es-US");
        put("GERMAN", "de-DE");
    }};

    public static ArrayList<String> getLanguages(){
        ArrayList<String>languages = new ArrayList<>();
        for(String key : languageCodes.keySet()){
            languages.add(key);

        }
        languages.remove("Chinese");
        Collections.sort(languages);
        return languages;
    }

    public static String getAudioCode(String language){
        if(!audioLanguageCodes.containsKey(language)){
            return "Audio messages not supported.";
        }
        return audioLanguageCodes.get(language);
    }

    public static String translate(String text, String prefLang, Context context) {
        if(!languageCodes.containsKey(prefLang)){
            return "Language not supported.";
        }
        if(!checkInternetConnection(context)){
            return "No internet connection. Cannot be translated.";
        }
        //Connect to API
        getTranslateService(context);
        //Get input text to be translated:
        originalText = text;
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage(languageCodes.get(prefLang)), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        return translatedText;

    }

    private static void getTranslateService(Context context) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = context.getResources().openRawResource(R.raw.translatecredentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public static boolean checkInternetConnection(Context context) {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        return connected;
    }


}
