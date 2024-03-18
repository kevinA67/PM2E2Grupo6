package com.example.pm2e2grupo6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.example.pm2e2grupo6.Config.RestApiMethods;

import org.json.JSONObject;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ActivityCreate extends AppCompatActivity implements OnMapReadyCallback {
    //gps
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    //gps
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    Button contactos, salvarContacto, tomarVideo;
    EditText nombre, telefono, latitud, longitud;
    VideoView videoView;
    Uri videoUri;
    String base64,direccion;
String video,video2;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        contactos=(Button) findViewById(R.id.btnContactos);
        salvarContacto=(Button) findViewById(R.id.btnSalvar);
        nombre=(EditText) findViewById(R.id.txtNombreUpdate);
        telefono=(EditText) findViewById(R.id.txtTelefonoUpdate);
        latitud=(EditText) findViewById(R.id.txtLatitudUpdate);
        longitud=(EditText) findViewById(R.id.txtLongitudUpdate);
        videoView=(VideoView) findViewById(R.id.videoView);
        tomarVideo=(Button) findViewById(R.id.btnTomarVideo2);
//gps
        // Inicializar el proveedor de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtener el fragmento del mapa
        //     SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //             .findFragmentById(R.id.mapFragment);
        //     mapFragment.getMapAsync(this);
//gps
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

    //gps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Puedes personalizar la configuración del mapa aquí si es necesario
        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.199999, -86.241905), 5));
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
                        } else
                        {
                            Toast.makeText(getApplicationContext(),"No es posible obtener la ubicación",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtainLocation(); //en caso de obtener permiso para ubicación
            } else {
                Toast.makeText(getApplicationContext(),"Permiso de ubicación denegado!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    //gps

    private void savedata() {
        requestQueue = Volley.newRequestQueue(this);
        Contactos contactos=new Contactos();

        contactos.setFull_name(nombre.getText().toString());
        contactos.setTelefono(telefono.getText().toString());
        contactos.setLatitud_gps(latitud.getText().toString());
        contactos.setLongitud_gps(longitud.getText().toString());
        contactos.setVideo(video);

       // String video = convertToBase64(getApplicationContext(), videoUri);
        String video2= guardarVideo(videoUri);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("full_name", contactos.getFull_name());
            jsonObject.put("telefono", contactos.getTelefono());
            jsonObject.put("latitud_gps", contactos.getLatitud_gps());
            jsonObject.put("longitud_gps", contactos.getLongitud_gps());
            jsonObject.put("video", video2);

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
            requestLocation();
            Toast.makeText(this, "Video guardado con éxito en el dev", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "La grabación de video fue cancelada!", Toast.LENGTH_SHORT).show();
        }

    }

    public static String convertToBase64(Context context, String videoUri) {
        FileInputStream video_obj = null;
        try{
            video_obj = new FileInputStream(videoUri);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        ByteArrayOutputStream video_byte = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try{
            for (int readNum; (readNum = video_obj.read(byteBufferString))!= -1;){
                video_byte.write(byteBufferString,0,readNum);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        String video_codificado;
        String video_encode = Base64.encodeToString(video_byte.toByteArray(),Base64.DEFAULT);
        Log.d("video_encode", video_encode);
        video_codificado=video_encode;
        return video_codificado = video_encode;
    }

    private String guardarVideo(Uri videoUri) {
        // Asegúrate de que el almacenamiento externo esté disponible para escritura
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Crea un directorio para guardar los videos si no existe
            File directory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "MyApp");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Crea un nombre de archivo único para el video
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String videoFileName = "VID_" + timeStamp + ".mp4";
            // Guarda el video en un archivo en el directorio creado
            File videoFile = new File(directory, videoFileName);
            try {
                InputStream inputStream = getContentResolver().openInputStream(videoUri);
                FileOutputStream outputStream = new FileOutputStream(videoFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                // Notifica al sistema que se ha agregado un nuevo video
                MediaScannerConnection.scanFile(this,
                        new String[]{videoFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                video = path; // Establecer la URI del video guardado
                                base64=convertToBase64(getApplicationContext(),path);
                                // Muestra un mensaje de éxito
                                // Toast.makeText(MainActivity.this, "Video guardado en " + path, Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
                // Muestra un mensaje de error si ocurre un problema al guardar el video
                Toast.makeText(this, "Error al guardar el video", Toast.LENGTH_LONG).show();
            }
        } else {
            // Muestra un mensaje si el almacenamiento externo no está disponible
            Toast.makeText(this, "El almacenamiento externo no está disponible", Toast.LENGTH_LONG).show();
        }
        return  video;
    }

}