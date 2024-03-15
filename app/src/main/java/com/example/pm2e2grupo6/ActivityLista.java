package com.example.pm2e2grupo6;

import static com.example.pm2e2grupo6.Config.RestApiMethods.EndpointGetContact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        requestQueue = Volley.newRequestQueue(this);

        ObtenerDatos();
    }

    private void ObtenerDatos() {
        listContactos=new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(EndpointGetContact, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int x = 0;

                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Contactos personas = new Contactos();
                            //personas.setNombres(obj.get("nombres").toString());
                            //listContactos.add(new Contactos(personas.getNombres()));

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
                int x = 0;
            }
        });

        requestQueue.add(request);
    }
    private void llenarLista() {
//        personas=new ArrayList<>();
//        personas.add(new Personas("Kevin"));
//        personas.add(new Personas("Alexis"));

        ListAdapter listAdapter=new ListAdapter(listContactos, this);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}