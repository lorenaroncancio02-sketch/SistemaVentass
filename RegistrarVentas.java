
package com.mycompany.SistemaVenta;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class RegistrarVentas extends JFrame {

    private final JComboBox<Producto> cmbProductos = new JComboBox<>();
    private final JTextField txtCantidad = new JTextField("1", 10);

    public RegistrarVentas() {
        setTitle("Registrar ventas");
        setSize(450, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnRegistrar = new JButton("Registrar venta");
        JButton btnActualizar = new JButton("Actualizar productos");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Producto:"));
        panel.add(cmbProductos);
        panel.add(new JLabel("Cantidad a vender:"));
        panel.add(txtCantidad);
        panel.add(btnActualizar);
        panel.add(btnRegistrar);

        add(panel);

        btnActualizar.addActionListener(e -> cargarProductos());
        btnRegistrar.addActionListener(e -> registrarVenta());

        cargarProductos();
    }

    private void cargarProductos() {
        cmbProductos.removeAllItems();

        String sql = "SELECT id_producto, nombre, precio, cantidad "
                   + "FROM productos ORDER BY nombre";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbProductos.addItem(new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("precio"),
                    rs.getInt("cantidad")
                ));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar productos: " + ex.getMessage());
        }
    }

    private void registrarVenta() {
        Producto producto = (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles.");
            return;
        }

        final int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Ingresa una cantidad válida.");
            return;
        }

        String consultar = "SELECT precio, cantidad FROM productos "
                         + "WHERE id_producto = ? FOR UPDATE";
        String insertar = "INSERT INTO ventas "
                        + "(id_producto, cantidad, precio_venta) VALUES (?, ?, ?)";
        String actualizar = "UPDATE productos SET cantidad = cantidad - ? "
                          + "WHERE id_producto = ? AND cantidad >= ?";

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            try {
                BigDecimal precio;
                int existencia;

                try (PreparedStatement ps = con.prepareStatement(consultar)) {
                    ps.setInt(1, producto.id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El producto ya no existe.");
                        }
                        precio = rs.getBigDecimal("precio");
                        existencia = rs.getInt("cantidad");
                    }
                }

                if (cantidad > existencia) {
                    throw new SQLException(
                        "Stock insuficiente. Disponibles: " + existencia);
                }

                try (PreparedStatement ps = con.prepareStatement(insertar)) {
                    ps.setInt(1, producto.id);
                    ps.setInt(2, cantidad);
                    ps.setBigDecimal(3, precio);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(actualizar)) {
                    ps.setInt(1, cantidad);
                    ps.setInt(2, producto.id);
                    ps.setInt(3, cantidad);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo actualizar el inventario.");
                    }
                }

                con.commit();

                BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));
                JOptionPane.showMessageDialog(this,
                    "Venta registrada correctamente.\n"
                    + "Producto: " + producto.nombre + "\n"
                    + "Cantidad: " + cantidad + "\n"
                    + "Total: $" + total);

                cargarProductos();

            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo registrar la venta: " + ex.getMessage());
        }
    }

    private static class Producto {
        int id;
        String nombre;
        BigDecimal precio;
        int cantidad;

        Producto(int id, String nombre, BigDecimal precio, int cantidad) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
        }

        @Override
        public String toString() {
            return nombre + " - $" + precio + " (Stock: " + cantidad + ")";
        }
    }
}
