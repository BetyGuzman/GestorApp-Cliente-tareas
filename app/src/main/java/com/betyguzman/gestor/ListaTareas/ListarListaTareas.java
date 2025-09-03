package com.betyguzman.gestor.ListaTareas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betyguzman.gestor.Adapters.AdapterTareas;
import com.betyguzman.gestor.Models.Tareas;
import com.betyguzman.gestor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarListaTareas extends AppCompatActivity {

    ImageView ivBack;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SearchView svBuscarTarea;
    RecyclerView rvTarea;
    RecyclerView.LayoutManager layoutManager;
    List<Tareas> listaTarea = new ArrayList<>();
    AdapterTareas adapterTarea;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_lista_tareas);

        ivBack = findViewById(R.id.ivBack);
        rvTarea = findViewById(R.id.rvTareas);
        svBuscarTarea = findViewById(R.id.svTareas);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tareas");

        layoutManager = new LinearLayoutManager(this);
        rvTarea.setLayoutManager(layoutManager);

        dialog = new Dialog(ListarListaTareas.this);

        ivBack.setOnClickListener(v -> finish());

        svBuscarTarea.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return false;
            }
        });


        listaTarea();
    }

    private void filtrar(String newText) {
        List<Tareas> listaTareasFiltrado = new ArrayList<>();
        for (Tareas tareas : listaTarea) {
            if (tareas.getTitulo() != null && tareas.getTitulo().toLowerCase().contains(newText.toLowerCase())) {
                listaTareasFiltrado.add(tareas);
            }
        }
        if (adapterTarea != null) adapterTarea.filtrar(listaTareasFiltrado);
    }

    private void listaTarea() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTarea.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.child("id").getValue(String.class);
                    String titulo = dataSnapshot.child("titulo").getValue(String.class);
                    String descripcion = dataSnapshot.child("descripcion").getValue(String.class);
                    String fecha_inicio = dataSnapshot.child("fecha_inicio").getValue(String.class);
                    String fecha_limite = dataSnapshot.child("fecha_limite").getValue(String.class);
                    String estatus = dataSnapshot.child("estatus").getValue(String.class);

                    if (titulo != null) {
                        // 🔹 Validamos si la fecha límite ya pasó
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // ajusta al formato que uses
                            Date fechaLimite = sdf.parse(fecha_limite);
                            Date hoy = new Date();

                            if (fechaLimite != null && hoy.after(fechaLimite)) {
                                if (!"No Completado".equals(estatus)) {
                                    // 🔹 Actualiza en Firebase
                                    databaseReference.child(id).child("estatus").setValue("No Completado");
                                    estatus = "No Completado"; // también lo reflejamos localmente
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Tareas tareas = new Tareas(id, titulo, descripcion, fecha_inicio, fecha_limite, estatus);
                        listaTarea.add(tareas);
                    }
                }


                adapterTarea = new AdapterTareas(listaTarea, ListarListaTareas.this);
                adapterTarea.setOnTextClickListener(tareas -> mostrarDialogoOpciones(tareas));
                rvTarea.setAdapter(adapterTarea);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void mostrarDialogoOpciones(Tareas tareas) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_opciones);

        CardView cvEditar = dialog.findViewById(R.id.cvEditar);
        CardView cvEliminar = dialog.findViewById(R.id.cvEliminar);

        cvEditar.setOnClickListener(v -> {
            dialog.dismiss();
            svBuscarTarea.setQuery("", false);
            Intent intent = new Intent(ListarListaTareas.this, EditarListaTareas.class);
            intent.putExtra("id", tareas.getId());
            intent.putExtra("titulo", tareas.getTitulo());
            intent.putExtra("descripcion", tareas.getDescripcion());
            intent.putExtra("fecha_inicio", tareas.getFecha_inicio());
            intent.putExtra("fecha_limite", tareas.getFecha_limite());
            intent.putExtra("estatus", tareas.getEstatus());
            startActivity(intent);
        });

        cvEliminar.setOnClickListener(v -> {
            dialog.dismiss();
            eliminarTarea(tareas.getId());
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void eliminarTarea(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListarListaTareas.this);
        builder.setTitle("Aviso de Aplicativo");
        builder.setMessage("¿Estás seguro de eliminar?");

        builder.setPositiveButton("Confirmar", (dialogInterface, which) -> {
            databaseReference.child(id).removeValue();
            Toast.makeText(ListarListaTareas.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }
}
