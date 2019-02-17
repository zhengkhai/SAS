package com.example.fawwazazrin.cleanmyriver_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isUpperCase;

public class ImageViewerActivity extends AppCompatActivity {

    public int REQUEST_CODE = 20;
    public ImageView imageView;
    private TextView coordinates;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button uploadButton;
    private DatabaseReference mDatabase;
    private double loclong;
    private double loclad;
    private String manufacturer;
    private String model;
    private static String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        onLaunchCamera();
        coordinates = (TextView) findViewById(R.id.coordinates);
        uploadButton = (Button) findViewById(R.id.upload);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loclong = location.getLongitude();
                loclad = location.getLatitude();
                coordinates.append("\n " + loclad + " " + loclong);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10
                );
                return;
            }
            else{
                locationFind();
            }
        }


        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ImageViewerActivity.this, MainActivity.class);
                startActivity(intent);

            }

        });
    }

    public void onLaunchCamera() {

        //currentTime = System.currentTimeMillis();
        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());
        //String fileName = "MY_APP" + currentTime + ".jpg";

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = new File(Environment.getExternalStorageDirectory(), fileName);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dir));

        startActivityForResult(intent, REQUEST_CODE);

    }

    public void getPhoneModel() {

        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        Log.i(TAG, "Phone model: " + manufacturer + " " + model);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(photo);
                getPhoneModel();
                pushDatabase();
                
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationFind();
                }
                return;
        }
    }

    private void locationFind() {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            }

    public void pushDatabase() {
        Log.i(TAG, "Function working");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map locMap = new HashMap();
        locMap.put("loc_longtitude", loclong);
        locMap.put("loc_laditude", loclad);
        Map userMap = new HashMap();
        userMap.put("Name", "Kajang River");
        //userMap.put("RGB_red", 1.253);
        //userMap.put("RGB_blue", 0.253);
        //userMap.put("RGB_green", 3.2);
        userMap.put("Manufacturer", manufacturer);
        userMap.put("Model", model);
        userMap.putAll(locMap);
        String key = mDatabase.push().getKey(); // generate id for information uploaded
        mDatabase.push().setValue(userMap);
    }

    }

