package com.example.pm2e2grupo6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMapa extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtener el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        latitud = Double.parseDouble(intent.getStringExtra("latitud"));
        longitud = Double.parseDouble(intent.getStringExtra("longitud"));

        Toast.makeText(this,"latitud: "+latitud+"\nlongitud: "+longitud,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Puedes personalizar la configuración del mapa aquí si es necesario
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 5));
        showLocationOnMap(latitud,longitud);
    }


    private void showLocationOnMap(double latitude, double longitude) {
        if (googleMap != null) {
            // Crear un objeto LatLng con las coordenadas
            LatLng location = new LatLng(latitude, longitude);

            // Mover la cámara a la ubicación especificada
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            // Puedes agregar un marcador si lo deseas
            googleMap.addMarker(new MarkerOptions().position(location).title("Mi ubicación"));

            // Opcional: Zoom en la ubicación
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
        }
    }
}