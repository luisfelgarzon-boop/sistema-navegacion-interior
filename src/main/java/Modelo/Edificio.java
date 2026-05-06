/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 ** Clase modelo que representa un edificio en la BD.
 * @author felip
 */
public class Edificio {

    private int id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private int pisos;

    public Edificio(String nombre, String descripcion, String tipo, int pisos) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.pisos       = pisos;
    }

    public Edificio(int id, String nombre, String descripcion, String tipo, int pisos) {
        this(nombre, descripcion, tipo, pisos);
        this.id = id;
    }

    public int getId()             { return id; }
    public String getNombre()      { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getTipo()        { return tipo; }
    public int getPisos()          { return pisos; }

    public void setNombre(String nombre)           { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTipo(String tipo)               { this.tipo = tipo; }
    public void setPisos(int pisos)                { this.pisos = pisos; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Tipo: %s | Pisos: %d | %s",
                id, nombre, tipo, pisos, descripcion);
    }
}
