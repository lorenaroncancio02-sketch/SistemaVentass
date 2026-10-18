
package com.mycompany.SistemaVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ConsultarInventario extends JFrame {

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Nombre", "Descripción", "Precio", "Cantidad"}, 0
    ) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public ConsultarInventario() {
        setTitle("Consulta de inventario");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTable tabla = new JTable(modelo);
        JButton btnActualizar = new JButton("Actualizar inventario");

        btnActualizar.addActionListener(e -> cargarProductos());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnActualizar, BorderLayout.SOUTH);

        cargarProductos();
    }

    private void cargarProductos() {
        modelo.setRowCount(0);

        String sql = "SELECT id_producto, nombre, descripcion, precio, cantidad "
                   + "FROM productos ORDER BY id_producto";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getBigDecimal("precio"),
                    rs.getInt("cantidad")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al consultar inventario: " + ex.getMessage());
        }
    }
}

