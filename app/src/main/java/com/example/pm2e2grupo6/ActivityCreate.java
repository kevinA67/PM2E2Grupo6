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
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pm2e2grupo6.Config.Contactos;
import com.example.pm2e2grupo6.Config.RestApiMethods;

import org.json.JSONObject;

public class ActivityCreate extends AppCompatActivity {
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    Button contactos, salvarContacto, tomarVideo;
    EditText nombre, telefono, latitud, longitud;
    VideoView videoView;
    Uri videoUri;
    private RequestQueue requestQueue;
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
        tomarVideo=(Button) findViewById(R.id.btnTomarVideo);

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
        requestQueue = Volley.newRequestQueue(this);
        Contactos contactos=new Contactos();

        contactos.setFull_name(nombre.getText().toString());
        contactos.setTelefono(telefono.getText().toString());
        contactos.setLatitud_gps(latitud.getText().toString());
        contactos.setLongitud_gps(longitud.getText().toString());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("full_name", contactos.getFull_name());
            jsonObject.put("telefono", contactos.getTelefono());
            jsonObject.put("latitud_gps", contactos.getLatitud_gps());
            jsonObject.put("longitud_gps", contactos.getLongitud_gps());


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndpointPostContact,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String mensaje = response.getString("message");
                        Toast.makeText(getApplicationContext(), "Contacto salvado.", Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            requestQueue.add(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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