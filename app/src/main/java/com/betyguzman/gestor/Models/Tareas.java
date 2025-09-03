package com.betyguzman.gestor.Models;

public class Tareas {
    String id;
    String titulo;
    String descripcion;
    String fecha_inicio;
    String fecha_limite;
    String estatus;

    public Tareas() {
    }

    public Tareas(String id, String titulo, String descripcion, String fecha_inicio, String fecha_limite, String estatus) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_limite = fecha_limite;
        this.estatus = estatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
