package com.betyguzman.gestor.Clientes;

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

import com.betyguzman.gestor.Models.Cliente;
import com.betyguzman.gestor.Models.TipoClientes;
import com.betyguzman.gestor.R;
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

public class EditarClientes extends AppCompatActivity {

    ImageView ivBack;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceTipoCliente, databaseReferenceCliente;
    Spinner spinnerTipoDocumento;
    EditText etNumeroDocEdit, etNombreEdit, etTelefonoEdit;
    AppCompatSpinner spTipoCliente;
    Button btnActualizarCliente;
    String id, id_tipo_cliente, tipo_cliente, tipo_documento, documento, nombre, telefono;
    List<TipoClientes> listaTipoClientes= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_clientes);

        ivBack = findViewById(R.id.ivBack);
        spinnerTipoDocumento = findViewById(R.id.spinnerTipoDoc);
        etNumeroDocEdit = findViewById(R.id.etNumeroDocEdit);
        etNombreEdit = findViewById(R.id.etNombreClienteEdit);
        etTelefonoEdit = findViewById(R.id.etTelefonoEdit);

        spTipoCliente = findViewById(R.id.spTipoCliente);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceTipoCliente = firebaseDatabase.getReference("TipoClientes");
        databaseReferenceCliente = firebaseDatabase.getReference("Clientes");

        btnActualizarCliente= findViewById(R.id.btnActualizarCliente);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("id");
            id_tipo_cliente = bundle.getString("id_tipo_cliente");
            nombre = bundle.getString("nombre");
            tipo_documento = bundle.getString("tipo_documento");
            documento = bundle.getString("documento");
            telefono = bundle.getString("telefono");

            etNumeroDocEdit.setText(documento);
            etNombreEdit.setText(nombre);
            etTelefonoEdit.setText(telefono);

        }

        String[] tipoDocumento = {"CURP","INE", "PASAPORTE"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoDocumento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapter);

//        Seleccion de el spinner d tipo documento
        int posicionTipoDoc = adapter.getPosition(tipo_documento);
        if (posicionTipoDoc != -1){
            spinnerTipoDocumento.setSelection(posicionTipoDoc);
        }

        spinnerTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String seleccionadoTipo= adapterView.getItemAtPosition(position).toString();
                tipo_documento = seleccionadoTipo;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getTipoCliente(id_tipo_cliente);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnActualizarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

    }

    private void validarDatos() {
        documento = etNumeroDocEdit.getText().toString().trim();
        nombre = etNombreEdit.getText().toString().trim();
        telefono = etTelefonoEdit.getText().toString().trim();

        if (TextUtils.isEmpty(documento)){
            Toast.makeText(this, "Ingrese el numro de documento", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(telefono)){
            Toast.makeText(this, "Ingrese el numero de telefono", Toast.LENGTH_SHORT).show();
        }else {
            actualizarCliente();
        }
    }

    private void actualizarCliente() {
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
                Toast.makeText(EditarClientes.this, "Cliente Actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarClientes.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTipoCliente(String idTipoCliente) {
        databaseReferenceTipoCliente.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listaTipoClientes.clear();

//                    xc
                    int positionSeleccionada= -1;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TipoClientes tipoClientes = dataSnapshot.getValue(TipoClientes.class);
                        listaTipoClientes.add(tipoClientes);
                    }

                    ArrayAdapter<TipoClientes> adapter = new ArrayAdapter<>(EditarClientes.this, android.R.layout.simple_spinner_item, listaTipoClientes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spTipoCliente.setAdapter(adapter);

                    for (int i= 0; i < listaTipoClientes.size(); i++){
                        if (listaTipoClientes.get(i).getId().equals(idTipoCliente)){
                            positionSeleccionada = i;
                            break;
                        }
                    }

                    if (positionSeleccionada != -1){
                        spTipoCliente.setSelection(positionSeleccionada);
                        id_tipo_cliente = listaTipoClientes.get(positionSeleccionada).getId();
                        tipo_cliente = listaTipoClientes.get(positionSeleccionada).getNombre();
                    }

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}