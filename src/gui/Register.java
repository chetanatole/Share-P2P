package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jtorrent.Client.Peer;

public class Register {

	public JFrame registerFrame;
	private JTextField userNameTextField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordfield;
	private JTextField nicknametextfield;
	private Peer peer;

	/**
	 * Launch the application.
	 */
	public void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register window = new Register(peer);
					window.registerFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Register(Peer peer) {
		this.peer = peer;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		registerFrame = new JFrame();
		registerFrame.setResizable(false);
		registerFrame
				.setIconImage(Toolkit.getDefaultToolkit().getImage("/imgs/Profile_GroupFriend-RoundedBlack-512.png"));
		registerFrame.setSize(800, 571);
		registerFrame.setLocationRelativeTo(null);
		registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		registerFrame.getContentPane().setLayout(null);

		JLabel registerLabel = new JLabel("REGISTER");
		registerLabel.setForeground(new Color(255, 255, 255));
		registerLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 46));
		registerLabel.setBounds(309, 10, 316, 49);
		registerFrame.getContentPane().add(registerLabel);

		JLabel userNameLabel = new JLabel("Username -");
		userNameLabel.setForeground(new Color(255, 255, 255));
		userNameLabel.setFont(new Font("Rockwell Condensed", Font.BOLD, 26));
		userNameLabel.setBounds(138, 69, 179, 31);
		registerFrame.getContentPane().add(userNameLabel);

		userNameTextField = new JTextField();
		userNameTextField.setBounds(327, 69, 309, 31);
		registerFrame.getContentPane().add(userNameTextField);
		userNameTextField.setColumns(10);

		JLabel passwordLabel = new JLabel("Password -");
		passwordLabel.setForeground(new Color(255, 255, 255));
		passwordLabel.setFont(new Font("Rockwell Condensed", Font.BOLD, 27));
		passwordLabel.setBounds(138, 156, 179, 31);
		registerFrame.getContentPane().add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(327, 156, 309, 31);
		registerFrame.getContentPane().add(passwordField);

		JLabel ConfirmPasswordLabel = new JLabel("Confirm Password    -");
		ConfirmPasswordLabel.setForeground(new Color(255, 255, 255));
		ConfirmPasswordLabel.setFont(new Font("Rockwell Condensed", Font.BOLD, 22));
		ConfirmPasswordLabel.setBounds(58, 240, 258, 37);
		registerFrame.getContentPane().add(ConfirmPasswordLabel);

		confirmPasswordfield = new JPasswordField();
		confirmPasswordfield.setBounds(327, 246, 309, 31);
		registerFrame.getContentPane().add(confirmPasswordfield);

		JLabel checkPasswordLabel = new JLabel("Password not matches.");
		checkPasswordLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		checkPasswordLabel.setForeground(new Color(255, 0, 0));
		checkPasswordLabel.setBounds(327, 274, 236, 25);
		checkPasswordLabel.setVisible(false);
		registerFrame.getContentPane().add(checkPasswordLabel);

		JLabel lblEnterUsername = new JLabel("Enter UserName");
		lblEnterUsername.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblEnterUsername.setForeground(new Color(255, 0, 0));
		lblEnterUsername.setBounds(337, 105, 168, 31);
		lblEnterUsername.setVisible(false);
		registerFrame.getContentPane().add(lblEnterUsername);

		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblEnterPassword.setForeground(new Color(255, 0, 0));
		lblEnterPassword.setBounds(337, 197, 168, 25);
		lblEnterPassword.setVisible(false);
		registerFrame.getContentPane().add(lblEnterPassword);

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBackground(new Color(51, 102, 153));
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Personal Details",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel.setBounds(138, 303, 520, 152);
		registerFrame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel PersonalDetailLabel = new JLabel("");
		PersonalDetailLabel.setBounds(10, 17, 510, 130);
		panel.add(PersonalDetailLabel);

		JLabel questionLabel = new JLabel("Enter your nick name -");
		questionLabel.setForeground(new Color(255, 255, 255));
		questionLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
		questionLabel.setBounds(48, 17, 377, 44);
		panel.add(questionLabel);

		nicknametextfield = new JTextField();
		nicknametextfield.setBounds(58, 61, 230, 44);
		panel.add(nicknametextfield);
		nicknametextfield.setColumns(10);

		JLabel checknicknamelabel = new JLabel("Enter nick name");
		checknicknamelabel.setFont(new Font("Times New Roman", Font.BOLD, 19));
		checknicknamelabel.setForeground(new Color(255, 0, 0));
		checknicknamelabel.setBounds(60, 100, 161, 28);
		checknicknamelabel.setVisible(false);
		panel.add(checknicknamelabel);
		userNameTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblEnterUsername.setVisible(false);
			}
		});
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblEnterPassword.setVisible(false);
			}
		});
		confirmPasswordfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				checkPasswordLabel.setVisible(false);
			}
		});
		nicknametextfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				checknicknamelabel.setVisible(false);
			}
		});
		JButton btnSubmit = new JButton("SIGN IN");
		btnSubmit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String[] credentials = new String[4];
				credentials[0] = userNameTextField.getText().toString();
				System.out.println(credentials[0]);
				credentials[1] = passwordField.getText().toString();
				System.out.println(credentials[1]);
				credentials[2] = nicknametextfield.getText().toString();
				System.out.println(credentials[2]);
				credentials[3] = confirmPasswordfield.getText().toString();
				System.out.println(credentials[3]);
				if (credentials[0].equals("")) {
					lblEnterUsername.setVisible(true);
				} else if (credentials[1].equals("")) {
					lblEnterPassword.setVisible(true);
				} else if (!credentials[1].equals(credentials[3])) {
					checkPasswordLabel.setVisible(true);
				} else if (credentials[2].equals("")) {
					checknicknamelabel.setVisible(true);
				} 
				else {

					String registerStatus = peer.Connect("REGISTER", credentials);
					if (registerStatus.contains("Welcome")) {
						registerFrame.setVisible(false);
						DashBoard dashboard = new DashBoard(peer,registerStatus);
						dashboard.DashBoardFrame.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(btnSubmit, "Username Already Present");
					}
				}
			
			}
		});
		btnSubmit.setForeground(new Color(255, 255, 255));
		btnSubmit.setBackground(new Color(0, 102, 153));
		btnSubmit.setFont(new Font("Rockwell Condensed", Font.BOLD, 25));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] credentials = new String[4];
				credentials[0] = userNameTextField.getText().toString();
				System.out.println(credentials[0]);
				credentials[1] = passwordField.getText().toString();
				System.out.println(credentials[1]);
				credentials[2] = nicknametextfield.getText().toString();
				System.out.println(credentials[2]);
				credentials[3] = confirmPasswordfield.getText().toString();
				System.out.println(credentials[3]);
				if (credentials[0].equals("")) {
					lblEnterUsername.setVisible(true);
				} else if (credentials[1].equals("")) {
					lblEnterPassword.setVisible(true);
				} else if (!credentials[1].equals(credentials[3])) {
					checkPasswordLabel.setVisible(true);
				} else if (credentials[2].equals("")) {
					checknicknamelabel.setVisible(true);
				} 
				else {

					String registerStatus = peer.Connect("REGISTER", credentials);
					if (registerStatus.contains("Welcome")) {
						registerFrame.setVisible(false);
						DashBoard dashboard = new DashBoard(peer,registerStatus);
						dashboard.DashBoardFrame.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(btnSubmit, "Username Already Present");
					}
				}
			}
		});
		btnSubmit.setBounds(281, 465, 251, 49);
		registerFrame.getContentPane().add(btnSubmit);

		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(new ImageIcon(Register.class.getResource("/imgs/regbg.jpg")));
		backgroundLabel.setLocation(0, -71);
		backgroundLabel.setSize(828, 642);
		registerFrame.getContentPane().add(backgroundLabel);
	}
}
