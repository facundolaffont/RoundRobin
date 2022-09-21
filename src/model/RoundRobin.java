package model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import controller.Controller;

public class RoundRobin {
	
	/* Public members. */
	
	public RoundRobin(int quantum, Controller controller) {
		init(quantum, controller);
		_clockFreq = -1;
	}
	
	public RoundRobin(int quantum, int clockFreq, Controller controller) {	
		init(quantum, controller);
		_clockFreq = clockFreq;
		
		// Creation of timer that'll emulate the clock.
		createTimer(quantum, clockFreq);
	}
	
	public float getAverageReturnTime() {
		return _averageReturnTime;
	}
	
	public float getAverageWaitingTime() {
		return _averageWaitingTime;
	}
	
	public int getQuantum() {
		return _quantum;
	}
	
	public int getClockFreq() {
		return _clockFreq;
	}
	
	public void addToWaitingQueue(String description, int requiredTime) {
		_waitQueue.add(new Process(_nextID++, description, requiredTime, _clockCounter));
	}
	
	public void cycle() {
		Process newProcess;
			
		// There are processes in waiting queue.
		if(!_waitQueue.isEmpty()) {
			
			// While queue is not empty, elements from the waiting queue to the
			// active processes list.
			while(!_waitQueue.isEmpty()) {
				newProcess = _waitQueue.poll();
				_activeProcesses.add(newProcess);
				_eventList.add(newProcess.getID() + "." + _clockCounter);
			}
			
		// There are no processes in waiting queue, and there are active processes.
		} else if(!_activeProcesses.isEmpty()) {
			int adder = 0;
			for(Process p: _activeProcesses) adder += p.getTiempoRestante();
			
			if(adder > 0)
				for(Process p: _activeProcesses) {
					
					// Gives the corresponding attention to every process.
					int remainingTime = p.getTiempoRestante();
					if(remainingTime > _quantum) {
						_clockCounter += _quantum;
						p.setTiempoRestante(remainingTime - _quantum);
					} else {
						_clockCounter += p.getTiempoRestante();
						p.setTiempoRestante(0);
						p.setTiempoFinal(_clockCounter);
					}
					
					// Updates the event list.
					_eventList.add(p.getID() + "." + _clockCounter);
				}
			
			ArrayList<Process> processesToEliminate = new ArrayList<Process>();
			adder = 0;
			for(Process p: _activeProcesses) {
				int remainingTime = p.getTiempoRestante();
				if(remainingTime == 0) {

					// Adds times to return and waiting lists.
					_returnTimesList.add(p.calculateReturnTime());
					_waitingTimesList.add(p.calculateReturnTime() - p.getTiempoRequerido());

					// Adds process to list of to be eliminated.
					processesToEliminate.add(p);
				}
			}
			
			// Si hay algún proceso que se debe eliminar:
			if(!processesToEliminate.isEmpty()) {
				
				// Elimino los procesos de la lista de activos.
				_activeProcesses.removeAll(processesToEliminate);
				
				// Calculo el tiempo promedio de espera de los procesos ya finalizados.
				adder = 0;
				for(int value: _waitingTimesList) adder += value;
				_averageWaitingTime = ((float) adder / _waitingTimesList.size());
			

				// Calculo el tiempo promedio de retorno de los procesos ya finalizados.
				adder = 0;
				for(int value: _returnTimesList) adder += value;
				_averageReturnTime = ((float) adder / _returnTimesList.size());
			}						
			
		}
		
		
		/* Se enlistan los procesos activos */
		
		String lista = "";
		
		lista += "ID\tTiempo\tDescripción";
		lista += "\n--\t------\t-----------\n";
		for(Process p: _activeProcesses) {
			lista += 
				"\n" +
				p.getID() + "\t" +
				p.getTiempoRestante() + "\t" +
				p.getDescripcion()
			;
		}
		
		lista += "\n";
		
		if(_averageReturnTime > -1) {
			lista += "\nTiempo promedio de retorno: " + _averageReturnTime;
			lista += "\nTiempo promedio de espera: " + _averageWaitingTime;
		}
		
		_controller.mostrarListaDeProcesos(lista);
	}
	
	public void salirConError() {
		System.exit(1);
	}
	
	public void salirSinError() {
		System.exit(0);
	}
	
	
	/* Private members. */

	private ArrayList<Process> _activeProcesses;
	private ConcurrentLinkedQueue<Process> _waitQueue;
	private ArrayList<String> _eventList;
	private float _averageReturnTime;
	private float _averageWaitingTime;
	private int _nextID;
	private int _clockCounter;
	private int _quantum;
	private int _clockFreq;
	private Controller _controller;
	private ArrayList<Integer> _waitingTimesList;
	private ArrayList<Integer> _returnTimesList;
	
	private void init(int quantum, Controller controller) {
		_controller = controller;
		_activeProcesses = new ArrayList<Process>();
		_waitQueue = new ConcurrentLinkedQueue<Process>();
		_eventList = new ArrayList<String>();
		_waitingTimesList = new ArrayList<Integer>();
		_returnTimesList = new ArrayList<Integer>();
		_averageReturnTime = -1;
		_averageWaitingTime = -1;
		_nextID = 0;
		_clockCounter = 0;
		_quantum = quantum;
	}
	
	private void createTimer(int quantum, int clockFreq) {
		Timer timer = new Timer();
		timer.schedule(
			new TimerTask() {
				@Override
				public void run() {
					cycle();
				}
			}, clockFreq, clockFreq // Initial clock time, and time between changes, in milliseconds.
		);
	}
}
