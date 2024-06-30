import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class AProyectar {
    private int idExpediente;
    private String responsable;
    private String tipoProyectar;
    private Date fechaProyeccion;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public AProyectar(int idExpediente, String responsable, String tipoProyectar, Date fechaProyeccion) {
        this.idExpediente = idExpediente;
        this.responsable = responsable;
        this.tipoProyectar = tipoProyectar;
        this.fechaProyeccion = fechaProyeccion;
    }

    public static void asignarEstadoAResolverOSentencia(Connection conexion, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del expediente para asignar el estado (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            String tipoProyectar;
            while (true) {
                System.out.print("Ingrese el estado (A resolver o A sentencia) (o 'Esc' para volver): ");
                tipoProyectar = scanner.nextLine();
                if (tipoProyectar.equalsIgnoreCase("A resolver") || tipoProyectar.equalsIgnoreCase("A sentencia")) {
                    break;
                } else {
                    System.out.println("Estado inválido. Por favor, ingrese 'A resolver' o 'A sentencia'.");
                }
            }

            String sql = "INSERT INTO aproyectar (id_expediente, responsable, tipoProyectar, fechaProyeccion) VALUES (?, ?, ?, NULL)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idExpediente);
                pstmt.setString(2, "");
                pstmt.setString(3, tipoProyectar.toLowerCase());
                pstmt.executeUpdate();
                System.out.println("Estado asignado exitosamente.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al asignar estado: " + e.getMessage());
        }
    }

    public static void listarExpedientesAResolverOSentencia(Connection conexion) {
        String sql = "SELECT e.id, e.numero, e.anio, e.actor, e.demandada, e.objeto, p.responsable, p.tipoProyectar " +
                "FROM expedientes e " +
                "JOIN aproyectar p ON e.id = p.id_expediente " +
                "WHERE p.tipoProyectar IN ('a resolver', 'a sentencia') AND p.fechaProyeccion IS NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes con estado 'A Resolver' o 'A Sentencia'.");
                return;
            }
            while (rs.next()) {
                String tipoProyectar = rs.getString("tipoProyectar").equalsIgnoreCase("a resolver") ? "Resolución" : "Sentencia";
                System.out.println(rs.getInt("id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tResponsable: " + (rs.getString("responsable").isEmpty() ? "No asignado" : rs.getString("responsable")));
                System.out.println("\tEstado: " + tipoProyectar);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes: " + e.getMessage());
        }
    }

    public static void marcarExpedienteComoProyectado(Connection conexion, Scanner scanner) {
        String sql = "SELECT e.id, e.numero, e.anio, e.actor, e.demandada, e.objeto, p.responsable, p.tipoProyectar " +
                "FROM expedientes e " +
                "JOIN aproyectar p ON e.id = p.id_expediente " +
                "WHERE p.tipoProyectar IN ('a resolver', 'a sentencia')";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes con estado 'A Resolver' o 'A Sentencia'.");
                return;
            }
            while (rs.next()) {
                String tipoProyectar = rs.getString("tipoProyectar").equalsIgnoreCase("a resolver") ? "Resolución" : "Sentencia";
                String responsable = rs.getString("responsable");
                System.out.println(rs.getInt("id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tEstado: " + tipoProyectar);
                System.out.println("\tResponsable: " + (responsable.isEmpty() ? "No asignado" : responsable));
            }

            System.out.print("Ingrese el ID del expediente a marcar como proyectado (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            String responsable = null;
            String responsableSql = "SELECT responsable FROM aproyectar WHERE id_expediente = ?";
            try (PreparedStatement pstmtResp = conexion.prepareStatement(responsableSql)) {
                pstmtResp.setInt(1, idExpediente);
                ResultSet rsResp = pstmtResp.executeQuery();

                if (rsResp.next() && rsResp.getString("responsable").isEmpty()) {
                    System.out.print("Ingrese el nombre del responsable: ");
                    responsable = scanner.nextLine();
                }
            }

            Date fechaProyeccion;
            while (true) {
                System.out.print("Ingrese la fecha de proyección (DD-MM-YYYY) (o 'Esc' para volver): ");
                String input = scanner.nextLine();
                try {
                    fechaProyeccion = new Date(dateFormat.parse(input).getTime());
                    if (fechaProyeccion.after(new Date(System.currentTimeMillis()))) {
                        System.out.println("La fecha de proyección debe ser igual o previa al día actual.");
                    } else {
                        break;
                    }
                } catch (ParseException e) {
                    System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
                }
            }

            String updateSql = "UPDATE aproyectar SET responsable = COALESCE(?, responsable), fechaProyeccion = ? WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(updateSql)) {
                pstmt.setString(1, responsable);
                pstmt.setDate(2, fechaProyeccion);
                pstmt.setInt(3, idExpediente);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Expediente marcado como proyectado exitosamente.");
                } else {
                    System.out.println("Expediente no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void listarExpedientesProyectados(Connection conexion) {
        String sql = "SELECT e.id, e.numero, e.anio, e.actor, e.demandada, e.objeto, p.responsable, p.fechaProyeccion, p.tipoProyectar " +
                "FROM expedientes e " +
                "JOIN aproyectar p ON e.id = p.id_expediente " +
                "WHERE p.fechaProyeccion IS NOT NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes proyectados.");
                return;
            }
            while (rs.next()) {
                String estadoMostrar = rs.getString("tipoProyectar").equalsIgnoreCase("a resolver") ? "Resolución" : "Sentencia";
                System.out.println(rs.getInt("id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tResponsable: " + rs.getString("responsable"));
                System.out.println("\tEstado: " + estadoMostrar);
                System.out.println("\tFecha de Proyección: " + dateFormat.format(rs.getDate("fechaProyeccion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes proyectados: " + e.getMessage());
        }
    }

    public static void asignarResponsable(Connection conexion, Scanner scanner) {
        try {
            listarExpedientesAResolverOSentencia(conexion);
            System.out.print("Ingrese el ID del expediente para asignar un responsable (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el nombre del responsable: ");
            String responsable = scanner.nextLine();

            String updateSql = "UPDATE aproyectar SET responsable = ? WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(updateSql)) {
                pstmt.setString(1, responsable);
                pstmt.setInt(2, idExpediente);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Responsable asignado exitosamente.");
                } else {
                    System.out.println("Expediente no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al asignar responsable: " + e.getMessage());
        }
    }
}
