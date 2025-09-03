package com.betyguzman.gestor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.betyguzman.gestor.Clientes.ListaClientes;
import com.betyguzman.gestor.ListaTareas.AgregarListaTareas;
import com.betyguzman.gestor.ListaTareas.ListarListaTareas;
import com.betyguzman.gestor.MisDatos.MisDatos;
import com.betyguzman.gestor.Prioridades.ListarPrioridades;
import com.betyguzman.gestor.TipoClientes.ListaTipoClientes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    TextView tvNombresApellidos, tvCorreoUsuario, tvUid;
    CardView cvClientes, cvTipoClientes, cvTareas, cvListaTareas, cvPrioridades, cvMisDatos;
    Button btnCerrarSesion, btnDeveloper;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Dialog dialogDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        tvNombresApellidos = findViewById(R.id.tvNombresApellidos);
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario);
        tvUid = findViewById(R.id.tvUidUsuario);

        cvClientes = findViewById(R.id.cvClientes);
        cvTipoClientes = findViewById(R.id.cvTipoClientes);
        cvTareas = findViewById(R.id.cvTareas);
        cvListaTareas = findViewById(R.id.cvListaTareas);
        cvPrioridades = findViewById(R.id.cvPrioridades);
        cvMisDatos = findViewById(R.id.cvMisDatos);


        btnCerrarSesion=findViewById(R.id.btnCerrarSesion);
        btnDeveloper=findViewById(R.id.btnDeveloper);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        dialogDev=new Dialog(this);


        cvClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Clientes", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, ListaClientes.class));
            }
        });

        cvTipoClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Tipos Clientes", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, ListaTipoClientes.class));
            }
        });

        cvTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Tareas", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, AgregarListaTareas.class));
            }
        });

        cvListaTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Lista Tareas", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, ListarListaTareas.class));
            }
        });

        cvPrioridades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Lista Tareas", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, ListarPrioridades.class));
            }
        });

        cvMisDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Estas en el Módulo Lista Tareas", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, MisDatos.class));
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        btnDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developer();
            }
        });
    }

    @Override
    protected void onStart() {
        comprobarDatos();
        super.onStart();
    }

    private void cargarDatosUsuario(){
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombre_usuario").getValue();
                    String apellido = ""+snapshot.child("apellidos").getValue();
                    String correo= ""+snapshot.child("correo").getValue();

                    String nombreApellido=nombre+" " +apellido;
                    tvNombresApellidos.setText(nombreApellido);
                    tvCorreoUsuario.setText(correo);
                    tvUid.setText(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void comprobarDatos(){
        if (firebaseUser != null){
            cargarDatosUsuario();
        }else {
            startActivity(new Intent(Dashboard.this, MainActivity.class));
        }
    }

    private void developer(){
        ImageButton ibYoutube, ibWhatsApp;
        Button btnVolver;

        dialogDev.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDev.setContentView(R.layout.dialogo_developer);
        btnVolver= dialogDev.findViewById(R.id.btnVolver);
        ibWhatsApp = dialogDev.findViewById(R.id.ibWhatsApp);
        ibYoutube = dialogDev.findViewById(R.id.ibYoutube);


        ibWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = "9221607092";
                Uri uri= Uri.parse("tel: " +numero);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ibYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse("https://youtube.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDev.dismiss();
            }
        });

        dialogDev.show();
        dialogDev.setCanceledOnTouchOutside(false);
    }
    private void cerrarSesion(){
        firebaseAuth.signOut();
        startActivity(new Intent(Dashboard.this, MainActivity.class));
        Toast.makeText(this, "Hasta Luego...", Toast.LENGTH_SHORT).show();
        finish();
    }
}