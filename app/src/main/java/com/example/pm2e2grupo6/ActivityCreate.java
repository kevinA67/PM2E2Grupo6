package com.example.pm2e2grupo6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class ActivityCreate extends AppCompatActivity {
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    Button contactos, salvarContacto, tomarVideo;
    EditText nombre, telefono, latitud, longitud;
    VideoView videoView;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        contactos=(Button) findViewById(R.id.btnContactos);
        salvarContacto=(Button) findViewById(R.id.btnSalvar);
        nombre=(EditText) findViewById(R.id.txtNombre);
        telefono=(EditText) findViewById(R.id.txtTelefono);
        latitud=(EditText) findViewById(R.id.txtLatitud);
        longitud=(EditText) findViewById(R.id.txtLongitud);
        videoView=(VideoView) findViewById(R.id.videoView);
        tomarVideo=findViewById(R.id.btnTomarVideo);


        contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ActivityLista.class);
                startActivity(intent);
            }
        });
        tomarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndDispatchTakeVideoIntent();
            }
        });
        salvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });
    }

    private void savedata() {
    }

    private void checkPermissionsAndDispatchTakeVideoIntent() {
        //Obtiene los permisos para tomar y capturar.
        String[] permissions = {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (allPermissionsGranted) {
            dispatchTakeVideoIntent();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No es posible abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

            Toast.makeText(this, "Video guardado con éxito en el dev", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "La grabación de video fue cancelada!", Toast.LENGTH_SHORT).show();
        }

    }
}