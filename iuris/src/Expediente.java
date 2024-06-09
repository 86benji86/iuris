import java.util.ArrayList;
import java.util.List;

public class Expediente {
    private static int contadorExpedientes = 1; // Contador estático para IDs
    private int idExpediente;
    private int numeroExpediente;
    private int anioExpediente;
    private String actorExpediente;
    private String demandadaExpediente;
    private String objetoExpediente;
    private List<Consulta> consultasExpediente;

    public Expediente(int numeroExpediente, int anioExpediente, String actorExpediente, String demandadaExpediente, String objetoExpediente) {
        this.idExpediente = contadorExpedientes++;
        this.numeroExpediente = numeroExpediente;
        this.anioExpediente = anioExpediente;
        this.actorExpediente = actorExpediente;
        this.demandadaExpediente = demandadaExpediente;
        this.objetoExpediente = objetoExpediente;
        this.consultasExpediente = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdExpediente() {
        return idExpediente;
    }

    public int getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(int numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getAnioExpediente() {
        return anioExpediente;
    }

    public void setAnioExpediente(int anioExpediente) {
        this.anioExpediente = anioExpediente;
    }

    public String getActorExpediente() {
        return actorExpediente;
    }

    public void setActorExpediente(String actorExpediente) {
        this.actorExpediente = actorExpediente;
    }

    public String getDemandadaExpediente() {
        return demandadaExpediente;
    }

    public void setDemandadaExpediente(String demandadaExpediente) {
        this.demandadaExpediente = demandadaExpediente;
    }

    public String getObjetoExpediente() {
        return objetoExpediente;
    }

    public void setObjetoExpediente(String objetoExpediente) {
        this.objetoExpediente = objetoExpediente;
    }

    public List<Consulta> getConsultasExpediente() {
        return consultasExpediente;
    }

    public void agregarConsulta(Consulta consulta) {
        this.consultasExpediente.add(consulta);
    }

    @Override
    public String toString() {
        return idExpediente + " - " + numeroExpediente + "/" + anioExpediente + " - " + actorExpediente + " c/ " + demandadaExpediente + " s/ " + objetoExpediente;
    }
}
