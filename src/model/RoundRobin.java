package model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import controller.Controller;

public class RoundRobin {
	
	/* Public members. */
	
	public RoundRobin(int quantum, Controller controller) {
		_init(quantum, controller);
		_clockFreq = -1;
	}
	
	public RoundRobin(int quantum, int clockFreq, Controller controller) {	
		_init(quantum, controller);
		_clockFreq = clockFreq;
		
		// Creation of timer that'll emulate the clock.
		_createTimer(quantum, clockFreq);
	}
	
	public float getAverageReturnTime() { return _averageReturnTime; }
	
	public float getAverageWaitingTime() { return _averageWaitingTime; }
	
	public int getQuantum() { return _quantum; }
	
	public int getClockFreq() { return _clockFreq; }
	
	public void addToWaitingQueue(String description, int requiredTime) {
		_waitQueue.add(
			new Process(_nextID++, description, requiredTime, _clockCounter)
		);
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
			for(Process p: _activeProcesses) adder += p.getRemainingTime();
			
			if(adder > 0)
				for(Process p: _activeProcesses) {
					
					// Gives the corresponding attention to every process.
					int remainingTime = p.getRemainingTime();
					if(remainingTime > _quantum) {
						_clockCounter += _quantum;
						p.setRemainingTime(remainingTime - _quantum);
					} else {
						_clockCounter += p.getRemainingTime();
						p.setRemainingTime(0);
						p.setFinalTime(_clockCounter);
					}
					
					// Updates the event list.
					_eventList.add(p.getID() + "." + _clockCounter);
				}
			
			ArrayList<Process> processesToEliminate = new ArrayList<Process>();
			adder = 0;
			for(Process p: _activeProcesses) {
				int remainingTime = p.getRemainingTime();
				if(remainingTime == 0) {

					// Adds times to return and waiting lists.
					_returnTimesList.add(p.calculateReturnTime());
					_waitingTimesList.add(p.calculateReturnTime() - p.getRequiredTime());

					// Adds process to list of to be eliminated.
					processesToEliminate.add(p);
				}
			}
			
			// There are processes that have to be eliminated.
			if(!processesToEliminate.isEmpty()) {
				
				// Eliminates processes from the active processes list.
				_activeProcesses.removeAll(processesToEliminate);
				
				// Calculates the mean waiting time of already finalized processes.
				adder = 0;
				for(int value: _waitingTimesList) adder += value;
				_averageWaitingTime = ((float) adder / _waitingTimesList.size());
			
				// Calculates the mean return time of already finalized processes.
				adder = 0;
				for(int value: _returnTimesList) adder += value;
				_averageReturnTime = ((float) adder / _returnTimesList.size());
			}						
			
		}
		
		
		/* Active processes are enlisted. */
		
		String activeProcesses = 
			  "ID" + "\t" + "Time" + "\t" + "Description" +
			"\n--" + "\t" + "----" + "\t" + "-----------"
		;
		for(Process p: _activeProcesses) {
			activeProcesses += "\n" + p.getID() + "\t" + p.getRemainingTime() + "\t" + p.getDescription();
		}
		
		if(_averageReturnTime > -1) {
			activeProcesses += "\nMean return time: " + _averageReturnTime;
			activeProcesses += "\nMean waiting time: " + _averageWaitingTime;
		}
		
		_controller.showProcessesList(activeProcesses);
	}
	
	public void exitWithError() {
		System.exit(1);
	}
	
	public void exitWithoutError() {
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
	
	private void _init(int quantum, Controller controller) {
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
	
	private void _createTimer(int quantum, int clockFreq) {
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
