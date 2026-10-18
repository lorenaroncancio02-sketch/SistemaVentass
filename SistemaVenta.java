
package com.mycompany.SistemaVenta;

import java.sql.Connection;
import javax.swing.JOptionPane;

public class SistemaVenta {

    public static void main(String[] args) {
        try (Connection conexion = Conexion.conectar()) {
            JOptionPane.showMessageDialog(
                null,
                "¡Conexión exitosa con la base de datos ventas!"
            );

            System.out.println("Base de datos conectada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Error de conexión: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}

