import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Consulta {
    private int id;
    private int idExpediente;
    private String consulta;
    private String respuesta;

    /* Clase Consulta, gestiona las consultas y respuestas a estas */

    public Consulta(int id, int idExpediente, String consulta, String respuesta) {
        this.id = id;
        this.idExpediente = idExpediente;
        this.consulta = consulta;
        this.respuesta = respuesta;
    }

    public static void cargarConsulta(Connection conexion, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del expediente a consultar (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            System.out.print("Ingrese la consulta del expediente: ");
            String consulta = scanner.nextLine();

            String sql = "INSERT INTO consultas (id_expediente, consulta) VALUES (?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idExpediente);
                pstmt.setString(2, consulta);
                pstmt.executeUpdate();
                System.out.println("Consulta cargada exitosamente.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al cargar la consulta: " + e.getMessage());
        }
    }

    public static void listarConsultas(Connection conexion) {
        String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, c.id AS consulta_id, c.consulta " +
                "FROM expedientes e " +
                "JOIN consultas c ON e.id = c.id_expediente " +
                "WHERE c.respuesta IS NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes con consultas pendientes.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tConsulta ID: " + rs.getInt("consulta_id") + " - " + rs.getString("consulta"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes con consultas: " + e.getMessage());
        }
    }

    public static void listarConsultasConRespuestas(Connection conexion) {
        String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, c.id AS consulta_id, c.consulta, c.respuesta " +
                "FROM expedientes e " +
                "JOIN consultas c ON e.id = c.id_expediente " +
                "WHERE c.respuesta IS NOT NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay consultas con respuestas.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tConsulta ID: " + rs.getInt("consulta_id") + " - " + rs.getString("consulta"));
                System.out.println("\t\tRespuesta: " + rs.getString("respuesta"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar consultas con respuestas: " + e.getMessage());
        }
    }

    public static void borrarConsulta(Connection conexion, Scanner scanner) {
        listarConsultas(conexion);
        try {
            System.out.print("Ingrese el ID de la consulta a borrar (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idConsulta = Integer.parseInt(input);

            if (tieneRespuesta(conexion, idConsulta)) {
                System.out.print("La consulta tiene una respuesta. ¿Desea borrarla? (Si/No): ");
                String confirmacion = scanner.nextLine();
                if (confirmacion.equalsIgnoreCase("No")) return;

                borrarRespuesta(conexion, idConsulta);
            }

            String deleteSql = "DELETE FROM consultas WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(deleteSql)) {
                pstmt.setInt(1, idConsulta);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Consulta borrada exitosamente.");
                } else {
                    System.out.println("Consulta no encontrada.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al borrar la consulta: " + e.getMessage());
        }
    }

    public static void responderConsulta(Connection conexion, Scanner scanner) {
        listarConsultas(conexion);
        try {
            System.out.print("Ingrese el ID de la consulta a responder (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idConsulta = Integer.parseInt(input);

            System.out.print("Ingrese la respuesta a la consulta: ");
            String respuesta = scanner.nextLine();

            String sql = "UPDATE consultas SET respuesta = ? WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, respuesta);
                pstmt.setInt(2, idConsulta);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Respuesta agregada exitosamente.");
                } else {
                    System.out.println("Consulta no encontrada.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al responder la consulta: " + e.getMessage());
        }
    }

    private static boolean tieneRespuesta(Connection conexion, int idConsulta) throws SQLException {
        String sql = "SELECT respuesta FROM consultas WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idConsulta);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getString("respuesta") != null;
        }
    }

    private static void borrarRespuesta(Connection conexion, int idConsulta) throws SQLException {
        String deleteSql = "UPDATE consultas SET respuesta = NULL WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(deleteSql)) {
            pstmt.setInt(1, idConsulta);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Respuesta borrada exitosamente.");
            } else {
                System.out.println("Consulta no encontrada.");
            }
        }
    }
}
