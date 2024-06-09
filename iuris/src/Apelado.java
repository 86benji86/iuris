import java.util.Date;

public class Apelado {
    private static int contadorApelados = 1; // Contador estático para IDs
    private int idApelado;
    private int idExpediente;
    private boolean apeladoNotificado;
    private Date fechaApelado;
    private Date fechaElevado;

    public Apelado(int idExpediente, boolean apeladoNotificado, Date fechaApelado, Date fechaElevado) {
        this.idApelado = contadorApelados++;
        this.idExpediente = idExpediente;
        this.apeladoNotificado = apeladoNotificado;
        this.fechaApelado = fechaApelado;
        this.fechaElevado = fechaElevado;
    }

    // Getters y Setters
    public int getIdApelado() {
        return idApelado;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public boolean isApeladoNotificado() {
        return apeladoNotificado;
    }

    public void setApeladoNotificado(boolean apeladoNotificado) {
        this.apeladoNotificado = apeladoNotificado;
    }

    public Date getFechaApelado() {
        return fechaApelado;
    }

    public void setFechaApelado(Date fechaApelado) {
        this.fechaApelado = fechaApelado;
    }

    public Date getFechaElevado() {
        return fechaElevado;
    }

    public void setFechaElevado(Date fechaElevado) {
        this.fechaElevado = fechaElevado;
    }

    @Override
    public String toString() {
        return idApelado + " - " + idExpediente + " - " + apeladoNotificado + " - " + fechaApelado + " - " + fechaElevado;
    }
}
