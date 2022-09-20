package model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import controller.Controller;

public class RoundRobin {
	
	/* Métodos públicos */
	
	public RoundRobin(int quantum, Controller controlador) {
		inicializacion(quantum, controlador);
		frecuenciaReloj = -1;
	}
	
	public RoundRobin(int quantum, int frecuenciaReloj, Controller controlador) {	
		inicializacion(quantum, controlador);
		this.frecuenciaReloj = frecuenciaReloj;
		
		// Creación del timer que emulará el reloj.
		crearTimer(quantum, frecuenciaReloj);
	}
	
	public float getTiempoPromedioDeRetorno() {
		return tiempoPromedioDeRetorno;
	}
	
	public float getTiempoPromedioDeEspera() {
		return tiempoPromedioDeEspera;
	}
	
	public int getQuantum() {
		return quantum;
	}
	
	public int getFrecuenciaReloj() {
		return frecuenciaReloj;
	}
	
	public void agregoAColaDeEspera(String descripcion, int tiempoRequeridoNumerico) {
		colaDeEspera.add(new Process(proximoID++, descripcion, tiempoRequeridoNumerico, contadorReloj));
	}
	
	public void realizarCiclo() {
		Process procesoNuevo;
			
		if(!colaDeEspera.isEmpty()) {
		// Hay procesos en cola de espera.
			
			// Mientras la cola no esté vacía, pasa los elementos de la cola de
			// espera a la lista de procesos activos.
			while(!colaDeEspera.isEmpty()) {
				procesoNuevo = colaDeEspera.poll();
				procesosActivos.add(procesoNuevo);
				listaEventos.add(procesoNuevo.getID() + "." + contadorReloj);
			}
			
		} else if(!procesosActivos.isEmpty()) {
		// No hay procesos en cola de espera, y hay procesos activos.
			
			int sumador = 0;
			for(Process p: procesosActivos) sumador += p.getTiempoRestante();
			
			if(sumador > 0)
				for(Process p: procesosActivos) {
					
					// Brinda la correspondiente atención a cada proceso.
					int tiempoRestante = p.getTiempoRestante();
					if(tiempoRestante > quantum) {
						contadorReloj += quantum;
						p.setTiempoRestante(tiempoRestante - quantum);
					} else {
						contadorReloj += p.getTiempoRestante();
						p.setTiempoRestante(0);
						p.setTiempoFinal(contadorReloj);
					}
					
					// Actualiza la lista de eventos.
					listaEventos.add(p.getID() + "." + contadorReloj);
				}
			
			ArrayList<Process> procesosAEliminar = new ArrayList<Process>();
			sumador = 0;
			for(Process p: procesosActivos) {
				int tiempoRestante = p.getTiempoRestante();
				if(tiempoRestante == 0) {

					// Agrega los tiempos a las listas de retorno y espera.
					listaTiemposRetorno.add(p.calcularTiempoRetorno());
					listaTiemposEspera.add(p.calcularTiempoRetorno() - p.getTiempoRequerido());

					// Se agrega a la lista de procesos que serán eliminados de la lista de procesos activos.
					procesosAEliminar.add(p);
				}
			}
			
			// Si hay algún proceso que se debe eliminar:
			if(!procesosAEliminar.isEmpty()) {
				
				// Elimino los procesos de la lista de activos.
				procesosActivos.removeAll(procesosAEliminar);
				
				// Calculo el tiempo promedio de espera de los procesos ya finalizados.
				sumador = 0;
				for(int valor: listaTiemposEspera) sumador += valor;
				tiempoPromedioDeEspera = ((float) sumador / listaTiemposEspera.size());
			

				// Calculo el tiempo promedio de retorno de los procesos ya finalizados.
				sumador = 0;
				for(int valor: listaTiemposRetorno) sumador += valor;
				tiempoPromedioDeRetorno = ((float) sumador / listaTiemposRetorno.size());
			}						
			
		}
		
		
		/* Se enlistan los procesos activos */
		
		String lista = "";
		
		lista += "ID\tTiempo\tDescripción";
		lista += "\n--\t------\t-----------\n";
		for(Process p: procesosActivos) {
			lista += 
				"\n" +
				p.getID() + "\t" +
				p.getTiempoRestante() + "\t" +
				p.getDescripcion()
			;
		}
		
		lista += "\n";
		
		if(tiempoPromedioDeRetorno > -1) {
			lista += "\nTiempo promedio de retorno: " + tiempoPromedioDeRetorno;
			lista += "\nTiempo promedio de espera: " + tiempoPromedioDeEspera;
		}
		
		controlador.mostrarListaDeProcesos(lista);
	}
	
	public void salirConError() {
		System.exit(1);
	}
	
	public void salirSinError() {
		System.exit(0);
	}
	
	
	/* Métodos privados */

	private ArrayList<Process> procesosActivos;
	private ConcurrentLinkedQueue<Process> colaDeEspera;
	private ArrayList<String> listaEventos;
	private float tiempoPromedioDeRetorno;
	private float tiempoPromedioDeEspera;
	private int proximoID;
	private int contadorReloj;
	private int quantum;
	private int frecuenciaReloj;
	private Controller controlador;
	private ArrayList<Integer> listaTiemposEspera;
	private ArrayList<Integer> listaTiemposRetorno;
	
	private void inicializacion(int quantum, Controller controlador) {
		// Vinculación con el controlador.
		this.controlador = controlador;
		
		// Configuración de propiedades iniciales del RoundRobin.
		procesosActivos = new ArrayList<Process>();
		colaDeEspera = new ConcurrentLinkedQueue<Process>();
		listaEventos = new ArrayList<String>();
		listaTiemposEspera = new ArrayList<Integer>();
		listaTiemposRetorno = new ArrayList<Integer>();
		tiempoPromedioDeRetorno = -1;
		tiempoPromedioDeEspera = -1;
		proximoID = 0;
		contadorReloj = 0;
		this.quantum = quantum;
	}
	
	private void crearTimer(int quantum, int periodoReloj) {
		Timer timer = new Timer();
		timer.schedule(
			new TimerTask() {
				@Override
				public void run() {
					realizarCiclo();
				}
			}, periodoReloj, periodoReloj // Tiempo de inicio del reloj, y tiempo entre pulsaciones, en milisegundos.
		);
	}
}
