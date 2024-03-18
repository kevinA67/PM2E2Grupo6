package com.example.pm2e2grupo6;

import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointDeleteContact;
import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointGetContact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pm2e2grupo6.Config.Contactos;
import com.example.pm2e2grupo6.Config.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityLista extends AppCompatActivity implements ListAdapter.OnItemDoubleClickListener {

    private RequestQueue requestQueue;
    List<Contactos> listContactos;
    SearchView searchView;
    ListAdapter listAdapter;
    Button eliminar, actualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        requestQueue = Volley.newRequestQueue(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.clearFocus();
        eliminar = (Button) findViewById(R.id.btnEliminar);
        actualizar=(Button) findViewById(R.id.btnActualizar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListAdapter.getSelectedItem() != -1) {
                    // Mostrar un diálogo de confirmación de eliminación
                    alertaEliminar();
                } else {
                    // Mostrar un mensaje si no se ha seleccionado ningún contacto
                    Toast.makeText(ActivityLista.this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivityUpdate.class);

                // Obtener el contacto seleccionado
                int selectedItemIndex = ListAdapter.getSelectedItem();
                if (selectedItemIndex != -1) {
                    Contactos contactos = listContactos.get(selectedItemIndex);
                    intent.putExtra("full_name", contactos.getFull_name());
                    intent.putExtra("telefono", contactos.getTelefono());
                    intent.putExtra("latitud", contactos.getLatitud_gps());
                    intent.putExtra("longitud", contactos.getLongitud_gps());
                    intent.putExtra("video", contactos.getVideo());
                }
                startActivity(intent);
                finish();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        ObtenerDatos();
    }

    private void filter(String text) {
        List<Contactos> filteredList = new ArrayList<>();
        for (Contactos contactos : listContactos) {
            if (contactos.getFull_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(contactos);
            }
        }

        if (!filteredList.isEmpty()) {
            listAdapter.setFilteredList(filteredList);
        }
    }

    private void ObtenerDatos() {
        listContactos = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(EndpointGetContact, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Contactos contactos = new Contactos();
                            contactos.setId_contacto(obj.get("id_contacto").toString());
                            contactos.setFull_name(obj.get("full_name").toString());
                            contactos.setTelefono(obj.get("telefono").toString());
                            contactos.setLatitud_gps(obj.get("latitud_gps").toString());
                            contactos.setLongitud_gps(obj.get("longitud_gps").toString());
                            listContactos.add(new Contactos(contactos.getId_contacto(), contactos.getFull_name(), contactos.getTelefono(),
                                    contactos.getLatitud_gps(),contactos.getLongitud_gps()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    llenarLista();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onItemDoubleClick(Contactos contactos) {
        Toast.makeText(getApplicationContext(), "Hola  ", Toast.LENGTH_LONG).show();
    }
    private void llenarLista() {
//        personas=new ArrayList<>();
//        personas.add(new Personas("Kevin"));
//        personas.add(new Personas("Alexis"));
        listAdapter = new ListAdapter(listContactos, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contactos contactos) {
                Toast.makeText(getApplicationContext(), "Hola  ", Toast.LENGTH_LONG).show();
            }
        }, new ListAdapter.OnItemDoubleClickListener() {
            @Override
            public void onItemDoubleClick(Contactos contactos) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLista.this);
                    builder.setTitle("Desea ver la ubicación");
                    builder.setMessage("¿Desea ver la ubicación del contacto seleccionado?");

                    // Agregar botón de actualizar
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int selectedItemIndex = ListAdapter.getSelectedItem();
                            if (selectedItemIndex != -1) {
                                Contactos contactos = listContactos.get(selectedItemIndex);

                                Intent intent=new Intent(getApplicationContext(),ActivityMapa.class);
                                intent.putExtra("latitud", contactos.getLatitud_gps());
                                intent.putExtra("longitud", contactos.getLongitud_gps());
                                startActivity(intent);

                            }
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Si el usuario cancela la eliminación, no hacer nada
                        }
                    });

                    builder.show();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private void moveToDescription(Contactos contactos) {
        //Toast.makeText(getApplicationContext(),contactos.getFull_name(),Toast.LENGTH_LONG).show();
    }

    private void alertaEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLista.this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Desea eliminar los datos del contacto seleccionado?");

        // Agregar botón de actualizar
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el contacto seleccionado
                int selectedItemIndex = ListAdapter.getSelectedItem();
                if (selectedItemIndex != -1) {
                    Contactos contactos = listContactos.get(selectedItemIndex);


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id_contacto", contactos.getId_contacto());
                        //Toast.makeText(getApplicationContext(), "Contacto seleccionado:  " + contactos.getFull_name(), Toast.LENGTH_LONG).show();

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, EndpointDeleteContact, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String mensaje = response.getString("message");
                                            Toast.makeText(getApplicationContext(), "Contacto eliminado.", Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(ActivityLista.this, ActivityLista.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
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