package com.betyguzman.gestor.ListaTareas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.betyguzman.gestor.Clientes.EditarClientes;
import com.betyguzman.gestor.Models.Tareas;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditarListaTareas extends AppCompatActivity {

    ImageView ivBack;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView tvFechaInicio;
    Button btnActualizarDatosTarea;
    Spinner espinnerEstatusEdit;
    EditText etTituloRegistroTareaEdit, etRegistroDescripcionEdit, etFechaLimiteEdit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String id, titulo, descripcion, fecha_inicio, fecha_limite, estatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lista_tareas);

        ivBack = findViewById(R.id.ivBack);
        tvFechaInicio = findViewById(R.id.tvFechaInicioEdit);
        etTituloRegistroTareaEdit = findViewById(R.id.etTituloRegistroTareaEdit);
        etRegistroDescripcionEdit = findViewById(R.id.etRegistroDescripcionEdit);
        etFechaLimiteEdit = findViewById(R.id.etFechaLimiteEdit);
        espinnerEstatusEdit = findViewById(R.id.spinnerEstatusEdit);
        btnActualizarDatosTarea = findViewById(R.id.btnActualizarDatosTarea);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tareas");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        getTarea(id);

        ivBack.setOnClickListener(v -> finish());

        btnActualizarDatosTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        etFechaLimiteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditarListaTareas.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
                                String fechaSeleccionada = dateFormat.format(selectedDate.getTime());

                                etFechaLimiteEdit.setText(fechaSeleccionada);
                            }
                        },
                        year, month, day
                );

                datePickerDialog.show();
            }
        });
    }

    private void validarDatos() {

        titulo = etTituloRegistroTareaEdit.getText().toString().trim();
        descripcion = etRegistroDescripcionEdit.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)){
            Toast.makeText(this, "Ingrese el Titulo", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(descripcion)){
            Toast.makeText(this, "Ingrese una descripcion", Toast.LENGTH_SHORT).show();
        }else {
            actualizarTarea();
        }
    }

    private void actualizarTarea() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("titulo", titulo);
        data.put("descripcion", descripcion);
        data.put("fecha_limite", fecha_limite);
        data.put("estatus", estatus);


        databaseReference.child(id).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditarListaTareas.this, "Cliente Actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarListaTareas.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTarea(String id) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Tareas tarea = snapshot.getValue(Tareas.class);

                    if (tarea != null) {

                        titulo = tarea.getTitulo();
                        descripcion = tarea.getDescripcion();
                        fecha_inicio = tarea.getFecha_inicio();
                        fecha_limite = tarea.getFecha_limite();
                        estatus = tarea.getEstatus();


                        etTituloRegistroTareaEdit.setText(titulo);
                        etRegistroDescripcionEdit.setText(descripcion);
                        etFechaLimiteEdit.setText(fecha_limite);
                        tvFechaInicio.setText(fecha_inicio);


                        String[] estatusTarea = {"Completado", "En Progreso", "No Completado"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarListaTareas.this,
                                android.R.layout.simple_spinner_item, estatusTarea);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        espinnerEstatusEdit.setAdapter(adapter);


                        int posicionTarea = adapter.getPosition(estatus);
                        if (posicionTarea != -1) {
                            espinnerEstatusEdit.setSelection(posicionTarea);
                        }


                        espinnerEstatusEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                estatus = adapterView.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
