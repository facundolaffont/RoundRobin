package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import Modelo.RoundRobin;

public class Vista extends JFrame {
	private static final long serialVersionUID = 1L; // Agregado para quitar el Warning de Eclipse.
	private Dimension pantalla;
	private int ancho,
				alto;
	private JPanel panelPrincipal;
	private JScrollPane panelConsola,
						panelConsolaTablero;
	private JTextArea 	consola,
						consolaVentana;
	private JTextField textoComandos;
	private DefaultCaret caret;
	private RoundRobin modelo;
	private boolean relojAutomatico;

	
	/* Métodos públicos */
	
	public Vista(RoundRobin modelo, boolean relojAutomatico) {	
		
		// Vinculación con el modelo y configuración del reloj.
		this.modelo = modelo;
		this.relojAutomatico = relojAutomatico;
		
		// Creación y configuración de la ventana de la consola.
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		ancho = 800;
		alto = 400;
		this.setSize(ancho, alto);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((pantalla.width / 2) - (ancho / 2), (pantalla.height / 2) - (alto / 2));
		this.setResizable(false);
		
		// Creación y configuración de los paneles de la consola.
		panelPrincipal = (JPanel) this.getContentPane();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.setVisible(true);
		
		// Creación y configuración de la consola.
		consola = new JTextArea();
		consola.setBounds(0, 0, 500, 350);
		consola.setEditable(false);
		consola.setMargin(new Insets(10, 10, 10, 10));
		consola.setLineWrap(true);
		consola.setTabSize(1);
		consola.setBackground(Color.BLACK);
		consola.setForeground(Color.WHITE);
		consola.setAutoscrolls(true);
		consola.setFont(new Font("monospaced", Font.PLAIN, 12));
		caret = (DefaultCaret) consola.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		consolaVentana = new JTextArea();
		consolaVentana.setBounds(0, 0, 300, 350);
		consolaVentana.setEditable(false);
		consolaVentana.setMargin(new Insets(20, 30, 10, 10));
		consolaVentana.setLineWrap(true);
		consolaVentana.setTabSize(5);
		consolaVentana.setBackground(Color.BLACK);
		consolaVentana.setForeground(Color.WHITE);
		consolaVentana.setAutoscrolls(true);
		consolaVentana.setFont(new Font("monospaced", Font.PLAIN, 12));
		panelConsola = new JScrollPane(consola, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelConsolaTablero = new JScrollPane(consolaVentana, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelPrincipal.add(panelConsola, BorderLayout.WEST);
		panelPrincipal.add(panelConsolaTablero, BorderLayout.EAST);
		
		// Creación y configuración de la caja de texto de la consola.
		textoComandos = new JTextField();
		panelPrincipal.add(textoComandos, BorderLayout.SOUTH);
		this.setVisible(true);
		textoComandos.grabFocus();
		
		crearActionListeners();
		
		consola.append("Quantum: " + modelo.getQuantum() + ".\n");
		consola.append("Frecuencia de reloj: " + modelo.getFrecuenciaReloj() + " ms.\n\n");
		consola.append("Ingrese \"ayuda\" ó \"?\" para información sobre los posibles comandos.\n\n");
		
		String lista = "";		
		lista += "ID\tTiempo\tDescripción";
		lista += "\n--\t------\t-----------";
		consolaVentana.setText(lista);
	}
	
	public void enlistarProcesosActivos(String lista) {
		consolaVentana.setText(lista);
	}
	
	
	/* Métodos privados */
	
	private void crearActionListeners() {
		textoComandos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String descripcion, tiempoRequerido;
				String comando = ((JTextField) e.getSource()).getText();
				int tiempoRequeridoNumerico;
				
				if(!textoComandos.getText().isEmpty()) {
					
					// ayuda, ?
					if(
						comando.equals("ayuda")
						||
						comando.equals("?")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						mostrarComandos();
						
					// clear, cls
					} else if(
						comando.equals("clear")
						||
						comando.equals("cls")
					) {
						textoComandos.setText("");
						consola.setText("");
					
					// ingresarProceso(<descripción>, <tiempo de procesamiento>), ip(<descripción>, <tiempo de procesamiento>)
					} else if(
						comando.matches("^ingresarProceso\\([a-zA-Z][a-zA-Z0-9]*, [1-9][0-9]*\\)$")
						||
						comando.matches("^ip\\([a-zA-Z][a-zA-Z0-9]*, [1-9][0-9]*\\)$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						descripcion = comando.substring(comando.indexOf('(') + 1, comando.indexOf(',')); // Obtengo la descripción del proceso.
						tiempoRequerido = comando.substring(comando.indexOf(',') + 2, comando.indexOf(')')); // Obtengo el tiempo de procesamiento.
						
						try {
							
							// Se convierte en entero la cadena de texto que contiene al tiempo.
							tiempoRequeridoNumerico = Integer.parseInt(tiempoRequerido);
							
							// Agrego el proceso a la cola de espera.
							modelo.agregoAColaDeEspera(descripcion, tiempoRequeridoNumerico);
							
						} catch(NumberFormatException f) {
							f.printStackTrace();
							modelo.salirConError();
						}
					
					// mostrarQuantum, mq
					} else if(
						comando.matches("^mostrarQuantum$")
						||
						comando.matches("^mq$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						consola.append("Quantum: " + Integer.toString(modelo.getQuantum()) + ".\n\n");
					
						// mostrarReloj, mr
					} else if(
							(
								comando.matches("^mostrarReloj$")
								||
								comando.matches("^mr$")
							)
							&&
							relojAutomatico
						) {
							textoComandos.setText("");
							consola.append("> " + comando + "\n\n");
							consola.append("Frecuencia del reloj: " + Integer.toString(modelo.getFrecuenciaReloj()) + " ms.\n\n");
						
					// mostrarTiempoDeRetornoPromedio, mtrp
					} else if(
						comando.matches("^mostrarTiempoDeRetornoPromedio$")
						||
						comando.matches("^mtrp$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						if(modelo.getTiempoPromedioDeRetorno() == -1) consola.append("Todavía no hay datos.\n\n");
						else consola.append(Float.toString(modelo.getTiempoPromedioDeRetorno()) + "\n\n");
							
					// mostrarTiempoDeEsperaPromedio, mtep
					} else if(
						comando.matches("^mostrarTiempoDeEsperaPromedio$")
						||
						comando.matches("^mtep$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						if(modelo.getTiempoPromedioDeEspera() == -1) consola.append("Todavía no hay datos.\n\n");
						else consola.append(Float.toString(modelo.getTiempoPromedioDeEspera()) + "\n\n");
					
					// ciclo, c
					} else if(
						(
							comando.matches("^ciclo$")
							||
							comando.matches("^c$")
						)
						&&
						!relojAutomatico
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						modelo.realizarCiclo();
						
					// salir
					} else if(comando.equals("salir")) modelo.salirSinError();
					
					// Comando incorrecto.
					else {
						consola.append("> " + comando + "\n\n");
						consola.append("El comando que ingresó es incorrecto.\n\n");
						consola.append("Ingrese \"ayuda\" ó \"?\" para información sobre los posibles comandos.\n\n");
					}
				}
			}
		});
	}
	
	private void mostrarComandos() {
		consola.append("*** Comandos ***\n");
		consola.append("\n* \"ayuda\" ó \"?\" para mostrar este mensaje.");
		consola.append("\n* \"clear\" ó \"cls\" para limpiar la consola.");
		consola.append("\n* \"ingresarProceso(<descripción>, <tiempo de procesamiento>)\" ó \"ip(<descripción>, <tiempo de procesamiento>)\" para ingresar un proceso al round robin.");
		consola.append("\n* \"mostrarQuantum\" ó \"mq\" para mostrar el valor del Quantum.");
		consola.append("\n* \"mostrarReloj\" ó \"mr\" para mostrar la frecuencia del reloj.");
		consola.append("\n* \"mostrarTiempoDeRetornoPromedio\" ó \"mtrp\" para mostrar el tiempo de retorno promedio hasta ahora.");
		consola.append("\n* \"mostrarTiempoDeEsperaPromedio\" ó \"mtep\" para mostrar el tiempo de retorno promedio hasta ahora.");
		if (!relojAutomatico)
			consola.append("\n* \"ciclo\" ó \"c\" para hacer avanzar un ciclo el reloj.");
		consola.append("\n* \"salir\" para abandonar el juego.");
		consola.append("\n\n");
	}
	
}