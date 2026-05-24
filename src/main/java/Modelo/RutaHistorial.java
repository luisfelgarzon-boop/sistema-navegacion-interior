/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *Clase modelo que representa una ruta calculada guardada en el historial.
 * @author felip
 */
public class RutaHistorial {

    private int id;
    private int edificioId;
    private String nodoInicio;
    private String nodoDestino;
    private String algoritmo;
    private String fecha;

    public RutaHistorial(int edificioId, String nodoInicio,
                         String nodoDestino, String algoritmo) {
        this.edificioId  = edificioId;
        this.nodoInicio  = nodoInicio;
        this.nodoDestino = nodoDestino;
        this.algoritmo   = algoritmo;
    }

    public RutaHistorial(int id, int edificioId, String nodoInicio,
                         String nodoDestino, String algoritmo, String fecha) {
        this(edificioId, nodoInicio, nodoDestino, algoritmo);
        this.id    = id;
        this.fecha = fecha;
    }

    public int getId()             { return id; }
    public int getEdificioId()     { return edificioId; }
    public String getNodoInicio()  { return nodoInicio; }
    public String getNodoDestino() { return nodoDestino; }
    public String getAlgoritmo()   { return algoritmo; }
    public String getFecha()       { return fecha; }

    @Override
    public String toString() {
        return String.format("[%d] %s → %s | Algoritmo: %s | Fecha: %s",
                id, nodoInicio, nodoDestino, algoritmo, fecha);
    }
}