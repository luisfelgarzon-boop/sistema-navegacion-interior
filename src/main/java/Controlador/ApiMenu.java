/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Edificio;
import Modelo.NodoDB;
import Modelo.RutaHistorial;
import Util.JsonManager;
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

    public void iniciar() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     API REST - Sistema Navegación    ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n¿Qué operación deseas realizar?");
            System.out.println("  1. GET    - Consultar datos");
            System.out.println("  2. POST   - Insertar datos");
            System.out.println("  3. PUT    - Actualizar datos");
            System.out.println("  4. DELETE - Eliminar datos");
            System.out.println("  0. Salir del menú API");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> menuGet();
                case "2" -> menuPost();
                case "3" -> menuPut();
                case "4" -> menuDelete();
                case "0" -> corriendo = false;
                default  -> System.out.println("Opción no válida.");
            }
        }
        System.out.println("Cerrando API...");
    }

    // ==================== GET ====================

    private void menuGet() {
        System.out.println("\n── GET ──────────────────────────────");
        System.out.println("  1. GET /edificios          → Listar desde BD");
        System.out.println("  2. GET /edificios/{id}     → Buscar por ID en BD");
        System.out.println("  3. GET /nodos/{id}         → Nodos de un edificio");
        System.out.println("  4. GET /historial          → Historial desde BD");
        System.out.println("  5. GET /historial/{id}     → Historial por edificio");
        System.out.println("  6. GET /json/edificios     → Leer desde JSON");
        System.out.println("  7. GET /json/historial     → Historial desde JSON");
        System.out.print("Opción: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> getEdificios();
            case "2" -> getEdificioPorId();
            case "3" -> getNodosPorEdificio();
            case "4" -> getHistorial();
            case "5" -> getHistorialPorEdificio();
            case "6" -> getEdificiosJson();
            case "7" -> getHistorialJson();
            default  -> System.out.println("Opción no válida.");
        }
    }

    private void getEdificios() {
        System.out.println("\n[GET] /edificios");
        List<Edificio> lista = controller.getEdificios();
        if (lista.isEmpty()) System.out.println("No hay edificios registrados.");
        else lista.forEach(System.out::println);
    }

    private void getEdificioPorId() {
        System.out.print("\n[GET] /edificios/{id} → ID: ");
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
        System.out.print("\n[GET] /nodos/{id} → ID edificio: ");
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
        System.out.print("\n[GET] /historial/{id} → ID edificio: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            List<RutaHistorial> lista = controller.getHistorial(id);
            if (lista.isEmpty()) System.out.println("No hay historial.");
            else lista.forEach(System.out::println);
        } catch (NumberFormatException ex) {
            System.out.println("ID inválido.");
        }
    }

    private void getEdificiosJson() {
        System.out.println("\n[GET] /json/edificios");
        List<Edificio> lista = JsonManager.obtenerEdificios();
        if (lista.isEmpty()) System.out.println("No hay edificios en JSON.");
        else lista.forEach(System.out::println);
    }

    private void getHistorialJson() {
        System.out.println("\n[GET] /json/historial");
        List<RutaHistorial> lista = JsonManager.obtenerHistorial();
        if (lista.isEmpty()) System.out.println("No hay historial en JSON.");
        else lista.forEach(System.out::println);
    }

    // ==================== POST ====================

    private void menuPost() {
        System.out.println("\n── POST ─────────────────────────────");
        System.out.println("  1. POST /edificios → Registrar edificio");
        System.out.println("  2. POST /nodos     → Registrar nodo");
        System.out.print("Opción: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> postEdificio();
            case "2" -> postNodo();
            default  -> System.out.println("Opción no válida.");
        }
    }

    private void postEdificio() {
        System.out.println("\n[POST] /edificios");
        try {
            System.out.print("  Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("  Descripción: ");
            String descripcion = scanner.nextLine().trim();
            System.out.print("  Tipo (Universitario / Comercial / Hospitalario): ");
            String tipo = scanner.nextLine().trim();
            System.out.print("  Pisos: ");
            int pisos = Integer.parseInt(scanner.nextLine().trim());

            boolean ok = controller.agregarEdificio(
                new Edificio(nombre, descripcion, tipo, pisos)
            );
            System.out.println(ok
                ? "✓ Edificio registrado en BD y JSON."
                : "✗ Error al registrar.");
        } catch (NumberFormatException ex) {
            System.out.println("Número de pisos inválido.");
        }
    }

    private void postNodo() {
        System.out.println("\n[POST] /nodos");
        try {
            System.out.print("  ID nodo (ej: N-3-4): ");
            String nodoId = scanner.nextLine().trim();
            System.out.print("  X: ");
            int x = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("  Y: ");
            int y = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("  Tipo (FREE_SPACE / DOOR / STAIRS / OBSTACLE): ");
            String tipo = scanner.nextLine().trim();
            System.out.print("  ID edificio: ");
            int edificioId = Integer.parseInt(scanner.nextLine().trim());

            boolean ok = controller.guardarNodo(
                new NodoDB(nodoId, x, y, tipo, edificioId)
            );
            System.out.println(ok ? "✓ Nodo registrado." : "✗ Error al registrar.");
        } catch (NumberFormatException ex) {
            System.out.println("Valor numérico inválido.");
        }
    }

    // ==================== PUT ====================

    private void menuPut() {
        System.out.println("\n── PUT ──────────────────────────────");
        System.out.println("  1. PUT /edificios/{id} → Actualizar edificio");
        System.out.print("Opción: ");

        if (scanner.nextLine().trim().equals("1")) putEdificio();
        else System.out.println("Opción no válida.");
    }

    private void putEdificio() {
        System.out.println("\n[PUT] /edificios/{id}");
        try {
            System.out.print("  ID del edificio a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Edificio existente = controller.getEdificio(id);
            if (existente == null) {
                System.out.println("Edificio no encontrado.");
                return;
            }

            System.out.print("  Nuevo nombre [" + existente.getNombre() + "]: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) nombre = existente.getNombre();

            System.out.print("  Nueva descripción [" + existente.getDescripcion() + "]: ");
            String desc = scanner.nextLine().trim();
            if (desc.isEmpty()) desc = existente.getDescripcion();

            System.out.print("  Nuevo tipo [" + existente.getTipo() + "]: ");
            String tipo = scanner.nextLine().trim();
            if (tipo.isEmpty()) tipo = existente.getTipo();

            System.out.print("  Nuevos pisos [" + existente.getPisos() + "]: ");
            String pisosStr = scanner.nextLine().trim();
            int pisos = pisosStr.isEmpty()
                ? existente.getPisos()
                : Integer.parseInt(pisosStr);

            existente.setNombre(nombre);
            existente.setDescripcion(desc);
            existente.setTipo(tipo);
            existente.setPisos(pisos);

            boolean ok = controller.actualizarEdificio(existente);
            System.out.println(ok
                ? "✓ Edificio actualizado en BD y JSON."
                : "✗ Error al actualizar.");
        } catch (NumberFormatException ex) {
            System.out.println("Valor numérico inválido.");
        }
    }

    // ==================== DELETE ====================

    private void menuDelete() {
        System.out.println("\n── DELETE ───────────────────────────");
        System.out.println("  1. DELETE /edificios/{id} → Eliminar edificio");
        System.out.print("Opción: ");

        if (scanner.nextLine().trim().equals("1")) deleteEdificio();
        else System.out.println("Opción no válida.");
    }

    private void deleteEdificio() {
        System.out.println("\n[DELETE] /edificios/{id}");
        try {
            System.out.print("  ID del edificio a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Edificio existente = controller.getEdificio(id);
            if (existente == null) {
                System.out.println("Edificio no encontrado.");
                return;
            }

            System.out.print("  ¿Confirmas eliminar '" + existente.getNombre() + "'? (s/n): ");
            String confirm = scanner.nextLine().trim();
            if (!confirm.equalsIgnoreCase("s")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean ok = controller.eliminarEdificio(id);
            System.out.println(ok
                ? " Edificio eliminado de BD y JSON."
                : " Error al eliminar.");
        } catch (NumberFormatException ex) {
            System.out.println("ID inválido.");
        }
    }
}
