package com.betyguzman.gestor.MisDatos;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.betyguzman.gestor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MisDatos extends AppCompatActivity {

    ImageView ivBack, btnVerContrasenaDatos;
    EditText etNombreDatos, etApellidoDatos, etCorreoDatos, etContrasenaDatos, etTelefonoDatos;
    TextView tvNombreApellidoDatos;
    Button btnEditarDatosUsuario;

    Boolean isVisibleDatos;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);

        ivBack = findViewById(R.id.ivBack);
        btnVerContrasenaDatos = findViewById(R.id.btnVerContrasenaDatos);
        etNombreDatos = findViewById(R.id.etNombreDatos);
        etApellidoDatos = findViewById(R.id.etApellidoDatos);
        etCorreoDatos = findViewById(R.id.etCorreoDatos);
        etContrasenaDatos = findViewById(R.id.etContrasenaDatos);
        etTelefonoDatos = findViewById(R.id.etTelefonoDatos);
        tvNombreApellidoDatos = findViewById(R.id.tvNombreApellidoDatos);
        btnEditarDatosUsuario = findViewById(R.id.btnEditarDatosUsuario);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        isVisibleDatos = false;

        ivBack.setOnClickListener(v -> finish());

        btnVerContrasenaDatos.setOnClickListener(v -> {
            isVisibleDatos = !isVisibleDatos;
            if (!TextUtils.isEmpty(etContrasenaDatos.getText().toString())) {
                if (isVisibleDatos) {
                    etContrasenaDatos.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnVerContrasenaDatos.setImageResource(R.drawable.contrasenamostrada);
                } else {
                    etContrasenaDatos.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnVerContrasenaDatos.setImageResource(R.drawable.contrasena);
                }
                etContrasenaDatos.setSelection(etContrasenaDatos.getText().length());
            }
        });

        btnEditarDatosUsuario.setOnClickListener(v ->
                startActivity(new Intent(MisDatos.this, EditarMisDatos.class))
        );

        cargarDatos();
    }

//    Para recargar la info de la bd de firebase, cada que se vuelva a la actividad anterior
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        if (firebaseUser == null) return;

        databaseReference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nombre = snapshot.child("nombre_usuario").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);
                            String correo = snapshot.child("correo").getValue(String.class);
                            String contrasena = snapshot.child("contrasena").getValue(String.class);
                            String telefono = snapshot.child("celular").getValue(String.class);

                            etNombreDatos.setText(nombre);
                            etApellidoDatos.setText(apellido);
                            etCorreoDatos.setText(correo);
                            etContrasenaDatos.setText(contrasena);
                            etTelefonoDatos.setText(telefono);

                            tvNombreApellidoDatos.setText(nombre + " " + apellido);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MisDatos.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
