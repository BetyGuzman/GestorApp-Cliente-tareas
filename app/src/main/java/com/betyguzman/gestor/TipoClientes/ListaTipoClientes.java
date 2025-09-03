package com.betyguzman.gestor.TipoClientes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betyguzman.gestor.Adapters.AdapterTipoCliente;
import com.betyguzman.gestor.Models.TipoClientes;
import com.betyguzman.gestor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListaTipoClientes extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ImageView ivBack;
    SearchView svBuscarTipoClientes;
    RecyclerView rvTipoClientes;
    RecyclerView.LayoutManager layoutManager;
    List<TipoClientes> listaTipoCliente = new ArrayList<>();
    AdapterTipoCliente adapterTipoCliente;
    Dialog dialog;
    FloatingActionButton btnAgregarTipoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tipo_clientes);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("TipoClientes");

        rvTipoClientes = findViewById(R.id.rvTipoClientes);

        ivBack =findViewById(R.id.ivBack);
        svBuscarTipoClientes = findViewById(R.id.svBuscarTipoClientes);
        btnAgregarTipoCliente = findViewById(R.id.btnAgregarTipoClientes);
        
        ivBack.setOnClickListener(v -> finish());


        svBuscarTipoClientes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return false;
            }
        });
        
        btnAgregarTipoCliente.setOnClickListener(
                v ->
//                        Toast.makeText(ListaTipoClientes.this, "", Toast.LENGTH_SHORT).show()
                        startActivity(new Intent(ListaTipoClientes.this, AgregarTipoClientes.class ))
        );

        layoutManager = new LinearLayoutManager(this);
        dialog = new Dialog(ListaTipoClientes.this);

        listaTipoCliente();

    }

    private void filtrar(String newText){
        List<TipoClientes> listaTipoClientesFiltrado = new ArrayList<>();
        for (TipoClientes tipoClientes: listaTipoCliente){
            if (tipoClientes.getNombre().toLowerCase().contains(newText.toLowerCase())){
                listaTipoClientesFiltrado.add(tipoClientes);
            }
        }

        adapterTipoCliente.filtrar(listaTipoClientesFiltrado);
    }

    private void listaTipoCliente() {
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    listaTipoCliente.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TipoClientes tipoClientes = dataSnapshot.getValue(TipoClientes.class);
                        listaTipoCliente.add(tipoClientes);
                    }

                    adapterTipoCliente = new AdapterTipoCliente(listaTipoCliente, ListaTipoClientes.this);

//                    adapterTipoCliente.setOnClickListener((View v) ->{ //                        Toast.makeText(ListaTipoClientes.this, "click", Toast.LENGTH_SHORT).show();
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog.setContentView(R.layout.dialogo_opciones);
//                        String id_tipo_cliente = listaTipoCliente.get(rvTipoClientes.getChildAdapterPosition(v)).getId();
//                        String tipo_cliente = listaTipoCliente.get(rvTipoClientes.getChildAdapterPosition(v)).getNombre();
//                        CardView cvEditar = dialog.findViewById(R.id.cvEditar);
//                        CardView cvEliminar = dialog.findViewById(R.id.cvEliminar);
//                        cvEditar.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                Toast.makeText(ListaTipoClientes.this, "Editar", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                                Intent intent =  new Intent(ListaTipoClientes.this, EditarListaClientes.class);
//                                intent.putExtra("id", id_tipo_cliente);
//                                intent.putExtra("tipo_cliente", tipo_cliente);
//                                startActivity(intent);
//                            }
//                        });
//                        cvEliminar.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                Toast.makeText(ListaTipoClientes.this, "eliminara", Toast.LENGTH_SHORT).show();
//                            eliminarTipoCliente(id_tipo_cliente);
//                            dialog.dismiss();
//                            }
//                        });
//                        dialog.show();
//                        dialog.setCanceledOnTouchOutside(true);                   });
//                    Actualzacion se uso el nuevo metodo para ejecutar el clic en el dialogo y mantener la posicion del elemento
                    adapterTipoCliente.setOnTextClickListener(new AdapterTipoCliente.onTextClickListener() {
                        @Override
                        public void onTextClick(TipoClientes position) {
                            //                        Toast.makeText(ListaTipoClientes.this, "click", Toast.LENGTH_SHORT).show();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.dialogo_opciones);

                            String id_tipo_cliente = position.getId();
                            String tipo_cliente = position.getNombre();

                            CardView cvEditar = dialog.findViewById(R.id.cvEditar);
                            CardView cvEliminar = dialog.findViewById(R.id.cvEliminar);

                            cvEditar.setOnClickListener(v -> {
//                                Toast.makeText(ListaTipoClientes.this, "Editar", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

//                                para limpiar el buscador luego de ouparlo
                                svBuscarTipoClientes.setQuery("", false);
                                Intent intent =  new Intent(ListaTipoClientes.this, EditarListaClientes.class);
                                intent.putExtra("id", id_tipo_cliente);
                                intent.putExtra("tipo_cliente", tipo_cliente);

                                startActivity(intent);
                            });

                            cvEliminar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                Toast.makeText(ListaTipoClientes.this, "eliminara", Toast.LENGTH_SHORT).show();
                                    eliminarTipoCliente(id_tipo_cliente);
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                            dialog.setCanceledOnTouchOutside(true);

                        }
                    });
                    rvTipoClientes.setAdapter(adapterTipoCliente);

                    rvTipoClientes.setLayoutManager(layoutManager);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void eliminarTipoCliente(String idTipoCliente) {

        AlertDialog.Builder builder= new AlertDialog.Builder(ListaTipoClientes.this);

        builder.setTitle("Aviso de Aplicativo");
        builder.setMessage("Estás seguro de eliminar?");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(firebaseUser.getUid()).child(idTipoCliente).removeValue();
                Toast.makeText(ListaTipoClientes.this, "Tipo de clinte eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

}