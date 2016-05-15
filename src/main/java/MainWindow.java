import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import api.PSPort;
import api.PSPortFactory;
import api.TopicListener;
import data.MessagePublication;
import data.MessagePublish;

public class MainWindow extends JFrame implements KeyListener, ActionListener, TopicListener {

	private static final long serialVersionUID = 1L;
	
	private JTextArea output;
	private JTextField txtEnviar;
	private JButton btnEnviar;
	
	PSPort port;
	
	public static void main(String[] args) {
		MainWindow w = new MainWindow();
		w.setVisible(true);
	}
	
	public MainWindow() {
		output = new JTextArea(5, 20);
		output.setMargin(new Insets(5,5,5,5));
		output.setEditable(false);
		output.setForeground(Color.WHITE);
		output.setBackground(Color.BLACK);
		JScrollPane scroll = new JScrollPane(output);
		add(scroll);
		
		JPanel panelSur = new JPanel(new BorderLayout());
		txtEnviar = new JTextField();
		txtEnviar.setForeground(Color.WHITE);
		txtEnviar.setBackground(Color.DARK_GRAY);
		btnEnviar = new JButton("Send");
		panelSur.add(txtEnviar,BorderLayout.CENTER);
		panelSur.add(btnEnviar,BorderLayout.EAST);
		add(panelSur,BorderLayout.SOUTH);
		txtEnviar.addKeyListener(this);
		btnEnviar.addActionListener(this);
		
		setSize(800, 500);
		setMinimumSize(getSize());
		setLocation(300, 70);
		setTitle("Prueba Middleware");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	txtEnviar.requestFocus();
		    }
		}); 
		
		try {
			port = PSPortFactory.getPort("PSPortTCP");
			port.addTopicListener(this);
			println("Connected with PSPortTCP");
		} catch (Throwable e) {
			println(e.getClass().getName()+": "+e.getMessage());
		}
	}
	
	public synchronized void clear() {
		output.setText("");
	}

	public synchronized void print(String s) {
		output.append(s);
	}
	
	public void println(String s) {
		print(s+"\n");
	}

	public void keyPressed(KeyEvent k) {
		if(k.getKeyChar()=='\n') {
			sendMsg();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		sendMsg();
	}
	
	public void sendMsg() {
		try {
			String[] args = txtEnviar.getText().split("[ ]");
			switch(args[0]) {
			case "s":
			case "su":
			case "sub":
			case "subs":
			case "subscribe":
				port.subscribe(args[1]);
				println("Subscribed to "+args[1]);
				break;
			case "l":
			case "a":
			case "al":
			case "addlistener":
			case "addListener":
				port.addTopicListener(this);
				println("Listener added");
				break;
			case "r":
			case "rl":
			case "removelistener":
			case "removeListener":
				port.removeTopicListener(this);
				println("Listener removed");
				break;
			case "u":
			case "us":
			case "un":
			case "usub":
			case "unsub":
			case "unsubscribe":
				port.unsubscribe(args[1]);
				println("Unsubscribed from "+args[1]);
				break;
			case "p":
			case "pu":
			case "pub":
			case "publ":
			case "publish":
				port.publish(new MessagePublish(args[1], args[2]));
				println("Published> Topic: "+args[1]+" || Data: "+args[2]);
				break;
			case "g":
			case "gls":
			case "get":
			case "getlastsample":
			case "getLastSample":
				MessagePublication message = port.getLastSample(args[1]);
				println("Last sample of "+args[1]+"> Timestamp: "+message.getTimestamp()+" || Sender: "+message.getSender()+" || Topic: "+message.getTopic()+" || Data: "+message.getDataObject());
				break;
			case "PSPortTCP":
				port = PSPortFactory.getPort(txtEnviar.getText());
				port.addTopicListener(this);
				println("Connected with "+txtEnviar.getText());
				break;
			case "c":
			case "cls":
			case "clear":
				clear();
				break;
			case "end":
			case "exit":
			case "bye":
			case "cya":
				println("Bye");
				System.exit(0);
				break;
			default:
				println("Unknown command");
				break;
			}
		} catch (Throwable e) {
			println(e.getClass().getName()+": "+e.getMessage());
		}
		txtEnviar.setText("");
	}
	
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	public void publicationReceived(MessagePublication message) {
		try {
			println("Received> Timestamp: "+message.getTimestamp()+" || Sender: "+message.getSender()+" || Topic: "+message.getTopic()+" || Data: "+message.getDataObject());
		} catch (Exception e) {
			println(e.getClass().getName()+": "+e.getMessage());
		}
	}
	
}
