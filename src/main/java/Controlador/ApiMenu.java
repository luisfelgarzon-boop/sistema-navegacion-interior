/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.Edificio;
import Modelo.NodoDB;
import Modelo.RutaHistorial;
import java.util.List;
import java.util.Scanner;
/**
 * Simula una API RESTful con operaciones GET y POST.
 * GET  → consultar datos de la BD
 * POST → insertar datos en la BD
 * @author felip
 */
public class ApiMenu {

    private final NavigationController controller;
    private final Scanner scanner;

    public ApiMenu(NavigationController controller) {
        this.controller = controller;
        this.scanner    = new Scanner(System.in);
    }

    /**
     * Lanza el menú principal de la API.
     */
    public void iniciar() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     API REST - Sistema Navegación    ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n¿Qué operación deseas realizar?");
            System.out.println("  1. GET  - Consultar datos");
            System.out.println("  2. POST - Insertar datos");
            System.out.println("  0. Salir del menú API");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> menuGet();
                case "2" -> menuPost();
                case "0" -> corriendo = false;
                default  -> System.out.println("Opción no válida.");
            }
        }
        System.out.println("Cerrando API...");
    }

    // ==================== GET ====================

    private void menuGet() {
        System.out.println("\n── GET ──────────────────────────────");
        System.out.println("  1. GET /edificios        → Listar todos los edificios");
        System.out.println("  2. GET /edificios/{id}   → Buscar edificio por ID");
        System.out.println("  3. GET /nodos/{edificioId} → Listar nodos de un edificio");
        System.out.println("  4. GET /historial        → Ver historial de rutas");
        System.out.println("  5. GET /historial/{edificioId} → Historial por edificio");
        System.out.print("Opción: ");

        String op = scanner.nextLine().trim();
        switch (op) {
            case "1" -> getEdificios();
            case "2" -> getEdificioPorId();
            case "3" -> getNodosPorEdificio();
            case "4" -> getHistorial();
            case "5" -> getHistorialPorEdificio();
            default  -> System.out.println("Opción no válida.");
        }
    }

    private void getEdificios() {
        System.out.println("\n[GET] /edificios");
        List<Edificio> lista = controller.getEdificios();
        if (lista.isEmpty()) {
            System.out.println("No hay edificios registrados.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void getEdificioPorId() {
        System.out.print("\n[GET] /edificios/{id} → Ingresa el ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Edificio e = controller.getEdificio(id);
            if (e != null) System.out.println(e);
            else System.out.println("Edificio no encontrado.");
        } catch (NumberFormatException ex) {
            System.out.println("ID inválido.");
        }
    }

    private void getNodosPorEdificio() {
        System.out.print("\n[GET] /nodos/{edificioId} → Ingresa el ID del edificio: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<NodoDB> nodos = controller.getNodos(id);
            if (nodos.isEmpty()) System.out.println("No hay nodos para ese edificio.");
            else nodos.forEach(System.out::println);
        } catch (NumberFormatException ex) {
            System.out.println("ID inválido.");
        }
    }

    private void getHistorial() {
        System.out.println("\n[GET] /historial");
        List<RutaHistorial> lista = controller.getHistorial();
        if (lista.isEmpty()) System.out.println("No hay rutas en el historial.");
        else lista.forEach(System.out::println);
    }

    private void getHistorialPorEdificio() {
        System.out.print("\n[GET] /historial/{edificioId} → Ingresa el ID del edificio: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<RutaHistorial> lista = controller.getHistorial(id);
            if (lista.isEmpty()) System.out.println("No hay historial para ese edificio.");
            else lista.forEach(System.out::println);
        } catch (NumberFormatException ex) {
            System.out.println("ID inválido.");
        }
    }

    // ==================== POST ====================

    private void menuPost() {
        System.out.println("\n── POST ─────────────────────────────");
        System.out.println("  1. POST /edificios  → Registrar nuevo edificio");
        System.out.println("  2. POST /nodos      → Registrar nuevo nodo");
        System.out.print("Opción: ");

        String op = scanner.nextLine().trim();
        switch (op) {
            case "1" -> postEdificio();
            case "2" -> postNodo();
            default  -> System.out.println("Opción no válida.");
        }
    }

    private void postEdificio() {
        System.out.println("\n[POST] /edificios → Ingresa los datos:");
        try {
            System.out.print("  Nombre: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("  Descripción: ");
            String descripcion = scanner.nextLine().trim();

            System.out.print("  Tipo (Universitario / Comercial / Hospitalario): ");
            String tipo = scanner.nextLine().trim();

            System.out.print("  Número de pisos: ");
            int pisos = Integer.parseInt(scanner.nextLine().trim());

            Edificio nuevo = new Edificio(nombre, descripcion, tipo, pisos);
            boolean ok = controller.agregarEdificio(nuevo);
            System.out.println(ok
                ? "✓ Edificio registrado correctamente."
                : "✗ Error al registrar el edificio.");
        } catch (NumberFormatException ex) {
            System.out.println("Número de pisos inválido.");
        }
    }

    private void postNodo() {
        System.out.println("\n[POST] /nodos → Ingresa los datos:");
        try {
            System.out.print("  ID del nodo (ej: N-3-4): ");
            String nodoId = scanner.nextLine().trim();

            System.out.print("  X: ");
            int x = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Y: ");
            int y = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Tipo (FREE_SPACE / DOOR / STAIRS / OBSTACLE): ");
            String tipo = scanner.nextLine().trim();

            System.out.print("  ID del edificio: ");
            int edificioId = Integer.parseInt(scanner.nextLine().trim());

            NodoDB nodo = new NodoDB(nodoId, x, y, tipo, edificioId);
            boolean ok = controller.guardarNodo(nodo);
            System.out.println(ok
                ? " Nodo registrado correctamente."
                : " Error al registrar el nodo.");
        } catch (NumberFormatException ex) {
            System.out.println("Valor numérico inválido.");
        }
    }
}
