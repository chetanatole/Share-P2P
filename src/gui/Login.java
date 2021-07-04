package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;

import jtorrent.Client.Peer;

public class Login {

	public JFrame LoginFrame;
	private JTextField userNametextField;
	private JPasswordField passwordField;
	private Peer peer;

	/**
	 * Launch the application.
	 */
	public void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login(peer);
					window.LoginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login(Peer peer) {
		this.peer = peer;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		LoginFrame = new JFrame();
		LoginFrame.getContentPane().setBackground(SystemColor.text);
		LoginFrame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Login.class.getResource("/imgs/Profile_GroupFriend-RoundedBlack-512.png")));
		LoginFrame.setResizable(false);
		LoginFrame.setSize(800, 600);
		LoginFrame.setLocationRelativeTo(null);
		LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginFrame.getContentPane().setLayout(null);

		JLabel jTorrentLabel = new JLabel("ShareP2P");
		jTorrentLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(255, 255, 255)));
		jTorrentLabel.setOpaque(true);
		jTorrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jTorrentLabel.setBackground(new Color(255, 255, 255));
		jTorrentLabel.setForeground(new Color(51, 102, 153));
		jTorrentLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 70));
		jTorrentLabel.setBounds(218, 69, 399, 67);
		LoginFrame.getContentPane().add(jTorrentLabel);

		JLabel userNameLabel = new JLabel("Username");
		userNameLabel.setIcon(new ImageIcon(Login.class.getResource("/imgs/768px-Circle-icons-profile.jpeg")));
		userNameLabel.setForeground(new Color(255, 255, 255));
		userNameLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		userNameLabel.setBounds(218, 158, 222, 40);
		LoginFrame.getContentPane().add(userNameLabel);

		userNametextField = new JTextField();
		userNametextField.setBorder(new EmptyBorder(2, 2, 2, 2));
		userNametextField.setBounds(439, 165, 164, 40);
		LoginFrame.getContentPane().add(userNametextField);
		userNametextField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(439, 272, 164, 40);
		LoginFrame.getContentPane().add(passwordField);

		JLabel lblEnterUsername = new JLabel("Enter username");
		lblEnterUsername.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblEnterUsername.setForeground(new Color(255, 0, 0));
		lblEnterUsername.setBounds(449, 215, 175, 25);
		lblEnterUsername.setVisible(false);
		LoginFrame.getContentPane().add(lblEnterUsername);

		JLabel lblEnterPassword = new JLabel("Enter Password");
		lblEnterPassword.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		lblEnterPassword.setForeground(new Color(255, 0, 0));
		lblEnterPassword.setBounds(439, 317, 178, 25);
		lblEnterPassword.setVisible(false);
		LoginFrame.getContentPane().add(lblEnterPassword);

		JLabel RegisterLabel = new JLabel("New User ? Register.");
		RegisterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		RegisterLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		RegisterLabel.setForeground(new Color(255, 255, 255));
		RegisterLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFrame.setVisible(false);
				Register register = new Register(peer);
				register.registerFrame.setVisible(true);
			}
		});
		RegisterLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 24));
		RegisterLabel.setBounds(205, 426, 427, 40);
		LoginFrame.getContentPane().add(RegisterLabel);

		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String[] credentials = new String[2];
				String loginStatus;
				credentials[0] = userNametextField.getText().toString();
				credentials[1] = passwordField.getText().toString();
				if (credentials[0].equals("")) {
					lblEnterUsername.setVisible(true);
				} else if (credentials[1].equals("")) {
					lblEnterPassword.setVisible(true);
				} else {
					loginStatus = peer.Connect("LOGIN", credentials);
					if (loginStatus.contains("Welcome")) {
						DashBoard dashboard = new DashBoard(peer,loginStatus);
						LoginFrame.setVisible(false);
						dashboard.DashBoardFrame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(btnLogin, "Invalid credentials");
					}
				}
			}
		});
		btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnLogin.setForeground(new Color(0, 102, 153));
		btnLogin.setBackground(new Color(255, 255, 255));
		btnLogin.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 20));
		btnLogin.setBounds(327, 376, 191, 40);
		LoginFrame.getContentPane().add(btnLogin);

		JLabel ForgetPasswordLabel = new JLabel("Forget Password?");
		ForgetPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ForgetPasswordLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		ForgetPasswordLabel.setForeground(new Color(255, 255, 255));
		ForgetPasswordLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFrame.setVisible(false);
				ForgetPassword forgetPassword = new ForgetPassword(peer);
				forgetPassword.ForgetPasswordFrame.setVisible(true);
			}
		});
		ForgetPasswordLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 27));
		ForgetPasswordLabel.setBounds(205, 461, 427, 45);
		LoginFrame.getContentPane().add(ForgetPasswordLabel);

		JLabel lblPassword = new JLabel(" Password");
		lblPassword.setIcon(new ImageIcon(Login.class.getResource("/imgs/images.png")));
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		lblPassword.setBounds(218, 265, 211, 40);
		LoginFrame.getContentPane().add(lblPassword);

		JLabel lblbg = new JLabel("");
		lblbg.setIcon(new ImageIcon(Login.class.getResource("/imgs/qw11.jpg")));
		lblbg.setBounds(41, 0, 797, 637);
		LoginFrame.getContentPane().add(lblbg);

		userNametextField.addKeyListener(new KeyAdapter() {
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
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] credentials = new String[2];
				String loginStatus;
				credentials[0] = userNametextField.getText().toString();
				credentials[1] = passwordField.getText().toString();
				if (credentials[0].equals("")) {
					lblEnterUsername.setVisible(true);
				} else if (credentials[1].equals("")) {
					lblEnterPassword.setVisible(true);
				} else {
					loginStatus = peer.Connect("LOGIN", credentials);
					if (loginStatus.contains("Welcome")) {
						DashBoard dashboard = new DashBoard(peer,loginStatus);
						LoginFrame.setVisible(false);
						dashboard.DashBoardFrame.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(btnLogin, "Invalid Credentials");
					}
				}
			}
		});
	}
}
