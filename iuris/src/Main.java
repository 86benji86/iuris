import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/iuris", "benji", "1234")) {
            while (true) {
                mostrarMenu();
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> Expediente.cargarExpediente(conexion, scanner);
                    case 2 -> Expediente.listarExpedientes(conexion);
                    case 3 -> Expediente.borrarExpediente(conexion, scanner);
                    case 4 -> Consulta.cargarConsulta(conexion, scanner);
                    case 5 -> Consulta.listarConsultas(conexion);
                    case 6 -> Consulta.borrarConsulta(conexion, scanner);
                    case 7 -> Consulta.responderConsulta(conexion, scanner);
                    case 8 -> Consulta.listarConsultasConRespuestas(conexion);
                    case 9 -> Apelado.cargarEstadoApelado(conexion, scanner);
                    case 10 -> Apelado.listarExpedientesApelados(conexion);
                    case 11 -> Apelado.cambiarNotificacionExpedienteApelado(conexion, scanner);
                    case 12 -> Apelado.cargarFechaElevacion(conexion, scanner);
                    case 13 -> Apelado.listarExpedientesConFechaDeElevacion(conexion);
                    case 14 -> Apelado.recibirExpedienteConElevacion(conexion, scanner);
                    case 15 -> AProyectar.asignarEstadoAResolverOSentencia(conexion, scanner);
                    case 16 -> AProyectar.listarExpedientesAResolverOSentencia(conexion);
                    case 17 -> AProyectar.marcarExpedienteComoProyectado(conexion, scanner);
                    case 18 -> AProyectar.listarExpedientesProyectados(conexion);
                    case 19 -> AProyectar.asignarResponsable(conexion, scanner);
                    case 20 -> Criterio.cargarCriterio(conexion, scanner);
                    case 21 -> Criterio.listarCriterios(conexion);
                    case 22 -> Criterio.borrarCriterio(conexion, scanner);
                    case 23 -> {
                        System.out.println("Saliendo del sistema...");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("Sistema de Gestión de Expedientes Judiciales");
        System.out.println("1. Cargar Expediente");
        System.out.println("2. Listar Expedientes");
        System.out.println("3. Borrar Expediente");
        System.out.println("4. Cargar Consulta");
        System.out.println("5. Listar Expedientes con Consultas");
        System.out.println("6. Borrar Consulta");
        System.out.println("7. Responder Consulta");
        System.out.println("8. Listar Consultas con Respuestas");
        System.out.println("9. Cargar Estado Apelado");
        System.out.println("10. Listar Expedientes Apelados");
        System.out.println("11. Cambiar notificación de Expedientes Apelados");
        System.out.println("12. Cargar Fecha de Elevación");
        System.out.println("13. Listar Expedientes con Fecha de Elevación");
        System.out.println("14. Recibir Expediente con Elevación");
        System.out.println("15. Asignar Estado A Resolver o A Sentencia");
        System.out.println("16. Listar Expedientes A Resolver o A Sentencia");
        System.out.println("17. Marcar Expediente como Proyectado");
        System.out.println("18. Listar Expedientes Proyectados");
        System.out.println("19. Asignar Responsable a Expediente A Resolver o A Sentencia");
        System.out.println("20. Cargar Criterio");
        System.out.println("21. Listar Criterios");
        System.out.println("22. Borrar Criterio");
        System.out.println("23. Salir");
        System.out.print("Seleccione una opción: ");
    }
}
