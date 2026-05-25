/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import Modelo.Edificio;
import Modelo.RutaHistorial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * Gestor de persistencia en formato JSON.
 * Guarda y lee edificios y rutas en un archivo local datos.json.
 * Complementa la persistencia en BD MySQL.
 * @author felip
 */
public class JsonManager {

    private static final String ARCHIVO = "datos.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ==================== Inicialización ====================

    /**
     * Crea el archivo JSON vacío si no existe.
     */
    public static void inicializar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            JsonObject raiz = new JsonObject();
            raiz.add("edificios", new JsonArray());
            raiz.add("historial", new JsonArray());
            escribir(raiz);
            System.out.println("[JSON] Archivo datos.json creado.");
        }
    }

    // ==================== Edificios ====================

    /**
     * Guarda un edificio en el archivo JSON.
     * POST /edificios
     */
    public static void guardarEdificio(Edificio e) {
        JsonObject raiz = leer();
        JsonArray edificios = raiz.getAsJsonArray("edificios");

        JsonObject obj = new JsonObject();
        obj.addProperty("id",          e.getId());
        obj.addProperty("nombre",      e.getNombre());
        obj.addProperty("descripcion", e.getDescripcion());
        obj.addProperty("tipo",        e.getTipo());
        obj.addProperty("pisos",       e.getPisos());

        edificios.add(obj);
        escribir(raiz);
        System.out.println("[JSON] Edificio guardado: " + e.getNombre());
    }

    /**
     * Obtiene todos los edificios del archivo JSON.
     * GET /edificios
     */
    public static List<Edificio> obtenerEdificios() {
        List<Edificio> lista = new ArrayList<>();
        JsonObject raiz = leer();
        JsonArray edificios = raiz.getAsJsonArray("edificios");

        for (int i = 0; i < edificios.size(); i++) {
            JsonObject obj = edificios.get(i).getAsJsonObject();
            lista.add(new Edificio(
                obj.get("id").getAsInt(),
                obj.get("nombre").getAsString(),
                obj.get("descripcion").getAsString(),
                obj.get("tipo").getAsString(),
                obj.get("pisos").getAsInt()
            ));
        }
        return lista;
    }

    /**
     * Actualiza un edificio en el archivo JSON por ID.
     * PUT /edificios/{id}
     */
    public static boolean actualizarEdificio(Edificio e) {
        JsonObject raiz = leer();
        JsonArray edificios = raiz.getAsJsonArray("edificios");

        for (int i = 0; i < edificios.size(); i++) {
            JsonObject obj = edificios.get(i).getAsJsonObject();
            if (obj.get("id").getAsInt() == e.getId()) {
                obj.addProperty("nombre",      e.getNombre());
                obj.addProperty("descripcion", e.getDescripcion());
                obj.addProperty("tipo",        e.getTipo());
                obj.addProperty("pisos",       e.getPisos());
                escribir(raiz);
                System.out.println("[JSON] Edificio actualizado: " + e.getNombre());
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un edificio del archivo JSON por ID.
     * DELETE /edificios/{id}
     */
    public static boolean eliminarEdificio(int id) {
        JsonObject raiz = leer();
        JsonArray edificios = raiz.getAsJsonArray("edificios");
        JsonArray nuevos = new JsonArray();

        boolean encontrado = false;
        for (int i = 0; i < edificios.size(); i++) {
            JsonObject obj = edificios.get(i).getAsJsonObject();
            if (obj.get("id").getAsInt() == id) {
                encontrado = true;
            } else {
                nuevos.add(obj);
            }
        }

        if (encontrado) {
            raiz.add("edificios", nuevos);
            escribir(raiz);
            System.out.println("[JSON] Edificio eliminado ID: " + id);
        }
        return encontrado;
    }

    // ==================== Historial de rutas ====================

    /**
     * Guarda una ruta en el historial JSON.
     * POST /historial
     */
    public static void guardarRuta(RutaHistorial r) {
        JsonObject raiz = leer();
        JsonArray historial = raiz.getAsJsonArray("historial");

        JsonObject obj = new JsonObject();
        obj.addProperty("id",           r.getId());
        obj.addProperty("edificioId",   r.getEdificioId());
        obj.addProperty("nodoInicio",   r.getNodoInicio());
        obj.addProperty("nodoDestino",  r.getNodoDestino());
        obj.addProperty("algoritmo",    r.getAlgoritmo());
        obj.addProperty("fecha", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        historial.add(obj);
        escribir(raiz);
        System.out.println("[JSON] Ruta guardada en historial JSON.");
    }

    /**
     * Obtiene todo el historial del archivo JSON.
     * GET /historial
     */
    public static List<RutaHistorial> obtenerHistorial() {
        List<RutaHistorial> lista = new ArrayList<>();
        JsonObject raiz = leer();
        JsonArray historial = raiz.getAsJsonArray("historial");

        for (int i = 0; i < historial.size(); i++) {
            JsonObject obj = historial.get(i).getAsJsonObject();
            lista.add(new RutaHistorial(
                obj.get("id").getAsInt(),
                obj.get("edificioId").getAsInt(),
                obj.get("nodoInicio").getAsString(),
                obj.get("nodoDestino").getAsString(),
                obj.get("algoritmo").getAsString(),
                obj.get("fecha").getAsString()
            ));
        }
        return lista;
    }

    // ==================== Lectura y escritura ====================

    private static JsonObject leer() {
        try (Reader reader = new FileReader(ARCHIVO)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            JsonObject raiz = new JsonObject();
            raiz.add("edificios", new JsonArray());
            raiz.add("historial", new JsonArray());
            return raiz;
        }
    }

    private static void escribir(JsonObject obj) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            gson.toJson(obj, writer);
        } catch (IOException e) {
            System.err.println("[JSON] Error al escribir: " + e.getMessage());
        }
    }
}
