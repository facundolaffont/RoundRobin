package Main;

import Controlador.Controlador;

public class Main {
	public static void main(String[] args) {
		boolean salir;
		int quantum;
		int periodoReloj;
		
		// Inicializaciones.
		salir = false;
		quantum = 0;
		periodoReloj = 0;
		
		// Verifica que los argumentos se hayan pasado correctamente,
		// y establece si se muestran las instrucciones de uso si se
		// pasaron de forma incorrecta.
		if(args.length < 1 || args.length > 2) {
			imprimirInstrucciones();			
			salir = true;
		} else {
		// Se ingresó una cantidad válida de argumentos.
			
			// Se verifica que el primer argumento sea un entero mayor a cero.
			try {
				quantum = Integer.parseInt(args[0]);
				if (quantum <= 0) salir = true;
			}
			catch(NumberFormatException e) {
				imprimirInstrucciones();
				salir = true;
			}
			
			// Si hay un segundo argumento, se verifica que el segundo sea un entero
			// entre 100 y 5000. 
			if(args.length == 2) {
				try {
					periodoReloj = Integer.parseInt(args[1]);
					if (periodoReloj < 100 || periodoReloj > 5000) salir = true;
				}
				catch(NumberFormatException e) {
					imprimirInstrucciones();
					salir = true;
				}
			}	
		}
		
		if(!salir) {
			// Se crea el controlador.
			if(args.length == 1) new Controlador(quantum);
			else new Controlador(quantum, periodoReloj);
		}
	}
	
	private static void imprimirInstrucciones() {
		System.out.println("Uso de la aplicación:");
		System.out.println("\tjava Main <quantum> <periodo del reloj>");
		System.out.println("\tjava Main <quantum>");
		System.out.println();
		System.out.println("Si se utilizan dos argumentos, se configura el funcionamiento automático del reloj,");
		System.out.println("con un periodo indicado por el segundo argumento. En cambio, si se utiliza un argumento,");
		System.out.println("se configura el funcionamiento manual del reloj.");
		System.out.println();
		System.out.println("El <quantum> debe ser un entero mayor a cero, y el <periodo del reloj> debe ser un entero");
		System.out.println("entre 100 y 5000.");
	}
}