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

import com.betyguzman.gestor.Models.Cliente;
import com.betyguzman.gestor.R;

import java.util.List;

//1 se hereda la clase recyclerView
public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.ViewHolder>{
//2 creacion de variables
    List<Cliente> listaClientes;
    Context context;
    onTextClickListener onTextClickListener;

//    3 creacio de construcctor


    public AdapterCliente(List<Cliente> listaClientes, Context context) {
        this.listaClientes = listaClientes;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCliente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        4 creacion dela bista del item cliente, preparacion delavista
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCliente.ViewHolder holder, int position) {
//  7 s prepara el onBindViewHolder
        Cliente cliente = listaClientes.get(position);

        holder.bind(cliente);
        holder.cvCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextClickListener != null){
                    onTextClickListener.onTextClick(cliente);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
//    8 creacion de la interfaz paara la captura de la lsta d regitros
        return listaClientes.size();
    }


//    6 creacion de interfaz y el setOnTextClickListener
    public interface onTextClickListener{
        void onTextClick(Cliente position);
    }

    public void setOnTextClickListener(onTextClickListener onTextClickListener){
        this.onTextClickListener = onTextClickListener;
    }

//    9 creacion del filtro de busqueda desdeel searchview
    @SuppressLint("NotifyDataSetChanged")
    public void filtrar(List<Cliente> listaClientesFiltrado){
        this.listaClientes = listaClientesFiltrado;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//       5 preparacion del view holder con el cardview del item
        CardView cvCliente;
        TextView tvNombre, tvTipoDoc, tvNumeroDoc, tvTipoCliente, tvTelefono;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvCliente = itemView.findViewById(R.id.cvCliente);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTipoDoc = itemView.findViewById(R.id.tvTipoDocumentoItem);
            tvNumeroDoc = itemView.findViewById(R.id.tvNumDocumentoItem);
            tvTipoCliente = itemView.findViewById(R.id.tvTipoClienteItem);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoItem);
        }

        void bind(Cliente cliente){
            tvNombre.setText(cliente.getNombre());
            tvTipoDoc.setText(cliente.getTipo_documento());
            tvNumeroDoc.setText(cliente.getDocumento());
            tvTipoCliente.setText(cliente.getTipo_cliente());
            tvTelefono.setText(cliente.getTelefono());

        }
    }
}
