public class Consulta {
    private static int contadorConsultas = 1; // Contador estático para IDs
    private int idConsulta;
    private int idExpediente;
    private String consultaExpediente;
    private String consultaRespuesta;

    public Consulta(int idExpediente, String consultaExpediente, String consultaRespuesta) {
        this.idConsulta = contadorConsultas++;
        this.idExpediente = idExpediente;
        this.consultaExpediente = consultaExpediente;
        this.consultaRespuesta = consultaRespuesta;
    }

    // Getters y Setters
    public int getIdConsulta() {
        return idConsulta;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public String getConsultaExpediente() {
        return consultaExpediente;
    }

    public void setConsultaExpediente(String consultaExpediente) {
        this.consultaExpediente = consultaExpediente;
    }

    public String getConsultaRespuesta() {
        return consultaRespuesta;
    }

    public void setConsultaRespuesta(String consultaRespuesta) {
        this.consultaRespuesta = consultaRespuesta;
    }

    public boolean isRespondida() {
        return this.consultaRespuesta != null && !this.consultaRespuesta.isEmpty();
    }

    @Override
    public String toString() {
        return idConsulta + " - " + "Consulta: " + consultaExpediente;
    }
}
