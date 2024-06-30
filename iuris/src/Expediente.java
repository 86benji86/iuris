import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/* Clase Expedientes, gestiona la carga, listado y borrado de estos. El resto de las clases relaciona con instancias de esta clase */

public class Expediente {
    private int id;
    private int numero;
    private int anio;
    private String actor;
    private String demandada;
    private String objeto;

    /* Constructor */
    public Expediente(int id, int numero, int anio, String actor, String demandada, String objeto) {
        this.id = id;
        this.numero = numero;
        this.anio = anio;
        this.actor = actor;
        this.demandada = demandada;
        this.objeto = objeto;
    }

    /* Metodos para gestionar cada expediente: carga, listado, borrado */

    public static void cargarExpediente(Connection conexion, Scanner scanner) {
        try {
            System.out.print("Ingrese el número del expediente: ");
            int numero = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el año del expediente: ");
            int anio = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el actor del expediente: ");
            String actor = scanner.nextLine();

            System.out.print("Ingrese la demandada del expediente: ");
            String demandada = scanner.nextLine();

            System.out.print("Ingrese el objeto del expediente: ");
            String objeto = scanner.nextLine();

            String sql = "INSERT INTO expedientes (numero, anio, actor, demandada, objeto) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, numero);
                pstmt.setInt(2, anio);
                pstmt.setString(3, actor);
                pstmt.setString(4, demandada);
                pstmt.setString(5, objeto);
                pstmt.executeUpdate();
                System.out.println("Expediente cargado exitosamente.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al cargar el expediente: " + e.getMessage());
        }
    }

    public static void listarExpedientes(Connection conexion) {
        String sql = "SELECT * FROM expedientes";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes cargados.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes: " + e.getMessage());
        }
    }

    public static void borrarExpediente(Connection conexion, Scanner scanner) {
        listarExpedientes(conexion);
        try {
            System.out.print("Ingrese el ID del expediente a borrar (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            String relacionados = verificarRegistrosRelacionados(conexion, idExpediente);
            if (!relacionados.isEmpty()) {
                if (relacionados.contains("apelados")) {
                    System.out.print("El expediente tiene registros relacionados en apelados. ¿Desea borrar ambos? (Si/No): ");
                    String confirmacion = scanner.nextLine();
                    if (!confirmacion.equalsIgnoreCase("Si")) {
                        System.out.println("Operación cancelada.");
                        return;
                    }
                } else {
                    System.out.println("No se puede borrar el expediente porque tiene registros relacionados: " + relacionados);
                    return;
                }
            }

            // Controles previo a borrar, por integridad de la BD

            // Eliminar registros relacionados en 'aproyectar'
            String sqlProyectos = "DELETE FROM aproyectar WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sqlProyectos)) {
                pstmt.setInt(1, idExpediente);
                pstmt.executeUpdate();
            }

            // Eliminar registros relacionados en 'apelados'
            String sqlApelados = "DELETE FROM apelados WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sqlApelados)) {
                pstmt.setInt(1, idExpediente);
                pstmt.executeUpdate();
            }

            // Eliminar registros relacionados en 'consultas'
            String sqlConsultas = "DELETE FROM consultas WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sqlConsultas)) {
                pstmt.setInt(1, idExpediente);
                pstmt.executeUpdate();
            }

            // Eliminar el expediente
            String sqlExpediente = "DELETE FROM expedientes WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sqlExpediente)) {
                pstmt.setInt(1, idExpediente);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Expediente y registros relacionados borrados exitosamente.");
                } else {
                    System.out.println("Expediente no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al borrar el expediente: " + e.getMessage());
        }
    }

    private static String verificarRegistrosRelacionados(Connection conexion, int idExpediente) throws SQLException {
        StringBuilder relacionados = new StringBuilder();

        String sql = "SELECT COUNT(*) AS total FROM consultas WHERE id_expediente = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idExpediente);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                relacionados.append("consultas ");
            }
        }

        sql = "SELECT COUNT(*) AS total FROM apelados WHERE id_expediente = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idExpediente);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                relacionados.append("apelados ");
            }
        }

        sql = "SELECT tipoProyectar FROM aproyectar WHERE id_expediente = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idExpediente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tipoProyectar = rs.getString("tipoProyectar");
                relacionados.append(tipoProyectar.equalsIgnoreCase("a resolver") ? "a resolver " : "a sentencia ");
            }
        }

        return relacionados.toString().trim();
    }
}
