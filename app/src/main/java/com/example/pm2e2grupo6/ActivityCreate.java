package com.example.pm2e2grupo6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.VideoView;

public class ActivityCreate extends AppCompatActivity {


    Button contactos, salvarContacto;
    EditText nombre, telefono, latitud, longitud;
    VideoView videoView;
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


        contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ActivityLista.class);
                startActivity(intent);
            }
        });
    }
}