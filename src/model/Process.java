package model;

public class Process {
	private int ID;
	private String descripcion;
	private int tiempoRequerido;
	private int tiempoRestante;
	private int tiempoInicial;
	private int tiempoFinal;
	
	public Process(int ID, String descripcion, int tiempoRequerido, int relojActual) {
		this.ID = ID;
		this.descripcion = descripcion;
		this.tiempoRequerido = tiempoRequerido;
		tiempoRestante = tiempoRequerido;
		this.tiempoInicial = relojActual;
		tiempoFinal = -1;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public int getTiempoRequerido() {
		return tiempoRequerido;
	}
	
	public int getTiempoRestante() {
		return tiempoRestante;
	}
	
	public void setTiempoRestante(int nuevoTiempo) {
		tiempoRestante = nuevoTiempo;
	}
	
	public void setTiempoFinal(int tiempoFinal) {
		this.tiempoFinal = tiempoFinal;
	}
	
	public int calculateReturnTime() {
		return tiempoFinal == -1 ? -1 : tiempoFinal - tiempoInicial;
	}
}