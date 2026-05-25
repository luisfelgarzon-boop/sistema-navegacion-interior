/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *Clase modelo que representa un nodo guardado en la BD.
 * @author felip
 */
public class NodoDB {

    private int id;
    private String nodoId;
    private int x;
    private int y;
    private String tipo;
    private int edificioId;

    public NodoDB(String nodoId, int x, int y, String tipo, int edificioId) {
        this.nodoId     = nodoId;
        this.x          = x;
        this.y          = y;
        this.tipo       = tipo;
        this.edificioId = edificioId;
    }

    public NodoDB(int id, String nodoId, int x, int y, String tipo, int edificioId) {
        this(nodoId, x, y, tipo, edificioId);
        this.id = id;
    }

    public int getId()          { return id; }
    public String getNodoId()   { return nodoId; }
    public int getX()           { return x; }
    public int getY()           { return y; }
    public String getTipo()     { return tipo; }
    public int getEdificioId()  { return edificioId; }

    @Override
    public String toString() {
        return String.format("[%d] Nodo %s | Pos: (%d,%d) | Tipo: %s | Edificio: %d",
                id, nodoId, x, y, tipo, edificioId);
    }
}
