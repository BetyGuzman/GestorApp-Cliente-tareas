package com.betyguzman.gestor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betyguzman.gestor.Models.Tareas;
import com.betyguzman.gestor.R;

import java.util.List;

public class AdapterTareas extends RecyclerView.Adapter<AdapterTareas.TareasViewHolder> {

    private List<Tareas> listaTareas;
    private Context context;
    private OnTextClickListener onTextClickListener;

    public AdapterTareas(List<Tareas> listaTareas, Context context) {
        this.listaTareas = listaTareas;
        this.context = context;
    }

    @NonNull
    @Override
    public TareasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tareas, parent, false);
        return new TareasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareasViewHolder holder, int position) {
        Tareas tareas = listaTareas.get(position);

        holder.tvTitulo.setText(tareas.getTitulo());
        holder.tvDescripcion.setText(tareas.getDescripcion());
        holder.tvFechaInicio.setText(tareas.getFecha_inicio());
        holder.tvFechaLimite.setText(tareas.getFecha_limite());
        holder.tvEstatus.setText(tareas.getEstatus());

        switch (tareas.getEstatus()) {
            case "Completado":
                holder.tvEstatus.setTextColor(context.getResources().getColor(R.color.completado));
                holder.tvFechaLimite.setTextColor(context.getResources().getColor(R.color.completado));
                holder.ivProgreso.setImageResource(R.drawable.completado);
                break;
            case "En Progreso":
                holder.tvEstatus.setTextColor(context.getResources().getColor(R.color.progreso));
                holder.tvFechaLimite.setTextColor(context.getResources().getColor(R.color.progreso));
                holder.ivProgreso.setImageResource(R.drawable.progreso);
                break;
            default:
                holder.tvEstatus.setTextColor(context.getResources().getColor(R.color.nocompletado));
                holder.tvFechaLimite.setTextColor(context.getResources().getColor(R.color.nocompletado));
                holder.ivProgreso.setImageResource(R.drawable.nocompletado);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (onTextClickListener != null) {
                onTextClickListener.onClick(tareas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void filtrar(List<Tareas> listaFiltrada) {
        this.listaTareas = listaFiltrada;
        notifyDataSetChanged();
    }

    public interface OnTextClickListener {
        void onClick(Tareas tareas);
    }

    public void setOnTextClickListener(OnTextClickListener listener) {
        this.onTextClickListener = listener;
    }

    static class TareasViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvFechaInicio, tvFechaLimite, tvEstatus;
        ImageView ivProgreso;

        public TareasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloTarea);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionItem);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicioItem);
            tvFechaLimite = itemView.findViewById(R.id.tvFechaLimiteItem);
            tvEstatus = itemView.findViewById(R.id.tvEstatusItem);
            ivProgreso = itemView.findViewById(R.id.ivProgresoTareaItem);
        }
    }
}
