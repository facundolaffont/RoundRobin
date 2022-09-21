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
	
	public View(RoundRobin model, boolean automaticClock) {	
		
		// Inits.
		_model = model;
		_automaticClock = automaticClock;
		
		// Creation and configuration of console window.
		_screen = Toolkit.getDefaultToolkit().getScreenSize();
		_width = 800;
		_height = 400;
		this.setSize(_width, _height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((_screen.width / 2) - (_width / 2), (_screen.height / 2) - (_height / 2));
		this.setResizable(false);
		
		// Creation and configuration of console panels.
		_mainPanel = (JPanel) this.getContentPane();
		_mainPanel.setLayout(new BorderLayout());
		_mainPanel.setVisible(true);
		
		// Creation and configuracion of console.
		_console = new JTextArea();
		_console.setBounds(0, 0, 500, 350);
		_console.setEditable(false);
		_console.setMargin(new Insets(10, 10, 10, 10));
		_console.setLineWrap(true);
		_console.setTabSize(1);
		_console.setBackground(Color.BLACK);
		_console.setForeground(Color.WHITE);
		_console.setAutoscrolls(true);
		_console.setFont(new Font("monospaced", Font.PLAIN, 12));
		_caret = (DefaultCaret) _console.getCaret();
		_caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		_windowConsole = new JTextArea();
		_windowConsole.setBounds(0, 0, 300, 350);
		_windowConsole.setEditable(false);
		_windowConsole.setMargin(new Insets(20, 30, 10, 10));
		_windowConsole.setLineWrap(true);
		_windowConsole.setTabSize(5);
		_windowConsole.setBackground(Color.BLACK);
		_windowConsole.setForeground(Color.WHITE);
		_windowConsole.setAutoscrolls(true);
		_windowConsole.setFont(new Font("monospaced", Font.PLAIN, 12));
		_consolePanel = new JScrollPane(_console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		_boardPanel = new JScrollPane(_windowConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_mainPanel.add(_consolePanel, BorderLayout.WEST);
		_mainPanel.add(_boardPanel, BorderLayout.EAST);
		
		// Creation and configuration of console text box.
		_commandTextBox = new JTextField();
		_mainPanel.add(_commandTextBox, BorderLayout.SOUTH);
		this.setVisible(true);
		_commandTextBox.grabFocus();
		
		_createActionListener();
		
		_console.append("Quantum: " + model.getQuantum() + ".");
		if(model.getClockFreq() == -1)
			_console.append("\nClock: manual");
		else
			_console.append("\nClock period: " + model.getClockFreq() + " ms.");
		_console.append("\n\nEnter \"help\" or \"?\" to display available commands.");
		
		String list =
				   "ID" + "\t" + "Time" + "\t" + "Description" +
			"\n" + "--" + "\t" + "----" + "\t" + "-----------"
		;
		_windowConsole.setText(list);
	}
	
	public void enlistarProcesosActivos(String list) {
		_windowConsole.setText(list);
	}
	
	
	/* Private members. */

	private Dimension _screen;
	private int _width,
				_height;
	private JPanel _mainPanel;
	private JScrollPane _consolePanel,
						_boardPanel;
	private JTextArea 	_console,
						_windowConsole;
	private JTextField _commandTextBox;
	private DefaultCaret _caret;
	private RoundRobin _model;
	private boolean _automaticClock;
	
	private void _createActionListener() {

	_commandTextBox.addActionListener(new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent e) {
		String description, requiredTime;
		String command = ( (JTextField) e.getSource() ).getText();
		int requiredTimeInt;
		
		// Do nothing if command textbox is empty.
		if( _commandTextBox.getText().isEmpty() ) return;
			
		// help, ?.
		if( command.matches("(help|\\?)") ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			showCommands();
		}
			
		// clear, cls.
		else if( command.matches("(clear|cls)") ) {
			_commandTextBox.setText("");
			_console.setText("");
		}
		
		// proc(<description>, <processing time>).
		else if( command.matches("^proc\\([a-zA-Z][a-zA-Z0-9]*, [1-9][0-9]*\\)$") ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			
			// Gets process description.
			description = command.substring(
				command.indexOf('(') + 1,
				command.indexOf(',')
			);
			
			// Gets processing time.
			requiredTime = command.substring(
				command.indexOf(',') + 2,
				command.indexOf(')')
			);
			
			try {
				requiredTimeInt = Integer.parseInt(requiredTime);
				_model.addToWaitingQueue(description, requiredTimeInt);
			} catch(NumberFormatException f) {
				f.printStackTrace();
				_model.exitWithError();
			}
		}
		
		// q (shows Quantum).
		else if( command.equals("q") ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			_console.append("\n\nQuantum: " + Integer.toString(_model.getQuantum()) + ".");
		}
		
		// sc (shows clock period).
		else if( command.equals("sc") && _automaticClock ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			_console.append("\n\nClock period: " + Integer.toString(_model.getClockFreq()) + " ms.");
		}
			
		// mrt (shows Mean Return Time).
		else if( command.equals("mrt") ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			if(_model.getAverageReturnTime() == -1) _console.append("\n\nStill no data.");
			else _console.append( "\n\n" + Float.toString( _model.getAverageReturnTime() ));
		}
				
		// mwt (shows Mean Waiting Time).
		else if( command.equals("mwt") ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			if(_model.getAverageWaitingTime() == -1) _console.append("\n\nStill no data.");
			else _console.append( "\n\n" + Float.toString( _model.getAverageWaitingTime() ) );
		}
		
		// c (moves a cycle forward).
		else if( command.equals("c") && !_automaticClock ) {
			_commandTextBox.setText("");
			_console.append("> " + command);
			_model.cycle();
		}
			
		// exit.
		else if( command.equals("exit") ) _model.exitWithoutError();
		
		// Malformed command.
		else { _console.append(
			"\n\n> " + command +
			"\n\nIncorrect command." +
			"\nEnter \"help\" or \"?\" to display available commands."
		); }

		_console.append("\n");

	} // public void actionPerformed(ActionEvent e)
	}); // _commandTextBox.addActionListener
	} // private void _createActionListener()
	
	private void showCommands() {
		_console.append(
			"\n\nCOMMANDS" +
				"\n\t* \"help\" or \"?\" to show this message." +
				"\n\t* \"clear\" or \"cls\" to clean the console." +
				"\n\t* \"proc(<description>, <processing time>)\" to create a new process." +
				"\n\t* \"q\" to show the quantum value." +
				"\n\t* \"mrt\" to show current mean return time." +
				"\n\t* \"mwt\" to show current mean waiting time."
		);
		if (_automaticClock) _console.append(
				"\n\t* \"sc\" to show the clock period."
		);
		if (!_automaticClock) _console.append(
				"\n\t* \"c\" to move the clock one cycle." +
				"\n\t* \"exit\" to exit the program."
		);
	}
	
}