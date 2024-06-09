public class AProyectar {
    private static int contadorAProyectar = 1; // Contador estático para IDs
    private int idProyectar;
    private int idExpediente;
    private String aProyectarResponsable;

    public AProyectar(int idExpediente, String aProyectarResponsable) {
        this.idProyectar = contadorAProyectar++;
        this.idExpediente = idExpediente;
        this.aProyectarResponsable = aProyectarResponsable;
    }

    // Getters y Setters
    public int getIdProyectar() {
        return idProyectar;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public String getAProyectarResponsable() {
        return aProyectarResponsable;
    }

    public void setAProyectarResponsable(String aProyectarResponsable) {
        this.aProyectarResponsable = aProyectarResponsable;
    }

    @Override
    public String toString() {
        return idProyectar + " - " + idExpediente + " - " + aProyectarResponsable;
    }
}
