package com.betyguzman.gestor.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.betyguzman.gestor.Models.TipoClientes;
import com.betyguzman.gestor.R;

import java.util.List;

//1. Se extiende de la clase RecyclerView  se implementan los métodos que trae, y muestrala clse ViewHolder con el constructor
public class AdapterTipoCliente extends RecyclerView.Adapter<AdapterTipoCliente.ViewHolder> {

//    6. creacion de variables para usar la lista
    List<TipoClientes> listaTipoClientes;
    Context context;
//    View.OnClickListener lister;
//    Se elimino el view.ocliclistener
//    aactualizacion1 se declara un nuevo onjto de la clase onTextClickListener
    onTextClickListener onTextClickListener;

//    7.Cotrcctores


    public AdapterTipoCliente(List<TipoClientes> listaTipoClientes,Context context) {
        this.listaTipoClientes = listaTipoClientes;
        this.context = context;
    }

    //2. Metodo on Create para idexar el item de la lista  a s vez cargarlo e el ViewHolder
    @NonNull
    @Override
    public AdapterTipoCliente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
//        8. Creacion de la vista (viewHolder) y se infla el item
//9 fals es para que no lo cargue de manera independiente, es decir, que lo cargu  traves del adaptador
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipo_cliente, parent, false );
//        Actualizacion 5 se elimina el clic d ela  vista
//        view.setOnClickListener(view1 -> {
//            lister.onClick(view1);
//        });

        return new ViewHolder(view);
    }

//3. Posicion del item de la lista
    @Override
    public void onBindViewHolder(@NonNull AdapterTipoCliente.ViewHolder holder, int position) {
// 10   Agregacion del bindholder de la listade la posisicion
//        Actualizacion 6 se modifica el onBindViewHolder
//        holder.bind(listaTipoClientes.get(position));
        TipoClientes tipoClientes = listaTipoClientes.get(position);
        holder.bind(tipoClientes);
        holder.cvTipoClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextClickListener != null){
                    onTextClickListener.onTextClick(tipoClientes);

                }
            }
        });
    }

//4. metodo que cuenta los registros o el tamamno de la lsta
    @Override
    public int getItemCount() {
// 11       Se trae la cantidad de registros de la lista
        return listaTipoClientes.size();
    }
//    actualizacion2 se crea la interface del onTextClickListener
    public interface onTextClickListener{
        void onTextClick(TipoClientes position);

    }
//    actualizacion3 creacion del construcctor de la clase onTextClickListener


    public void setOnTextClickListener(onTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    //    12. creacion del metodo de filtracion de registro
    @SuppressLint("NotifyDataSetChanged")
    public void filtrar(List<TipoClientes> listaTipoClientesFiltrado){
        this.listaTipoClientes = listaTipoClientesFiltrado;
        notifyDataSetChanged();
    }
//    ctualizacion4 se borra el paso 13
// 13 crecion del dialogo de acciones editar y eliminar
//    public void setOnClickListener(View.OnClickListener lister){
//        this.lister = lister;
//
//    }

//5. metdo  que usa la clase viewHolder que permite trabjar con controles adicionales, en este caso el Dialog
    public class ViewHolder extends RecyclerView.ViewHolder {
//        9. declaracion de controles que se usaran despues del clic y se crea el bind para setear los datos
        CardView cvTipoClientes;
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cvTipoClientes = itemView.findViewById(R.id.cvTipoCliente);
            tvItem = itemView.findViewById(R.id.tvItem);
        }

        void bind(TipoClientes tipoClientes){
            tvItem.setText(tipoClientes.getNombre());
        }
    }
}
