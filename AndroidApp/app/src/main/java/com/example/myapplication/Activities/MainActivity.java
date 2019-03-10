package com.example.myapplication.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Networking.ApiClient;
import com.example.myapplication.R;
import com.example.myapplication.Requests.GarbageDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    ImageView imageView;
    TextView textView;
    String text2="";
    String send_data="";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.image);
        textView=(TextView)findViewById(R.id.text);

        FirebaseApp.initializeApp(MainActivity.this);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent pictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                );
                if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pictureIntent,
                            REQUEST_CAPTURE_IMAGE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);

                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler();

                final StringBuffer stringBuffer=new StringBuffer("");
                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                int i=0;
                                for (FirebaseVisionImageLabel label: labels) {
                                    if(i==0) {
                                        send_data = label.getText();
                                        i=1;
                                    }
                                    stringBuffer.append("\n").append(new Gson().toJson(label));
                                    Log.e("label_data",new Gson().toJson(label));
                                }
                                text2=stringBuffer.toString();
                                textView.setText(text2);

                                final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                GarbageDetail apiService =
                                        ApiClient.getClient().create(GarbageDetail.class);

                                Call<com.example.myapplication.Responses.GarbageDetail> call = apiService.call(send_data,"Barco India");
                                call.enqueue(new Callback<com.example.myapplication.Responses.GarbageDetail>() {
                                    @Override
                                    public void onResponse(Call<com.example.myapplication.Responses.GarbageDetail>call, Response<com.example.myapplication.Responses.GarbageDetail> response) {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<com.example.myapplication.Responses.GarbageDetail>call, Throwable t) {
                                        // Log error here since request failed
                                        progressDialog.dismiss();
                                        Log.e("TAG", t.toString());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("failure",e.getMessage());
                            }
                        });

            }
        }
    }
}