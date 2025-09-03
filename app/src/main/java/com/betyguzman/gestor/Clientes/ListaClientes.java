package com.betyguzman.gestor.Clientes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betyguzman.gestor.Adapters.AdapterCliente;
import com.betyguzman.gestor.Models.Cliente;
import com.betyguzman.gestor.R;
import com.betyguzman.gestor.TipoClientes.AgregarTipoClientes;
import com.betyguzman.gestor.TipoClientes.ListaTipoClientes;
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

public class ListaClientes extends AppCompatActivity {


    ImageView ivBack;
    FloatingActionButton btnAgregarCliente;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SearchView svBuscarCliente;
    RecyclerView rvClientes;
    RecyclerView.LayoutManager layoutManager;
    List<Cliente> listaCliente =new ArrayList<>();
    AdapterCliente adapterCliente;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        ivBack =findViewById(R.id.ivBack);
        
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("Clientes");

        rvClientes= findViewById(R.id.rvClientes);
        svBuscarCliente= findViewById(R.id.svClientes);

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);

        ivBack.setOnClickListener(v -> finish());

        svBuscarCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        btnAgregarCliente.setOnClickListener(
                v ->
//                        Toast.makeText(ListaTipoClientes.this, "", Toast.LENGTH_SHORT).show()
                        startActivity(new Intent(ListaClientes.this, AgregarCliente.class ))
        );

        layoutManager = new LinearLayoutManager(this);

        dialog = new Dialog(ListaClientes.this);
        
        listaCliente();

    }

//    creacion del metodo filtrar enlazado al filtrado del adapter
    private void filtrar(String newText){
        List<Cliente> listaClienteFiltrado =  new ArrayList<>();
        for (Cliente cliente : listaCliente){
            if (cliente.getNombre().toLowerCase().contains(newText.toLowerCase())){
                listaClienteFiltrado.add(cliente);
            }
        }
        adapterCliente.filtrar(listaClienteFiltrado);
    }

    private void listaCliente() {
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listaCliente.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Cliente cliente = dataSnapshot.getValue(Cliente.class);

                        listaCliente.add(cliente);
                    }

                    adapterCliente = new AdapterCliente(listaCliente, ListaClientes.this);
                    adapterCliente.setOnTextClickListener(new AdapterCliente.onTextClickListener() {
                        @Override
                        public void onTextClick(Cliente position) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.dialogo_opciones);

                            String id_cliente = position.getId();
                            String id_tipo_cliente = position.getId_tipo_cliente();
                            String nom_tipo_cliente = position.getTipo_cliente();
                            String nombre = position.getNombre();
                            String tipo_documento = position.getTipo_documento();
                            String documento = position.getDocumento();
                            String telefono = position.getTelefono();

                            CardView cvEditar = dialog.findViewById(R.id.cvEditar);
                            CardView cvEliminar = dialog.findViewById(R.id.cvEliminar);

                            cvEditar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(ListaClientes.this, EditarClientes.class);
                                    intent.putExtra("id",id_cliente);
                                    intent.putExtra("id_tipo_cliente",id_tipo_cliente);
                                    intent.putExtra("tipo_cliente",nom_tipo_cliente);
                                    intent.putExtra("nombre",nombre);
                                    intent.putExtra("tipo_documento",tipo_documento);
                                    intent.putExtra("documento",documento);
                                    intent.putExtra("telefono",telefono);
                                    startActivity(intent);
                                }
                            });

                            cvEliminar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    eliminarCliente(id_cliente);
                                }
                            });

                            dialog.show();
                            dialog.setCanceledOnTouchOutside(true);
                        }
                    });

                    rvClientes.setAdapter(adapterCliente);
                    rvClientes.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void eliminarCliente(String idCliente) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(ListaClientes.this);
        builder.setTitle("Aviso de aplicativo");
        builder.setMessage("Estás seguro de eliminar este registro?");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(firebaseUser.getUid()).child(idCliente).removeValue();

                Toast.makeText(ListaClientes.this, "Registro eliminado:)", Toast.LENGTH_SHORT).show();
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