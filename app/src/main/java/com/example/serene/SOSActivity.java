package com.example.serene;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SOSActivity extends AppCompatActivity {

    private TextView txtLocation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("SOS Help");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button btnCallPolice = findViewById(R.id.btnCallPolice);
        Button btnCallHospital = findViewById(R.id.btnCallHospital);
        Button btnLocation = findViewById(R.id.btnLocation);
        txtLocation = findViewById(R.id.txtLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnCallPolice.setOnClickListener(v -> callNumber("999"));
        btnCallHospital.setOnClickListener(v -> callNumber("0195349988"));
        btnLocation.setOnClickListener(v -> getLocation());
    }

    private void callNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                String address = getAddressFromLocation(location);
                txtLocation.setText(String.format(Locale.getDefault(),
                        " %s\nLatitude: %.5f\nLongitude: %.5f",
                        address, location.getLatitude(), location.getLongitude()));
            } else {
                txtLocation.setText("Unable to retrieve location.");
            }
        });
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address addr = addresses.get(0);
                return addr.getThoroughfare() + ", " + addr.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown location";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
