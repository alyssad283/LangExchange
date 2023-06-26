package edu.temple.langexchange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
;
import java.io.IOException;
import java.io.InputStream;


public class PhotoTextDetector {

    private static boolean connected;
    private static Task<Text> result;
    private static InputImage image;


    public static String detectText(Context context, TextView textView, int imageId) throws IOException {
        TextRecognizer recognizer = TextRecognition.getClient();
        Uri uri= Uri.parse("android.resource://"+ context.getPackageName() +"/" + imageId);
        try {
            image = InputImage.fromFilePath(context, uri);
        } catch (IOException e){
            e.printStackTrace();
        }

        result = recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                String resultText = result.getResult().getText();
                                textView.setText(resultText);
                                for (Text.TextBlock block : result.getResult().getTextBlocks()) {
                                    String blockText = block.getText();
                                    for (Text.Line line : block.getLines()) {
                                        String lineText = line.getText();
                                        System.out.println(lineText);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
        return result.getResult().getText();
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
