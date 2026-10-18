package com.mycompany.SistemaVenta;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Ventas e Inventario");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel(
            "SISTEMA DE VENTAS E INVENTARIO",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnProductos = new JButton("Gestionar productos");
        JButton btnInventario = new JButton("Consultar inventario");
        JButton btnVentas = new JButton("Registrar ventas");
        JButton btnHistorial = new JButton("Historial de ventas");

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 15, 15));
        panelBotones.setBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        panelBotones.add(btnProductos);
        panelBotones.add(btnInventario);
        panelBotones.add(btnVentas);
        panelBotones.add(btnHistorial);

        setLayout(new BorderLayout(10, 10));
        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
           
        
        btnProductos.addActionListener(e ->
        new GestionProductos().setVisible(true)
);

       btnInventario.addActionListener(e ->
    new ConsultarInventario().setVisible(true)
);

      btnVentas.addActionListener(e ->
    new RegistrarVentas().setVisible(true)
);

       btnHistorial.addActionListener(e ->
    new HistorialVentas().setVisible(true)
);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new VentanaPrincipal().setVisible(true)
        );
    }
}
