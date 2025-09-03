package com.betyguzman.gestor.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.betyguzman.gestor.Models.TipoClientes;
import com.betyguzman.gestor.R;
import com.betyguzman.gestor.TipoClientes.AgregarTipoClientes;
import com.betyguzman.gestor.TipoClientes.ListaTipoClientes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarCliente extends AppCompatActivity {
    ImageView ivBack;
    Spinner spinnerTipoDoc;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceTipoCliente, databaseReferenceCliente;
    EditText etDocumento, etNombre, etTelefono;
    String id, tipo_documento, documento, nombre, id_tipo_cliente, tipo_cliente, telefono;
    AppCompatSpinner spTipoCliente;
    List<TipoClientes> listaTipoCliente = new ArrayList<>();
    Button btnRegistrarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceTipoCliente = firebaseDatabase.getReference("TipoClientes");
        databaseReferenceCliente = firebaseDatabase.getReference("Clientes");

        id = databaseReferenceCliente.push().getKey();

        ivBack = findViewById(R.id.ivBack);
        etDocumento = findViewById(R.id.etNumeroDoc);
        etNombre = findViewById(R.id.etNombreCliente);
        etTelefono =findViewById(R.id.etTelefono);

        spinnerTipoDoc = findViewById(R.id.spinnerTipoDoc);
        spTipoCliente = findViewById(R.id.spTipoCliente);

        btnRegistrarCliente =findViewById(R.id.btnRegistrarCliente);

        String[] tipoDocumento = {"CURP","INE", "PASAPORTE"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tipoDocumento
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDoc.setAdapter(adapter);

// capturaos el spinner
        spinnerTipoDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String seleccionadoTipo = adapterView.getItemAtPosition(position).toString();
//               valor de la seccion
                tipo_documento = seleccionadoTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getClientes();


        btnRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });


    }

    private void validarDatos() {
        documento = etDocumento.getText().toString().trim();
        nombre = etNombre.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(documento)){
            Toast.makeText(this, "Ingrese el numero de documento", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "Ingrese un numero telefonico", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show();
            registrarCliente();
        }
    }

    private void registrarCliente() {
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("id_tipo_cliente", id_tipo_cliente);
        data.put("tipo_cliente", tipo_cliente);
        data.put("nombre", nombre);
        data.put("tipo_documento", tipo_documento);
        data.put("documento", documento);
        data.put("telefono", telefono);

        databaseReferenceCliente.child(firebaseUser.getUid()).child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AgregarCliente.this, "Cliente Registrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarCliente.this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getClientes() {
        databaseReferenceTipoCliente.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                se cargan los datos de la bd al spinner tipo lsta
                if (snapshot.exists()){
                    listaTipoCliente.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TipoClientes tipoClientes = dataSnapshot.getValue(TipoClientes.class);
                        listaTipoCliente.add(tipoClientes);

                    }
                }

                ArrayAdapter<TipoClientes> adapter  = new ArrayAdapter<>(AgregarCliente.this, android.R.layout.simple_spinner_item, listaTipoCliente);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTipoCliente.setAdapter(adapter);

//                Acciones de ejecucion posteriores a la seleccion de un elemento de la lista
                spTipoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        TipoClientes seleccionado = (TipoClientes) adapterView.getItemAtPosition(position);
                        id_tipo_cliente = seleccionado.getId();
                        tipo_cliente = seleccionado.getNombre();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}