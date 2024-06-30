import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/* Clase Criterio, gestión de criterios generales del juzgado y su listado */

public class Criterio {
    private int id;
    private String descripcion;

    public Criterio(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public static void cargarCriterio(Connection conexion, Scanner scanner) {
        try {
            System.out.print("Ingrese la descripción del criterio: ");
            String descripcion = scanner.nextLine();

            String sql = "INSERT INTO criterio (descripcion) VALUES (?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, descripcion);
                pstmt.executeUpdate();
                System.out.println("Criterio cargado exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar el criterio: " + e.getMessage());
        }
    }

    public static void listarCriterios(Connection conexion) {
        String sql = "SELECT * FROM criterio";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay criterios cargados.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar criterios: " + e.getMessage());
        }
    }

    public static void borrarCriterio(Connection conexion, Scanner scanner) {
        listarCriterios(conexion);
        try {
            System.out.print("Ingrese el ID del criterio a borrar (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idCriterio = Integer.parseInt(input);

            String sql = "DELETE FROM criterio WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idCriterio);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Criterio borrado exitosamente.");
                } else {
                    System.out.println("Criterio no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al borrar el criterio: " + e.getMessage());
        }
    }
}
