public class Criterio {
    private static int contadorCriterios = 1; // Contador estático para IDs
    private int idCriterio;
    private String criterioTema;

    public Criterio(String criterioTema) {
        this.idCriterio = contadorCriterios++;
        this.criterioTema = criterioTema;
    }

    // Getters y Setters
    public int getIdCriterio() {
        return idCriterio;
    }

    public String getCriterioTema() {
        return criterioTema;
    }

    public void setCriterioTema(String criterioTema) {
        this.criterioTema = criterioTema;
    }

    @Override
    public String toString() {
        return idCriterio + " - " + criterioTema;
    }
}
