package com.betyguzman.gestor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText etNombres, etApellidos, etCorreo, etContrasena,etContrasenaConfirmada, etCelular;
    TextView tvIrlogin;
    Button btnRegistrarR;
    ImageView btnVerContrasenaR, btnVerContrasenaRConfirmada;
    FirebaseAuth firebaseAuth;
//    ProgressDialog progressDialog;
    String nombres="", apellidos="", correo="", contrasena="", celular="";
    Boolean isVisibleRegistro, isVisibleRegistroC;
    private AlertDialog loadingDialog;

    private void showLoadingDialog(String mensaje, String animacionLottie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.progress_bar, null);

        LottieAnimationView lottieJson = view.findViewById(R.id.lottieJson);
        TextView tvMensaje = view.findViewById(R.id.tvCargando);

        lottieJson.setAnimation(animacionLottie);
        lottieJson.playAnimation();
        tvMensaje.setText(mensaje);

        builder.setView(view);
        builder.setCancelable(false);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombres =findViewById(R.id.etNombresR);
        etApellidos =findViewById(R.id.etApellidosR);
        etCorreo =findViewById(R.id.etCorreosR);
        etContrasena =findViewById(R.id.etContrasenaR);
        etContrasenaConfirmada =findViewById(R.id.etContrasenaRConfirmada);
        etCelular =findViewById(R.id.etCelularR);

        tvIrlogin = findViewById(R.id.tvIrlogin);
        btnRegistrarR = findViewById(R.id.btnRegistrarR);
        btnVerContrasenaR = findViewById(R.id.btnVerContrasenaR);
        btnVerContrasenaRConfirmada = findViewById(R.id.btnVerContrasenaRConfirmada);

        firebaseAuth = FirebaseAuth.getInstance();

        isVisibleRegistro = false;
        isVisibleRegistroC = false;

//        progressDialog = new ProgressDialog(Registro.this);
//        progressDialog.setTitle("Espere por favor...");
//        progressDialog.setCanceledOnTouchOutside(false);


        btnVerContrasenaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisibleRegistro = !isVisibleRegistro;

                if (etContrasena.getText() !=null && !etContrasena.getText().toString().isEmpty()){
                    if (isVisibleRegistro){
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnVerContrasenaR.setImageResource(R.drawable.contrasenamostrada);
                    }else {
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        btnVerContrasenaR.setImageResource(R.drawable.contrasena);
                    }
                    etContrasena.setSelection(etContrasena.getText().length());
                }
            }
        });

        btnVerContrasenaRConfirmada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisibleRegistroC = !isVisibleRegistroC;

                if (etContrasenaConfirmada.getText() !=null && !etContrasenaConfirmada.getText().toString().isEmpty()){
                    if (isVisibleRegistroC){
                        etContrasenaConfirmada.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnVerContrasenaRConfirmada.setImageResource(R.drawable.contrasenamostrada);
                    }else {
                        etContrasenaConfirmada.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        btnVerContrasenaRConfirmada.setImageResource(R.drawable.contrasena);
                    }
                    etContrasenaConfirmada.setSelection(etContrasenaConfirmada.getText().length());
                }
            }
        });









        tvIrlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sto hace que se cree un bucle de ventanas o activiades
//                startActivity(new Intent(Registro.this, MainActivity.class));
                finish();
            }
        });

        btnRegistrarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Registro.this, "Te registraste", Toast.LENGTH_SHORT).show();
                validarDatos();
            }
        });
    }

    private void validarDatos() {

        showLoadingDialog("Validando usuario", "validardatos.json");
        //Alamacena los valores de las vriables que se relacionan con su id
        nombres = etNombres.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        correo = etCorreo.getText().toString().trim();
        contrasena = etContrasena.getText().toString().trim();
        celular = etCelular.getText().toString().trim();


        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(this, "El campo Nombre esta vacio", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(apellidos)){
            Toast.makeText(this, "El campo Apellido esta vacio", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(contrasena) || contrasena.length()<6){
            Toast.makeText(this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.PHONE.matcher(celular).matches()){
            Toast.makeText(this, "Número de tlefono incompleto", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "Registrando...", Toast.LENGTH_SHORT).show();
            registrar();
        }
    }


    public void registrar(){
//        progressDialog.setMessage("Registrando usuario...");
//        progressDialog.show();

        showLoadingDialog("Registrando...", "loadinghand.json");

        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                guardarUsuario();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //cierra el dialogo
//                progressDialog.dismiss();
                Toast.makeText(Registro.this, "Ocurrio un problma, revisa los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarUsuario(){
//        progressDialog.setMessage("Guardando usuario...");
//        progressDialog.show();
        showLoadingDialog("Guardando usuario...", "saved.json");

        String uid= firebaseAuth.getUid();
        HashMap<String, String> datosUsuario = new HashMap<>();

        datosUsuario.put("uid", uid);
        datosUsuario.put("nombre_usuario", nombres);
        datosUsuario.put("apellidos", apellidos);
        datosUsuario.put("correo", correo);
        datosUsuario.put("contrasena", contrasena);
        datosUsuario.put("celular", celular);
        datosUsuario.put("estado", "1");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        assert uid != null; // para que se inicialice y no venga nulo
        databaseReference.child(uid).setValue(datosUsuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                progressDialog.dismiss();
                hideLoadingDialog();
                Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Registro.this, Dashboard.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                hideLoadingDialog();
                Toast.makeText(Registro.this, "Ocurrio un problema", Toast.LENGTH_SHORT).show();
            }
        });
    }

}