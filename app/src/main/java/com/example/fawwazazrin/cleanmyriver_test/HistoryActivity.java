package com.example.fawwazazrin.cleanmyriver_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HistoryActivity extends AppCompatActivity {

    private static String TAG = "HistoryActivity";
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        TextView textView = (TextView) findViewById(R.id.textView2);

        String filepath = ImageViewerActivity.getFile();
        Bitmap p = BitmapFactory.decodeFile(filepath);

        imageView.setImageBitmap(p);
        address = ImageViewerActivity.getAddress();
        textView.setText(address);


    }
}
