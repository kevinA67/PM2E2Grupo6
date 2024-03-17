package com.example.pm2e2grupo6.Config;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm2e2grupo6.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Contactos> datos;
    private LayoutInflater inflater;
    private Context context;
    final ListAdapter.OnItemClickListener listener;

    public static int getSelectedItem() {
        return selectedItem;
    }

    public static int selectedItem = -1;

    public interface OnItemClickListener {
        void onItemClick(Contactos contactos);
    }

    public ListAdapter(List<Contactos> itemList, Context context, ListAdapter.OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = inflater.inflate(R.layout.disenio, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(datos.get(position));
        final int currentPosition = position;

        holder.itemView.setSelected(position == selectedItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar el índice del elemento seleccionado
                selectedItem = currentPosition;

                // Notificar al adaptador de los cambios
                notifyDataSetChanged();
            }
        });
    }

    public void setItems(List<Contactos> items) {
        datos = items;
    }

    public void setFilteredList(List<Contactos> filteredList) {
        this.datos = filteredList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nombre, telefono;
        LinearLayout item;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView2);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            telefono = (TextView) itemView.findViewById(R.id.telefono2);
            //item=(LinearLayout) itemView.findViewById(R.id.item);
        }

        void bindData(final Contactos contactos) {
            nombre.setText(contactos.getFull_name());
            telefono.setText(contactos.getTelefono());
            //limpiarSeleccion();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(contactos);
                }
            });
        }

//        public void limpiarSeleccion(){
//            item.setBackgroundColor(ContextCompat.getColor(context, R.color.gris));
//        }

    }
}
