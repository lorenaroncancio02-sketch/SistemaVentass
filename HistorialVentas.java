
package com.mycompany.SistemaVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HistorialVentas extends JFrame {

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"N.º Venta", "Fecha", "Producto", "Cantidad",
                     "Precio unitario", "Total"}, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public HistorialVentas() {
        setTitle("Historial de ventas");
        setSize(850, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTable tabla = new JTable(modelo);
        JButton btnActualizar = new JButton("Actualizar historial");

        btnActualizar.addActionListener(e -> cargarVentas());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnActualizar, BorderLayout.SOUTH);

        cargarVentas();
    }

    private void cargarVentas() {
        modelo.setRowCount(0);

        String sql = "SELECT v.id_venta, v.fecha, p.nombre, "
                   + "v.cantidad, v.precio_venta, "
                   + "(v.cantidad * v.precio_venta) AS total "
                   + "FROM ventas v "
                   + "INNER JOIN productos p ON v.id_producto = p.id_producto "
                   + "ORDER BY v.id_venta DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getTimestamp("fecha"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("precio_venta"),
                    rs.getBigDecimal("total")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al consultar ventas: " + ex.getMessage());
        }
    }
}
