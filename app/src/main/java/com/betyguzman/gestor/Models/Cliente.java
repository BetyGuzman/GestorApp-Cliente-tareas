package com.betyguzman.gestor.Models;

import androidx.annotation.NonNull;

public class Cliente {
    String id;
    String id_tipo_cliente;
    String tipo_cliente;
    String nombre;
    String tipo_documento;
    String documento;
    String telefono;

    public Cliente() {
    }

    public Cliente(String id, String id_tipo_cliente, String tipo_cliente, String nombre, String tipo_documento, String documento, String telefono) {
        this.id = id;
        this.id_tipo_cliente = id_tipo_cliente;
        this.tipo_cliente = tipo_cliente;
        this.nombre = nombre;
        this.tipo_documento = tipo_documento;
        this.documento = documento;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_tipo_cliente() {
        return id_tipo_cliente;
    }

    public void setId_tipo_cliente(String id_tipo_cliente) {
        this.id_tipo_cliente = id_tipo_cliente;
    }

    public String getTipo_cliente() {
        return tipo_cliente;
    }

    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre;
    }
}
