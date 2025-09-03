package com.betyguzman.gestor.MisDatos;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.betyguzman.gestor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarMisDatos extends AppCompatActivity {

    ImageView ivBack, btnVerContrasenaDatosEdit;
    EditText etNombreDatosEdit, etApellidoDatosEdit, etCorreoDatosEdit, etContrasenaDatosEdit, etTelefonoDatosEdit;
    Button btnActualizarDatosUsuario;

    Boolean isVisibleDatosEdit;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mis_datos);

        ivBack = findViewById(R.id.ivBack);

        isVisibleDatosEdit = false;

        etNombreDatosEdit = findViewById(R.id.etNombreDatosEdit);
        etApellidoDatosEdit = findViewById(R.id.etApellidoDatosEdit);
        etCorreoDatosEdit = findViewById(R.id.etCorreoDatosEdit);
        etContrasenaDatosEdit = findViewById(R.id.etContrasenaDatosEdit);
        etTelefonoDatosEdit = findViewById(R.id.etTelefonoDatosEdit);
        btnActualizarDatosUsuario = findViewById(R.id.btnActualizarDatosUsuario);
        btnVerContrasenaDatosEdit = findViewById(R.id.btnVerContrasenaDatosEdit);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnVerContrasenaDatosEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisibleDatosEdit = !isVisibleDatosEdit;

                if (!TextUtils.isEmpty(etContrasenaDatosEdit.getText().toString())) {
                    if (isVisibleDatosEdit) {
                        etContrasenaDatosEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnVerContrasenaDatosEdit.setImageResource(R.drawable.contrasenamostrada);
                    } else {
                        etContrasenaDatosEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        btnVerContrasenaDatosEdit.setImageResource(R.drawable.contrasena);
                    }
                    etContrasenaDatosEdit.setSelection(etContrasenaDatosEdit.getText().length());
                }
            }
        });

        cargarDatos();


        btnActualizarDatosUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
                finish();
            }
        });
    }

    private void cargarDatos() {
        if(firebaseUser == null) return;

        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nombre = snapshot.child("nombre_usuario").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);
                            String correo = snapshot.child("correo").getValue(String.class);
                            String contrasena = snapshot.child("contrasena").getValue(String.class);
                            String telefono = snapshot.child("celular").getValue(String.class);

                            etNombreDatosEdit.setText(nombre);
                            etApellidoDatosEdit.setText(apellido);
                            etCorreoDatosEdit.setText(correo);
                            etContrasenaDatosEdit.setText(contrasena);
                            etTelefonoDatosEdit.setText(telefono);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditarMisDatos.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validarDatos() {
        String nombre = etNombreDatosEdit.getText().toString();
        String apellido = etApellidoDatosEdit.getText().toString();
        String correo = etCorreoDatosEdit.getText().toString();
        String contrasena = etContrasenaDatosEdit.getText().toString();
        String telefono = etTelefonoDatosEdit.getText().toString();

        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()){
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("nombre_usuario", nombre);
        updateMap.put("apellidos", apellido);
        updateMap.put("correo", correo);
        updateMap.put("contrasena", contrasena);
        updateMap.put("celular", telefono);

        databaseReference.child(firebaseUser.getUid())
                .updateChildren(updateMap)
                .addOnSuccessListener(unused ->
                        Toast.makeText(EditarMisDatos.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(EditarMisDatos.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show()
                );

    }
}
