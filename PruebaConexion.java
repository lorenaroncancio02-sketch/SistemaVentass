
package com.mycompany.SistemaVenta;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {
        try (Connection conexion = Conexion.conectar()) {
            System.out.println(
                "Conexion exitosa con la base de datos ventas."
            );
        } catch (Exception e) {
            System.out.println(
                "Error de conexion: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
