package com.example.pm2e2grupo6;

import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointUpdateContact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pm2e2grupo6.Config.Contactos;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.List;

public class ActivityUpdate extends AppCompatActivity {
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RequestQueue requestQueue;
    List<Contactos> listContactos;
    EditText nombre, telefono, latitud, longitud;
    Button guardarCambios, tomarVideo;
    VideoView videoView;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Inicializar el proveedor de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        requestQueue = Volley.newRequestQueue(this);

        nombre = (EditText) findViewById(R.id.txtNombreUpdate);
        telefono = (EditText) findViewById(R.id.txtTelefonoUpdate);
        latitud = (EditText) findViewById(R.id.txtLatitudUpdate);
        longitud = (EditText) findViewById(R.id.txtLongitudUpdate);
        guardarCambios = (Button) findViewById(R.id.btnActualizarDatos);
        tomarVideo = (Button) findViewById(R.id.btnTomarVideo2);
        videoView=(VideoView) findViewById(R.id.videoViewUpdate);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("full_name");
        String telefono = intent.getStringExtra("telefono");
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");
        String video = intent.getStringExtra("video");

        //videoView.setVideoURI(Uri.parse("/videos/video.mp4"));
       // videoView.setVideoURI(Uri.parse("Internal storage/DCIM/Camera/VID_20240317_215515.mp4"));

        videoView.setVideoURI(Uri.parse(video));
        videoView.start();

        this.nombre.setText(nombre);
        this.telefono.setText(telefono);
        this.latitud.setText(latitud);
        this.longitud.setText(longitud);

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaActualizar();
            }
        });

        tomarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndDispatchTakeVideoIntent();
            }
        });
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

    private void requestLocation() {
        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido, obtener la ubicación
            obtainLocation();
        } else {
            // Solicitar permiso de ubicación
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void obtainLocation() {
        // Obtener la última ubicación conocida
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            // Ubicación obtenida con éxito
                            Location lastLocation = task.getResult();
                            double latitude = lastLocation.getLatitude();
                            double longitude = lastLocation.getLongitude();

                            String txtLat = String.valueOf(latitude);
                            String txtLon = String.valueOf(longitude);

                            latitud.setText(txtLat);
                            longitud.setText(txtLon);

                            // Mostrar la ubicación en el mapa
                            //showLocationOnMap(latitude, longitude);
                        } else {
                            Toast.makeText(getApplicationContext(), "No es posible obtener la ubicación",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
            requestLocation();
            Toast.makeText(this, "Video guardado con éxito en el dev", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "La grabación de video fue cancelada!", Toast.LENGTH_SHORT).show();
        }

    }

    private void alertaActualizar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUpdate.this);
        builder.setTitle("Confirmar actualización");
        builder.setMessage("¿Desea actualizar los datos del contacto seleccionado?");

        // Agregar botón de actualizar
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el contacto seleccionado
//                int selectedItemIndex = ListAdapter.getSelectedItem();
//                if (selectedItemIndex != -1) {
//                    Contactos contactos = listContactos.get(selectedItemIndex);


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_contacto", "13");
                    jsonObject.put("full_name", nombre.getText());
                    jsonObject.put("telefono", telefono.getText());
                    jsonObject.put("latitud_gps", latitud.getText());
                    jsonObject.put("longitud_gps", longitud.getText());

                    //Toast.makeText(getApplicationContext(), "Contacto seleccionado:  " + contactos.getFull_name(), Toast.LENGTH_LONG).show();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, EndpointUpdateContact, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String mensaje = response.getString("message");
                                        Toast.makeText(getApplicationContext(), "Contacto actualizado.", Toast.LENGTH_LONG).show();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    requestQueue.add(request);
                    Intent intent = new Intent(getApplicationContext(), ActivityLista.class);
                    startActivity(intent);
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
//            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si el usuario cancela la eliminación, no hacer nada
            }
        });

        builder.show();
    }
}