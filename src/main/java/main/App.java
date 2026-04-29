/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import Controlador.NavigationController;
import Vista.MainView;

/**
 *  Punto de entrada principal del Sistema de Navegación Inteligente en Interiores.
 * Inicializa los componentes MVC y lanza la interfaz gráfica.
 * Patrón MVC:
 *   - El Modelo se inicializa dentro del Controlador.
 *   - La Vista se crea aquí y se vincula al Controlador.
 *   - El Controlador coordina la comunicación entre ambos.
 * @author felip
 */
public class App {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println(" Sistema de Navegación Inteligente");
        System.out.println(" en Interiores v1.0");
        System.out.println("===========================================");
        System.out.println("Iniciando sistema...");

        // Lanzar la interfaz gráfica en el hilo de Swing (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            NavigationController controller = new NavigationController();
            MainView view = new MainView(controller);
            controller.setView(view);
            view.setVisible(true);
            System.out.println("Sistema iniciado correctamente.");
        });
    }
}
