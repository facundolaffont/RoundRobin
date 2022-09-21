package view;

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

import model.RoundRobin;

public class View extends JFrame {
	
	/* Public members. */
	
	public View(RoundRobin modelo, boolean relojAutomatico) {	
		
		// Vinculación con el modelo y configuración del reloj.
		this.modelo = modelo;
		this.relojAutomatico = relojAutomatico;
		
		// Creation and configuration of console window.
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		ancho = 800;
		alto = 400;
		this.setSize(ancho, alto);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((pantalla.width / 2) - (ancho / 2), (pantalla.height / 2) - (alto / 2));
		this.setResizable(false);
		
		// Creation and configuration of console panels.
		panelPrincipal = (JPanel) this.getContentPane();
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.setVisible(true);
		
		// Creation and configuracion of console.
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
		
		// Creation and configuration of console text box.
		textoComandos = new JTextField();
		panelPrincipal.add(textoComandos, BorderLayout.SOUTH);
		this.setVisible(true);
		textoComandos.grabFocus();
		
		crearActionListeners();
		
		consola.append("Quantum: " + modelo.getQuantum() + ".\n");
		if(modelo.getClockFreq() == -1)
			consola.append("Reloj: manual\n\n");
		else
			consola.append("Frecuencia de reloj: " + modelo.getClockFreq() + " ms.\n\n");
		consola.append("Ingrese \"ayuda\" ó \"?\" para información sobre los posibles comandos.\n\n");
		
		String lista = "";		
		lista += "ID\tTiempo\tDescripción";
		lista += "\n--\t------\t-----------";
		consolaVentana.setText(lista);
	}
	
	public void enlistarProcesosActivos(String lista) {
		consolaVentana.setText(lista);
	}
	
	
	/* Private members. */

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
	
	private void crearActionListeners() {
		textoComandos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String descripcion, tiempoRequerido;
				String comando = ((JTextField) e.getSource()).getText();
				int tiempoRequeridoNumerico;
				
				if(!textoComandos.getText().isEmpty()) {
					
					// help, ?
					if(
						comando.equals("help")
						||
						comando.equals("?")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						showCommands();
						
					// clear, cls
					} else if(
						comando.equals("clear")
						||
						comando.equals("cls")
					) {
						textoComandos.setText("");
						consola.setText("");
					
					// newproc(<description>, <processing time>), proc(<description>, <processing time>)
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
							modelo.addToWaitingQueue(descripcion, tiempoRequeridoNumerico);
							
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
							consola.append("Frecuencia del reloj: " + Integer.toString(modelo.getClockFreq()) + " ms.\n\n");
						
					// mostrarTiempoDeRetornoPromedio, mtrp
					} else if(
						comando.matches("^mostrarTiempoDeRetornoPromedio$")
						||
						comando.matches("^mtrp$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						if(modelo.getAverageReturnTime() == -1) consola.append("Todavía no hay datos.\n\n");
						else consola.append(Float.toString(modelo.getAverageReturnTime()) + "\n\n");
							
					// mostrarTiempoDeEsperaPromedio, mtep
					} else if(
						comando.matches("^mostrarTiempoDeEsperaPromedio$")
						||
						comando.matches("^mtep$")
					) {
						textoComandos.setText("");
						consola.append("> " + comando + "\n\n");
						if(modelo.getAverageWaitingTime() == -1) consola.append("Todavía no hay datos.\n\n");
						else consola.append(Float.toString(modelo.getAverageWaitingTime()) + "\n\n");
					
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
						modelo.cycle();
						
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
	
	private void showCommands() {
		consola.append(
			"COMMANDS"
				+ "\n\t* \"help\" or \"?\" to show this message."
				+ "\n\t* \"clear\" or \"cls\" to clean the console."
				+ "\n\t* \"newproc(<description>, <processing time>)\" or \"proc(<description>, <processing time>)\" to create a new process."
				+ "\n\t* \"showquantum\" or \"sq\" to show the quantum value."
				+ "\n\t* \"showclock\" or \"sc\" to show the clock period."
				+ "\n\t* \"mrt\" to show current mean return time."
				+ "\n\t* \"mwt\" to show current mean waiting time."
		);
		if (!relojAutomatico) consola.append(
				"\n\t* \"cycle\" or \"c\" to move the clock one cycle."
				+ "\n\t* \"exit\" to exit the program."
		);
	}
	
}