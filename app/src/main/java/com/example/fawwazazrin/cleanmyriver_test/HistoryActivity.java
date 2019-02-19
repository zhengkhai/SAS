package com.example.fawwazazrin.cleanmyriver_test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {


    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        TextView textView = (TextView) findViewById(R.id.textView2);
        Bitmap p = ImageViewerActivity.getBitmap();
        imageView.setImageBitmap(p);

        address = ImageViewerActivity.getAddress();
        textView.setText(address);


    }
}
