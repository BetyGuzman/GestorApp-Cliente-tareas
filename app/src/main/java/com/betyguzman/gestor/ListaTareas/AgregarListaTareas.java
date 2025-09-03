package com.betyguzman.gestor.ListaTareas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.betyguzman.gestor.Dashboard;
import com.betyguzman.gestor.MisDatos.MisDatos;
import com.betyguzman.gestor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AgregarListaTareas extends AppCompatActivity {

    ImageView ivBack;
    Button btnRegistrarDatosTarea;
    EditText etTituloTarea,etDescripcionTarea, etFechaLimiteTarea;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String id, titulo, descripcion, fecha_limite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_lista_tareas);

        ivBack =  findViewById(R.id.ivBack);

        etTituloTarea =findViewById(R.id.etRegistroTituloTarea);
        etDescripcionTarea =findViewById(R.id.etRegistroDescripcion);
        etFechaLimiteTarea =findViewById(R.id.etRegistroFechaLimiteTarea);
        btnRegistrarDatosTarea = findViewById(R.id.btnRegistrarDatosTarea);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tareas");

        id = databaseReference.push().getKey();


        btnRegistrarDatosTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo = etTituloTarea.getText().toString().trim();
                descripcion = etDescripcionTarea.getText().toString().trim();
                fecha_limite = etFechaLimiteTarea.getText().toString().trim();

                if (titulo.isEmpty()){
                    Toast.makeText(AgregarListaTareas.this, "Agrega el titulo dela tarea", Toast.LENGTH_SHORT).show();
                } else if (descripcion.isEmpty()) {
                    Toast.makeText(AgregarListaTareas.this, "Agrega la descripcion", Toast.LENGTH_SHORT).show();
                } else if (fecha_limite.isEmpty()) {
                    Toast.makeText(AgregarListaTareas.this, "Agrega la fecha limite de la tarea", Toast.LENGTH_SHORT).show();
                }else {
                    agregarTarea();
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etFechaLimiteTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AgregarListaTareas.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
                                String fechaSeleccionada = dateFormat.format(selectedDate.getTime());

                                etFechaLimiteTarea.setText(fechaSeleccionada);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });

    }

    private void agregarTarea() {

        String fecha_inicio = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());

        String estatus = "En Progreso";

//                String id, titulo, descripcion, fecha_inicio, fecha_limite, status;
        Map<String, String> data= new HashMap<>();
        data.put("id", id);
        data.put("titulo", titulo);
        data.put("descripcion", descripcion);
        data.put("fecha_inicio", fecha_inicio);
        data.put("fecha_limite", fecha_limite);
        data.put("estatus", estatus);

//        databaseReference.child(firebaseUser.getUid()).child(id).setValue(data)
        databaseReference.child(id).setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AgregarListaTareas.this, "Registrado", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(AgregarListaTareas.this, ListarListaTareas.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarListaTareas.this, "Ocurrio un problema", Toast.LENGTH_SHORT).show();
            }
        });

    }
}