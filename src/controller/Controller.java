package controller;

import javax.swing.SwingUtilities;

import model.RoundRobin;
import view.View;

import java.lang.reflect.InvocationTargetException;

public class Controller {
	

	/* Public members. */

	public Controller(int quantum, int clockPeriod) {
		_model = new RoundRobin(quantum, clockPeriod, this);
		creacionDeVista(_model, true);
	}
	
	public Controller(int quantum) {
		_model = new RoundRobin(quantum, this);
		creacionDeVista(_model, false);
	}
	
	public void mostrarListaDeProcesos(String lista) {
		_view.enlistarProcesosActivos(lista);
	}
	

	/* Private members. */

	private static View _view;
	private static RoundRobin _model;
	
	private void creacionDeVista(RoundRobin model, boolean automaticClock) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					_view = new View(model, automaticClock);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}