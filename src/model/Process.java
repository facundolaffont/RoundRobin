package model;

public class Process {

	/* Public members. */

	public Process(int ID, String descripcion, int tiempoRequerido, int relojActual) {
		_ID = ID;
		_description = descripcion;
		_requiredTime = tiempoRequerido;
		_remainingTime = tiempoRequerido;
		_initialTime = relojActual;
		_finalTime = -1;
	}
	
	public int getID() { return _ID; }
	
	public String getDescription() { return _description; }
	
	public int getRequiredTime() { return _requiredTime; }
	
	public int getRemainingTime() { return _remainingTime; }
	
	public void setRemainingTime(int nuevoTiempo) { _remainingTime = nuevoTiempo; }
	
	public void setFinalTime(int tiempoFinal) { _finalTime = tiempoFinal; }
	
	public int calculateReturnTime() {
		return
			_finalTime == -1
			? -1
			: _finalTime - _initialTime
		;
	}


	/* Private members. */

	private int _ID;
	private String _description;
	private int _requiredTime;
	private int _remainingTime;
	private int _initialTime;
	private int _finalTime;

}