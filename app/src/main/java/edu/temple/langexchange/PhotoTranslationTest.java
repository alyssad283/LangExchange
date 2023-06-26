package edu.temple.langexchange;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.io.InputStream;

public class PhotoTranslationTest extends Activity {

        private static final int CAMERA_REQUEST = 1888;
        private static final int MY_CAMERA_PERMISSION_CODE = 100;
        private ImageView imageView;
        private TextView textView;
        private Bitmap photo;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_photo_translation_test);
            imageView = (ImageView)this.findViewById(R.id.photo);
            textView = (TextView)this.findViewById(R.id.detectedText);
            Button photoButton = (Button) this.findViewById(R.id.button);
            Button detectTextButton = (Button) this.findViewById(R.id.button2);
            photoButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        detectTextButton.setVisibility(View.VISIBLE);
                        detectTextButton.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                PhotoTextDetector.detectText(PhotoTranslationTest.this, textView, photo);
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == MY_CAMERA_PERMISSION_CODE)
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else
                {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                photo = (Bitmap) data.getExtras().get("data");

                imageView.setImageBitmap(photo);
            }
        }

    }