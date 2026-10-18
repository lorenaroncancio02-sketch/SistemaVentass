
package com.mycompany.SistemaVenta;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GestionProductos extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtCantidad = new JTextField();

    public GestionProductos() {
        setTitle("Gestion de productos");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Descripcion:"));
        formulario.add(txtDescripcion);
        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecio);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(txtCantidad);

        JButton btnGuardar = new JButton("Guardar producto");
        JButton btnLimpiar = new JButton("Limpiar");

        formulario.add(btnGuardar);
        formulario.add(btnLimpiar);

        add(formulario);

        btnGuardar.addActionListener(e -> guardarProducto());

        btnLimpiar.addActionListener(e -> {
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtPrecio.setText("");
            txtCantidad.setText("");
        });
    }

    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del producto.");
            return;
        }

        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim()
                    .replace(",", "."));
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (precio < 0 || cantidad < 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio y la cantidad no pueden ser negativos.");
                return;
            }

            String sql = "INSERT INTO productos "
                    + "(nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?)";

            try (Connection con = Conexion.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, nombre);
                ps.setString(2, descripcion);
                ps.setDouble(3, precio);
                ps.setInt(4, cantidad);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Producto guardado correctamente.");
                txtNombre.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Verifica que el precio y la cantidad sean números válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el producto: " + ex.getMessage());
        }
    }
}

