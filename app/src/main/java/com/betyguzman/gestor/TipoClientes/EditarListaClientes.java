package com.betyguzman.gestor.TipoClientes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.betyguzman.gestor.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditarListaClientes extends AppCompatActivity {

    ImageView ivBack;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText etTipoClienteEdit;
    Button btnActualizarTipo;
    String id, nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lista_clientes);

        ivBack = findViewById(R.id.ivBack);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("TipoClientes");

        etTipoClienteEdit = findViewById(R.id.etTipoClienteEdit);
        btnActualizarTipo = findViewById(R.id.btnActualizarTipo);

        Bundle bundle=  getIntent().getExtras();
        if (bundle != null){
            id = bundle.getString("id");
            nombre = bundle.getString("tipo_cliente");

            etTipoClienteEdit.setText(nombre);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnActualizarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre= etTipoClienteEdit.getText().toString().trim();

                if (!nombre.isEmpty()){
                    editarTipoCliente();
                }
            }
        });
    }

    private void editarTipoCliente() {
        DatabaseReference data = databaseReference.child(firebaseUser.getUid()).child(id);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("id", id);
        updateMap.put("nombre", nombre);

        data.updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditarListaClientes.this, "Registro Actualizado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
}