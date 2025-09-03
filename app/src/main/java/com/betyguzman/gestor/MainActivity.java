package com.betyguzman.gestor;

import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText etCorreo, etContrasena;
    Button btnWeb, btnIngresar;
    ImageView btnVerContrasenaMain;
    TextView tvRegistrate;
    FirebaseAuth firebaseAuth;
//    ProgressDialog progressDialog;

    Boolean isVisibleMain;

    String correo="", contrasena="";
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
        setContentView(R.layout.activity_main);

        etCorreo = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etClave);

        isVisibleMain = false;

        btnWeb = findViewById(R.id.btnWeb);
        btnIngresar= findViewById(R.id.btnIngresar);
        tvRegistrate = findViewById(R.id.tvRegistrate);
        btnVerContrasenaMain = findViewById(R.id.btnVerContrasenaMain);

        firebaseAuth = FirebaseAuth.getInstance();




        btnVerContrasenaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisibleMain = !isVisibleMain;

                if (etContrasena.getText() != null && !etContrasena.getText().toString().isEmpty()) {
                    if (isVisibleMain) {
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        btnVerContrasenaMain.setImageResource(R.drawable.contrasenamostrada);
                    } else {
                        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        btnVerContrasenaMain.setImageResource(R.drawable.contrasena);
                    }
                    etContrasena.setSelection(etContrasena.getText().length());
                }
            }
        });


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Ingraste", Toast.LENGTH_SHORT).show();
                validarDatos();
            }
        });

        tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Btyyyyy btnWeb", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, PaginaWeb.class));
            }
        });


    }

    private void validarDatos(){
        showLoadingDialog("Validando usuario", "loadinghand.json");
        correo = etCorreo.getText().toString().trim();
        contrasena = etContrasena.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(MainActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();

        }else {
//            Toast.makeText(this, "Verificando datos...", Toast.LENGTH_SHORT).show();
            hideLoadingDialog();
            logearUsuario();
        }

    }
    private void logearUsuario() {
//        progressDialog.setMessage("Iniciando sesion...");
//        progressDialog.show();
        showLoadingDialog("Ingresando...", "validardatos.json");

        firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoadingDialog();
                        if (task.isSuccessful()){
//                            progressDialog.dismiss();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, Dashboard.class));
                            Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
//                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Verifique el correo o contraseña, por favor", Toast.LENGTH_SHORT).show();
                            hideLoadingDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoadingDialog();

                    }
                });
    }
}







