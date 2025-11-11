package edu.oms.modelo;

public class UnidadMedida {
    private int idUnidad;
    private String nombre;
    private String abreviatura;
    private boolean activo;

    public UnidadMedida() {}

    public UnidadMedida(String nombre, String abreviatura) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.activo = true;
    }

    public UnidadMedida(int idUnidad, String nombre, String abreviatura, boolean activo) {
        this.idUnidad = idUnidad;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.activo = activo;
    }

    public int getIdUnidad() { return idUnidad; }
    public void setIdUnidad(int idUnidad) { this.idUnidad = idUnidad; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getAbreviatura() { return abreviatura; }
    public void setAbreviatura(String abreviatura) { this.abreviatura = abreviatura; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre + " (" + abreviatura + ")";
    }
}
