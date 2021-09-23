package Controlador;

import javax.swing.SwingUtilities;
import Modelo.RoundRobin;
import Vista.Vista;
import java.lang.reflect.InvocationTargetException;

public class Controlador {
	private static Vista vista;
	private static RoundRobin modelo;
	
	public Controlador(int quantum, int periodoReloj) {
		modelo = new RoundRobin(quantum, periodoReloj, this);
		creacionDeVista(modelo, true);
	}
	
	public Controlador(int quantum) {
		modelo = new RoundRobin(quantum, this);
		creacionDeVista(modelo, false);
	}
	
	public void mostrarListaDeProcesos(String lista) {
		vista.enlistarProcesosActivos(lista);
	}
	
	private void creacionDeVista(RoundRobin modelo, boolean relojAutomatico) {
		// Se crea la ventana.
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					vista = new Vista(modelo, relojAutomatico);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}