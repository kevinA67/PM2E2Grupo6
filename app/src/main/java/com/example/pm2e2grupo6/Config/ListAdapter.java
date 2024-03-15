package com.example.pm2e2grupo6.Config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2e2grupo6.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Contactos> datos;
    private LayoutInflater inflater;
    private Context context;

    public ListAdapter(List<Contactos> itemList, Context context){
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        this.datos=itemList;
    }

    @Override
    public int getItemCount(){return datos.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        View view=inflater.inflate(R.layout.disenio,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(datos.get(position));
    }

    public void setItems(List<Contactos> items){datos=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nombre;
        ViewHolder(View itemView){
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.imageView2);
            nombre=(TextView) itemView.findViewById(R.id.nombre);
        }
        void bindData(final Contactos personas){
            //nombre.setText(personas.getNombres());
        }
    }
}
