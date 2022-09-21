package app;

import controller.Controller;

public class RoundRobin {

	/* Public members. */

	public static void main(String[] args) {
		
		// Shows instructions if the number of arguments is incorrect.
		if(args.length < 1 || args.length > 2) {
			_printInstructions();			
			System.exit(0);
		}

		// Verifies that the first argument is an integer greater than cero.
		int quantum = 0;
		try {
			quantum = Integer.parseInt(args[0]);
			if (quantum <= 0) {
				_printInstructions();
				System.exit(0);
			}
		}
		catch(NumberFormatException e) {
			_printInstructions();
			System.exit(0);
		}
		
		// If there is a second argument, verifies that it's an integer between
		// 100 and 5000.
		if(args.length == 2) {
			try {
				int clockPeriod = Integer.parseInt(args[1]);
				if (clockPeriod < 100 || clockPeriod > 5000) {
					_printInstructions();
					System.exit(0);
				}
				new Controller(quantum, clockPeriod);
			}
			catch(NumberFormatException e) {
				_printInstructions();
				System.exit(0);
			}
		} else {

			// Controller is created with one argument.
			new Controller(quantum);

		}
	}
	

	/* Private members. */

	private static void _printInstructions() {
		System.out.println(
			"\nSYNOPSIS" +
				"\n\tjava RoundRobin <quantum> [<timer period>]" +
			
			"\n\nOPTIONS" +
				"\n\tIf two arguments are used, automatic timer is enabled, using the second argument as the timer period." +
				"\n\tIf one argument is used, instead, manual timer is enabled." +

				"\n\n\t<quantum> must be an integer greater than cero, and <timer period> must be an integer between 100 and 5000."
		);
	}

}