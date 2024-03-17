package com.example.pm2e2grupo6;

import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointDeleteContact;
import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointGetContact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ActivityLista extends AppCompatActivity {

    private RequestQueue requestQueue;
    List<Contactos> listContactos;
    SearchView searchView;
    ListAdapter listAdapter;
    Button eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.clearFocus();
        eliminar = (Button) findViewById(R.id.btnEliminar);
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
                if (ListAdapter.selectedItem != -1) {
                    // Mostrar un diálogo de confirmación de eliminación
                    alertaEliminar();
                } else {
                    // Mostrar un mensaje si no se ha seleccionado ningún contacto
                    Toast.makeText(ActivityLista.this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
                }
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
                            contactos.setFull_name(obj.get("full_name").toString());
                            contactos.setTelefono(obj.get("telefono").toString());
                            listContactos.add(new Contactos(contactos.getFull_name(), contactos.getTelefono()));

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

    private void llenarLista() {
//        personas=new ArrayList<>();
//        personas.add(new Personas("Kevin"));
//        personas.add(new Personas("Alexis"));
        listAdapter = new ListAdapter(listContactos, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contactos contactos) {
                moveToDescription(contactos);
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
                // Si el usuario confirma la actualización, establecer confirmacion como true
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, EndpointDeleteContact + "/" + ListAdapter.selectedItem, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String mensaje = response.getString("message");
                                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
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

//                    Intent intent = new Intent(ActivityLista.this, ActivityLista.class);
//                    startActivity(intent);
//                    finish();
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