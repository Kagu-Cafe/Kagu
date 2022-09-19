/**
 * 
 */
package cafe.kagu.kagu.prot.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.prot.Note;

/**
 * @author DistastefulBannock
 *
 */
public class SwingWindow {
	
	static {
		FlatLightLaf.setup();
	}
	
	private static JFrame mainFrame;
	private static JPanel homePanel, loginPanel, registerPanel;
	private static JTextField usernameField = new JTextField();
	private static JPasswordField passwordField1 = new JPasswordField();
	private static JRadioButton saveCredentials = new JRadioButton("Save Credentials");
	
	/**
	 * Called before the mc display is created
	 */
	public static void start() {
		// Initializes keyauth
		Kagu.getKeyAuth().initialize(m -> {
			System.err.println("Error initializing, exiting cheat");
			System.exit(Note.WINAUTH_APP_DISABLED);
		}, m -> {
			System.err.println(m + ", exiting cheat");
			System.exit(Note.WINAUTH_REQUEST_FAILED);
		}, m -> {
			System.err.println("Reponse tampered with, exiting cheat");
			System.exit(Note.WINAUTH_RESPONSE_TAMPERED);
		});
		
		final int padding = 10;
		final float fontSize = 24;
		mainFrame = new JFrame("Auth");
		mainFrame.setLayout(new BorderLayout());
		
		loginPanel = new JPanel(true);
		loginPanel.setLayout(new BorderLayout());
		loginPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
		{ // Register panel shit (crap code because ui code is shit)
			JPanel form = new JPanel(true);
			form.setLayout(new BorderLayout());
			final int formFieldCount = 3;
			
			JPanel labels = new JPanel(new GridLayout(formFieldCount, 1, padding, padding));
			labels.add(new JLabel("Username: "));
			labels.add(new JLabel("Password: "));
			labels.add(new JLabel(""));
			form.add(labels, BorderLayout.WEST);
			
			JPanel fields = new JPanel(new GridLayout(formFieldCount, 1, padding, padding));
			fields.add(usernameField);
			fields.add(passwordField1);
			fields.add(saveCredentials);
			form.add(fields, BorderLayout.CENTER);
			
			loginPanel.add(form, BorderLayout.CENTER);
			
			JButton submitForm = new JButton("Login");
			JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
			errorLabel.setForeground(Color.RED);
			submitForm.addActionListener(evt -> {
				Kagu.getKeyAuth().login(usernameField.getText(), new String(passwordField1.getPassword()),
						msg -> {
							errorLabel.setText("0x" + Integer.toHexString(Note.WINAUTH_REQUEST_FAILED) + " " + msg);
						},
						msg -> {
							new Throwable().printStackTrace();
							errorLabel.setText("0x" + Integer.toHexString(Note.WINAUTH_RESPONSE_TAMPERED) + " Json Invalid");
						},
						msg -> {
							errorLabel.setText("0x" + Integer.toHexString(Note.WINAUTH_LOGIN_FAILED) + " " + msg);
						});
			});
			loginPanel.add(submitForm, BorderLayout.SOUTH);
			
			JPanel backPanel = new JPanel(true);
			backPanel.setLayout(new BorderLayout());
			JButton back = new JButton("<");
			back.addActionListener(evt -> {
				mainFrame.setContentPane(homePanel);
				mainFrame.revalidate();
				mainFrame.repaint();
			});
			backPanel.add(back, BorderLayout.WEST);
			backPanel.add(errorLabel, BorderLayout.CENTER);
			loginPanel.add(backPanel, BorderLayout.NORTH);
		}
		
		registerPanel = new JPanel(true);
		registerPanel.setLayout(new BorderLayout());
		registerPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
		{ // Register panel shit
			JPanel form = new JPanel(true);
			form.setLayout(new GridLayout(3, 1, padding, padding));
			
			JPanel backPanel = new JPanel(true);
			backPanel.setLayout(new BorderLayout());
			JButton back = new JButton("<");
			back.addActionListener(evt -> {
				mainFrame.setContentPane(homePanel);
				mainFrame.revalidate();
				mainFrame.repaint();
			});
			backPanel.add(back, BorderLayout.WEST);
			registerPanel.add(backPanel, BorderLayout.NORTH);
		}
		
		homePanel = new JPanel(true);
		homePanel.setLayout(new GridLayout(2, 1, padding, padding));
		homePanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(evt -> {
			mainFrame.setContentPane(loginPanel);
			mainFrame.revalidate();
			mainFrame.repaint();
		});
		loginButton.setFont(loginButton.getFont().deriveFont(fontSize));
		homePanel.add(loginButton);
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(evt -> {
			mainFrame.setContentPane(registerPanel);
			mainFrame.revalidate();
			mainFrame.repaint();
		});
		registerButton.setFont(registerButton.getFont().deriveFont(fontSize));
		homePanel.add(registerButton);
		
		mainFrame.setContentPane(homePanel);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(450, 350);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.toFront();
		mainFrame.setAlwaysOnTop(true);
		mainFrame.requestFocus();
		mainFrame.setVisible(true);
		while (!Kagu.isLoggedIn());
	}
	
	/**
	 * @return the mainFrame
	 */
	public static JFrame getMainFrame() {
		return mainFrame;
	}
	
}
