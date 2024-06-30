import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Apelado {
    private int id;
    private int idExpediente;
    private boolean notificado;
    private Date fechaApelacion;
    private Date fechaElevado;
    private boolean devueltos;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Apelado(int id, int idExpediente, boolean notificado, Date fechaApelacion, Date fechaElevado, boolean devueltos) {
        this.id = id;
        this.idExpediente = idExpediente;
        this.notificado = notificado;
        this.fechaApelacion = fechaApelacion;
        this.fechaElevado = fechaElevado;
        this.devueltos = devueltos;
    }

    /* Clase Apelado, gestiona fechas de apelaciones, notificaciones, elevaciones a cámara revisora y devolución */

    public static void cargarEstadoApelado(Connection conexion, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del expediente relacionado (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            boolean notificado = false;
            while (true) {
                System.out.print("El expediente fue notificado (Si/No) (o 'Esc' para volver): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Si")) {
                    notificado = true;
                    break;
                } else if (input.equalsIgnoreCase("No")) {
                    notificado = false;
                    break;
                } else {
                    System.out.println("Entrada inválida. Por favor, responda con 'Si' o 'No'.");
                }
            }

            Date fechaApelacion = null;
            while (true) {
                System.out.print("Ingrese la fecha de apelación (DD-MM-YYYY) (o 'Esc' para volver): ");
                String input = scanner.nextLine();
                try {
                    fechaApelacion = new Date(dateFormat.parse(input).getTime());
                    if (fechaApelacion.after(new Date(System.currentTimeMillis()))) {
                        System.out.println("La fecha de apelación debe ser igual o previa al día actual.");
                    } else {
                        break;
                    }
                } catch (ParseException e) {
                    System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
                }
            }

            String sql = "INSERT INTO apelados (id_expediente, notificado, fecha_apelacion, elevado, devueltos) VALUES (?, ?, ?, FALSE, FALSE)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idExpediente);
                pstmt.setBoolean(2, notificado);
                pstmt.setDate(3, fechaApelacion);
                pstmt.executeUpdate();
                System.out.println("Estado apelado cargado exitosamente.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al cargar el estado apelado: " + e.getMessage());
        }
    }

    public static void listarExpedientesApelados(Connection conexion) {
        String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, a.notificado, a.fecha_apelacion " +
                "FROM expedientes e " +
                "JOIN apelados a ON e.id = a.id_expediente " +
                "WHERE a.elevado = FALSE";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes apelados.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tNotificado: " + (rs.getBoolean("notificado") ? "Sí" : "No"));
                System.out.println("\tFecha de Apelación: " + dateFormat.format(rs.getDate("fecha_apelacion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes apelados: " + e.getMessage());
        }
    }

    public static void cambiarNotificacionExpedienteApelado(Connection conexion, Scanner scanner) {
        try {
            listarExpedientesApelados(conexion);
            System.out.print("Ingrese el ID del expediente a cambiar la notificación (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            boolean notificado = false;
            while (true) {
                System.out.print("El expediente fue notificado (Si/No) (o 'Esc' para volver): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Si")) {
                    notificado = true;
                    break;
                } else if (input.equalsIgnoreCase("No")) {
                    notificado = false;
                    break;
                } else {
                    System.out.println("Entrada inválida. Por favor, responda con 'Si' o 'No'.");
                }
            }

            String updateSql = "UPDATE apelados SET notificado = ? WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(updateSql)) {
                pstmt.setBoolean(1, notificado);
                pstmt.setInt(2, idExpediente);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Notificación del expediente actualizada exitosamente.");
                } else {
                    System.out.println("Expediente no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void cargarFechaElevacion(Connection conexion, Scanner scanner) {
        try {
            String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, a.fecha_apelacion " +
                    "FROM expedientes e " +
                    "JOIN apelados a ON e.id = a.id_expediente " +
                    "WHERE a.notificado = TRUE AND a.elevado = FALSE";
            try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No hay expedientes apelados notificados. Primero cargue un expediente apelado y notifíquelo.");
                    return;
                }
                while (rs.next()) {
                    System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                            rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                    System.out.println("\tFecha de Apelación: " + dateFormat.format(rs.getDate("fecha_apelacion")));
                }

                System.out.print("Ingrese el ID del expediente para cargar la fecha de elevación (o 'Esc' para volver): ");
                int idExpediente = Integer.parseInt(scanner.nextLine());

                Date fechaElevacion = null;
                while (true) {
                    System.out.print("Ingrese la fecha de elevación (DD-MM-YYYY) (o 'Esc' para volver): ");
                    String input = scanner.nextLine();
                    try {
                        fechaElevacion = new Date(dateFormat.parse(input).getTime());
                        if (fechaElevacion.after(new Date(System.currentTimeMillis()))) {
                            System.out.println("La fecha de elevación debe ser igual o previa al día actual.");
                        } else {
                            break;
                        }
                    } catch (ParseException e) {
                        System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
                    }
                }

                String updateSql = "UPDATE apelados SET fecha_elevado = ?, elevado = TRUE WHERE id_expediente = ?";
                try (PreparedStatement pstmt = conexion.prepareStatement(updateSql)) {
                    pstmt.setDate(1, fechaElevacion);
                    pstmt.setInt(2, idExpediente);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Fecha de elevación cargada exitosamente.");
                    } else {
                        System.out.println("Expediente no encontrado.");
                    }
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void listarExpedientesConFechaDeElevacion(Connection conexion) {
        String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, a.fecha_elevado " +
                "FROM expedientes e " +
                "JOIN apelados a ON e.id = a.id_expediente " +
                "WHERE a.elevado = TRUE";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes con fecha de elevación cargada.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tFecha de Elevación: " + dateFormat.format(rs.getDate("fecha_elevado")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes con fecha de elevación: " + e.getMessage());
        }
    }

    public static void recibirExpedienteConElevacion(Connection conexion, Scanner scanner) {
        String sql = "SELECT e.id AS expediente_id, e.numero, e.anio, e.actor, e.demandada, e.objeto, a.fecha_elevado " +
                "FROM expedientes e " +
                "JOIN apelados a ON e.id = a.id_expediente " +
                "WHERE a.elevado = TRUE AND a.devueltos = FALSE";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No hay expedientes elevados disponibles para recibir.");
                return;
            }
            while (rs.next()) {
                System.out.println(rs.getInt("expediente_id") + " - " + rs.getInt("numero") + "/" + rs.getInt("anio") + " - " +
                        rs.getString("actor") + " c/ " + rs.getString("demandada") + " s/ " + rs.getString("objeto"));
                System.out.println("\tFecha de Elevación: " + dateFormat.format(rs.getDate("fecha_elevado")));
            }

            System.out.print("Ingrese el ID del expediente a recibir (o 'Esc' para volver): ");
            int idExpediente = Integer.parseInt(scanner.nextLine());

            Date fechaDevuelto = null;
            while (true) {
                System.out.print("Ingrese la fecha de recepción (DD-MM-YYYY) (o 'Esc' para volver): ");
                String input = scanner.nextLine();
                try {
                    fechaDevuelto = new Date(dateFormat.parse(input).getTime());
                    if (fechaDevuelto.after(new Date(System.currentTimeMillis()))) {
                        System.out.println("La fecha de recepción debe ser igual o previa al día actual.");
                    } else {
                        break;
                    }
                } catch (ParseException e) {
                    System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
                }
            }

            String updateSql = "UPDATE apelados SET fecha_devuelto = ?, devueltos = TRUE WHERE id_expediente = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(updateSql)) {
                pstmt.setDate(1, fechaDevuelto);
                pstmt.setInt(2, idExpediente);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Expediente recibido exitosamente.");
                } else {
                    System.out.println("Expediente no encontrado.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
