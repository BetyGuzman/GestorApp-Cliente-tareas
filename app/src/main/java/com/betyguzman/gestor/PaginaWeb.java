package com.betyguzman.gestor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaginaWeb extends AppCompatActivity {

    WebView wvPagina;
    //usar controles dentro de la web
    WebSettings webSettings;

    //llama la libreria de JS
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_web);

        wvPagina = findViewById(R.id.wvPagina);
        webSettings = wvPagina.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        wvPagina.loadUrl("https://registrocursos.maxcodedev.com/");
        wvPagina.setWebViewClient(new WebViewClient());
    }
}