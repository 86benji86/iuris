import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static List<Expediente> expedientes = new ArrayList<>();
    private static List<Consulta> consultas = new ArrayList<>();
    private static List<Criterio> criterios = new ArrayList<>();
    private static List<AProyectar> aProyectarList = new ArrayList<>();
    private static List<Apelado> apeladosList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) {
        while (true) {
            System.out.println("Sistema de Gestión de Expedientes Judiciales");
            System.out.println("1. Cargar Expediente");
            System.out.println("2. Listar Expedientes");
            System.out.println("3. Cargar Consulta");
            System.out.println("4. Listar Expedientes con Consultas");
            System.out.println("5. Responder Consulta");
            System.out.println("6. Listar Consultas con Respuestas");
            System.out.println("7. Cargar Estado Apelado");
            System.out.println("8. Listar Expedientes Apelados");
            System.out.println("9. Cargar Fecha de Elevación");
            System.out.println("10. Listar Expedientes con Fecha de Elevación");
            System.out.println("11. Asignar Estado A Resolver o A Sentencia");
            System.out.println("12. Listar Expedientes A Resolver o A Sentencia");
            System.out.println("13. Marcar Expediente como Proyectado");
            System.out.println("14. Listar Expedientes Proyectados");
            System.out.println("15. Salir");
            System.out.print("Seleccione una opción: (o escriba 'Esc' para volver):");
            int opcion = 0;

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Opción inválida. Por favor, ingrese un número.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    cargarExpediente();
                    break;
                case 2:
                    listarExpedientes();
                    break;
                case 3:
                    cargarConsulta();
                    break;
                case 4:
                    listarExpedientesConConsultas();
                    break;
                case 5:
                    responderConsulta();
                    break;
                case 6:
                    listarConsultasConRespuestas();
                    break;
                case 7:
                    cargarEstadoApelado();
                    break;
                case 8:
                    listarExpedientesApelados();
                    break;
                case 9:
                    cargarFechaElevacion();
                    break;
                case 10:
                    listarExpedientesConFechaDeElevacion();
                    break;
                case 11:
                    asignarEstadoAResolverOSentencia();
                    break;
                case 12:
                    listarExpedientesAResolverOSentencia();
                    break;
                case 13:
                    marcarExpedienteComoProyectado();
                    break;
                case 14:
                    listarExpedientesProyectados();
                    break;
                case 15:
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
    }

    /* Carga de expedientes, respetando parámetros propios de cada uno y respectivos tipos de datos. Para la carga de datos se devuelve un error si el tipo es erroneo. Si el usuario escribe Esc, vuelve al menú principal. */

    private static void cargarExpediente() {
        try {
            System.out.print("Ingrese el número del expediente: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int numero = Integer.parseInt(input);

            System.out.print("Ingrese el año del expediente: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int anio = Integer.parseInt(input);

            System.out.print("Ingrese el actor del expediente: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            String actor = input;

            System.out.print("Ingrese la demandada del expediente: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            String demandada = input;

            System.out.print("Ingrese el objeto del expediente: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            String objeto = input;

            Expediente expediente = new Expediente(numero, anio, actor, demandada, objeto);
            expedientes.add(expediente);
            System.out.println("Expediente cargado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un valor correcto.");
        }
    }

    /*Devuelve expedientes cargados, previo control de que exista alguno al menos*/

    private static void listarExpedientes() {
        if (expedientes.isEmpty()) {
            System.out.println("No hay expedientes cargados.");
        } else {
            expedientes.forEach(expediente -> System.out.println(expediente));
        }
    }

    /*Carga consultas a expedientes. Controla que ya haya sido cargado al menos uno */

    private static void cargarConsulta() {
        try {
            System.out.print("Ingrese el ID del expediente a consultar (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            System.out.print("Ingrese la consulta del expediente: ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            String consulta = input;
            String respuesta = null;

            Consulta consultaExpediente = new Consulta(idExpediente, consulta, respuesta);
            consultas.add(consultaExpediente);

            // Vincular consulta al expediente
            expedientes.stream()
                    .filter(exp -> exp.getIdExpediente() == idExpediente)
                    .findFirst()
                    .ifPresent(exp -> exp.agregarConsulta(consultaExpediente));

            System.out.println("Consulta cargada exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }

    /* Devuelve listado de expedientes con consultas. Si no hay, advierte eso. Si ya tuvieron respuesta, no aparecen en el listado */

    private static void listarExpedientesConConsultas() {
        boolean existenExpedientesConConsultas = false;

        for (Expediente exp : expedientes) {
            List<Consulta> consultasNoRespondidas = exp.getConsultasExpediente().stream()
                    .filter(consulta -> !consulta.isRespondida())
                    .collect(Collectors.toList());

            if (!consultasNoRespondidas.isEmpty()) {
                System.out.println(exp);
                consultasNoRespondidas.forEach(con -> System.out.println("\t" + con));
                existenExpedientesConConsultas = true;
            }
        }

        if (!existenExpedientesConConsultas) {
            System.out.println("No hay expedientes con consultas pendientes.");
        }
    }

    /* Permite responder consultas, previo control de que haya alguna cargada */

    private static void responderConsulta() {
        System.out.println("Expedientes con consultas:");
        listarExpedientesConConsultas();

        try {
            System.out.print("Ingrese el ID de la consulta a responder (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idConsulta = Integer.parseInt(input);

            Consulta consulta = consultas.stream()
                    .filter(con -> con.getIdConsulta() == idConsulta)
                    .findFirst()
                    .orElse(null);

            if (consulta != null) {
                System.out.println("Consulta seleccionada: " + consulta);
                System.out.print("Ingrese la respuesta a la consulta: ");
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("Esc")) return;
                String respuesta = input;
                consulta.setConsultaRespuesta(respuesta);
                System.out.println("Respuesta agregada exitosamente.");
            } else {
                System.out.println("Consulta no encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }

    /* Muestra un listado de consultas respondidas */

    private static void listarConsultasConRespuestas() {
        boolean existenConsultasConRespuestas = false;

        for (Consulta consulta : consultas) {
            if (consulta.isRespondida()) {
                Expediente exp = expedientes.stream()
                        .filter(e -> e.getIdExpediente() == consulta.getIdExpediente())
                        .findFirst()
                        .orElse(null);

                if (exp != null) {
                    System.out.println(exp.getIdExpediente() + " - " +
                            exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                            exp.getActorExpediente() + " c/ " +
                            exp.getDemandadaExpediente() + " s/ " +
                            exp.getObjetoExpediente() + " - Consulta: " +
                            consulta.getConsultaExpediente() + " Respuesta: " + consulta.getConsultaRespuesta());
                    existenConsultasConRespuestas = true;
                }
            }
        }

        if (!existenConsultasConRespuestas) {
            System.out.println("No hay consultas con respuestas.");
        }
    }

    /* Cargar expedientes como apelados. Controla estado y fecha */

    private static void cargarEstadoApelado() {
        if (expedientes.isEmpty()) {
            System.out.println("No hay expedientes cargados. Primero cargue un expediente.");
            return;
        }

        try {
            System.out.print("Ingrese el ID del expediente relacionado (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            System.out.print("El expediente fue notificado (Si/No) (o 'Esc' para volver): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            boolean notificado = input.equalsIgnoreCase("si");

            System.out.print("Ingrese la fecha de apelación (DD-MM-YYYY) (o 'Esc' para volver): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            Date fechaApelacion = dateFormat.parse(input);

            Apelado apelado = new Apelado(idExpediente, notificado, fechaApelacion, null);
            apeladosList.add(apelado);

            System.out.println("Estado apelado cargado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
        }
    }

    private static void listarExpedientesApelados() {
        if (apeladosList.isEmpty()) {
            System.out.println("No hay expedientes apelados.");
            return;
        }

        for (Apelado apelado : apeladosList) {
            Expediente exp = expedientes.stream()
                    .filter(e -> e.getIdExpediente() == apelado.getIdExpediente())
                    .findFirst()
                    .orElse(null);

            if (exp != null) {
                System.out.println(exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                        exp.getActorExpediente() + " c/ " + exp.getDemandadaExpediente() + " s/ " +
                        exp.getObjetoExpediente() + " - Notificado: " + (apelado.isApeladoNotificado() ? "true" : "false") + " - " +
                        "Fecha de notificado: " + dateFormat.format(apelado.getFechaApelado()));
            }
        }
    }

    /* Carga fecha de elevación de expedientes apelados. Controla que estén notificados y que la fecha sea posterior a la apelación */

    private static void cargarFechaElevacion() {
        if (apeladosList.isEmpty() || apeladosList.stream().noneMatch(Apelado::isApeladoNotificado)) {
            System.out.println("No hay expedientes apelados notificados. Primero cargue un expediente apelado y notifíquelo.");
            return;
        }

        try {
            System.out.print("Ingrese el ID del expediente apelado (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            Apelado apelado = apeladosList.stream()
                    .filter(a -> a.getIdExpediente() == idExpediente && a.isApeladoNotificado())
                    .findFirst()
                    .orElse(null);

            if (apelado == null) {
                System.out.println("No se encontró un expediente apelado notificado con ese ID.");
                return;
            }

            System.out.print("Ingrese la fecha de elevación (DD-MM-YYYY) (o 'Esc' para volver): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            Date fechaElevacion = dateFormat.parse(input);

            if (fechaElevacion.before(apelado.getFechaApelado())) {
                System.out.println("La fecha de elevación debe ser posterior a la fecha de notificación de la apelación.");
                return;
            }

            apelado.setFechaElevado(fechaElevacion);
            System.out.println("Fecha de elevación cargada exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
        }
    }

    private static void listarExpedientesConFechaDeElevacion() {
        boolean existenExpedientesConFechaDeElevacion = false;

        for (Apelado apelado : apeladosList) {
            if (apelado.getFechaElevado() != null) {
                Expediente exp = expedientes.stream()
                        .filter(e -> e.getIdExpediente() == apelado.getIdExpediente())
                        .findFirst()
                        .orElse(null);

                if (exp != null) {
                    System.out.println(exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                            exp.getActorExpediente() + " c/ " + exp.getDemandadaExpediente() + " s/ " +
                            exp.getObjetoExpediente() + " - Fecha de elevación: " + dateFormat.format(apelado.getFechaElevado()));
                    existenExpedientesConFechaDeElevacion = true;
                }
            }
        }

        if (!existenExpedientesConFechaDeElevacion) {
            System.out.println("No hay expedientes con fecha de elevación cargada.");
        }
    }

    /* Asigna expedientes a Resolver o a Sentencia (confección de proyectos) y responsable a cargo */

    private static void asignarEstadoAResolverOSentencia() {
        if (expedientes.isEmpty()) {
            System.out.println("No hay expedientes cargados. Primero cargue un expediente.");
            return;
        }

        try {
            System.out.print("Ingrese el ID del expediente relacionado (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            System.out.print("Ingrese el nombre del responsable (o 'Esc' para volver): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            String responsable = input;

            AProyectar aProyectar = new AProyectar(idExpediente, responsable);
            aProyectarList.add(aProyectar);

            System.out.println("Estado a resolver/a sentencia asignado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }

    /* Listado de expedientes a proyectar y Responsable asignado*/

    private static void listarExpedientesAResolverOSentencia() {
        if (aProyectarList.isEmpty()) {
            System.out.println("No hay expedientes con estado 'A Resolver' o 'A Sentencia'.");
            return;
        }

        for (AProyectar aProyectar : aProyectarList) {
            Expediente exp = expedientes.stream()
                    .filter(e -> e.getIdExpediente() == aProyectar.getIdExpediente())
                    .findFirst()
                    .orElse(null);

            if (exp != null) {
                System.out.println(exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                        exp.getActorExpediente() + " c/ " + exp.getDemandadaExpediente() + " s/ " +
                        exp.getObjetoExpediente() + " - Responsable: " + aProyectar.getAProyectarResponsable());
            }
        }
    }


    private static void marcarExpedienteComoProyectado() {
        List<AProyectar> aResolverOSentenciaList = aProyectarList.stream()
                .filter(a -> a != null)
                .collect(Collectors.toList());

        if (aResolverOSentenciaList.isEmpty()) {
            System.out.println("No hay expedientes con estado 'A Resolver' o 'A Sentencia'.");
            return;
        }

        System.out.println("Expedientes con estado 'A Resolver' o 'A Sentencia':");
        for (AProyectar aProyectar : aResolverOSentenciaList) {
            Expediente exp = expedientes.stream()
                    .filter(e -> e.getIdExpediente() == aProyectar.getIdExpediente())
                    .findFirst()
                    .orElse(null);

            if (exp != null) {
                System.out.println(exp.getIdExpediente() + " - " + exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                        exp.getActorExpediente() + " c/ " + exp.getDemandadaExpediente() + " s/ " + exp.getObjetoExpediente() +
                        " - Responsable: " + aProyectar.getAProyectarResponsable());
            }
        }

        try {
            System.out.print("Ingrese el ID del expediente a marcar como proyectado (o 'Esc' para volver): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            int idExpediente = Integer.parseInt(input);

            System.out.print("Ingrese la fecha de proyección (DD-MM-YYYY) (o 'Esc' para volver): ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Esc")) return;
            Date fechaProyeccion = dateFormat.parse(input);

            AProyectar aProyectar = aProyectarList.stream()
                    .filter(a -> a.getIdExpediente() == idExpediente)
                    .findFirst()
                    .orElse(null);

            if (aProyectar != null) {
                System.out.println("Expediente marcado como proyectado exitosamente en la fecha " + dateFormat.format(fechaProyeccion));
            } else {
                System.out.println("No se encontró el expediente con ese ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
        } catch (ParseException e) {
            System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
        }
    }

    private static void listarExpedientesProyectados() {
        if (aProyectarList.isEmpty()) {
            System.out.println("No hay expedientes proyectados.");
            return;
        }

        for (AProyectar aProyectar : aProyectarList) {
            Expediente exp = expedientes.stream()
                    .filter(e -> e.getIdExpediente() == aProyectar.getIdExpediente())
                    .findFirst()
                    .orElse(null);

            if (exp != null) {
                System.out.println(exp.getNumeroExpediente() + "/" + exp.getAnioExpediente() + " - " +
                        exp.getActorExpediente() + " c/ " + exp.getDemandadaExpediente() + " s/ " +
                        exp.getObjetoExpediente() + " - Responsable: " + aProyectar.getAProyectarResponsable());
            }
        }
    }
}
