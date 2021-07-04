package gui;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

import org.javatuples.Pair;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;

import jtorrent.Client.Peer;
import jtorrent.Client.FileLeecher;;

public class DashBoard {

	public JFrame DashBoardFrame;
	JProgressBar progressbar = null;
	private Peer peer;
	private String welcomeMsg=null;
	private String metaFileName = null, publishFileName = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// DashBoard window = new DashBoard();
					// window.DashBoardFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public DashBoard(Peer peer,String welcomeMsg) {
		this.peer = peer;
		this.welcomeMsg=welcomeMsg;
		peer.UpdateDaemon();
		new Thread(new Runnable() {
			@Override
			public void run() {
				peer.SeederDaemon();
			}
		}).start();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DashBoardFrame = new JFrame();
		DashBoardFrame.setResizable(false);
		DashBoardFrame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(DashBoard.class.getResource("/imgs/Profile_GroupFriend-RoundedBlack-512.png")));
		// DashBoardFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\91860\\Desktop\\Project\\gui\\src\\imgs\\Profile_GroupFriend-RoundedBlack-512.png"));
		DashBoardFrame.setSize(1227, 714);
		DashBoardFrame.setLocationRelativeTo(null);
		DashBoardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DashBoardFrame.getContentPane().setLayout(null);

		JPanel UpperPanel = new JPanel();
		UpperPanel.setBounds(0, 0, 1213, 237);
		UpperPanel.setBackground(Color.WHITE);
		DashBoardFrame.getContentPane().add(UpperPanel);
		UpperPanel.setLayout(null);

		JLabel lblIcon = new JLabel("");
		lblIcon.setIcon(
				new ImageIcon(DashBoard.class.getResource("/imgs/images-removebg-preview-removebg-preview.png")));
		lblIcon.setBackground(Color.WHITE);
		lblIcon.setBounds(387, 10, 304, 170);
		UpperPanel.add(lblIcon);

		JLabel lblTagline = new JLabel("CONNECT | DOWNLOAD | SHARE");
		lblTagline.setForeground(new Color(0, 153, 153));
		lblTagline.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 24));
		lblTagline.setBounds(365, 164, 484, 37);
		UpperPanel.add(lblTagline);

		JLabel lblConnectMsg = new JLabel("You Are Connected!!");
		lblConnectMsg.setFont(new Font("FreeSerif", Font.BOLD | Font.ITALIC, 22));
		lblConnectMsg.setBackground(Color.WHITE);
		lblConnectMsg.setForeground(Color.BLACK);
		lblConnectMsg.setBounds(10, 10, 326, 37);
		UpperPanel.add(lblConnectMsg);

		JLabel lblUsername = new JLabel("");
		lblUsername.setText(welcomeMsg);
		lblUsername.setForeground(Color.BLACK);
		lblUsername.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
		lblUsername.setBackground(Color.WHITE);
		lblUsername.setBounds(872, 10, 341, 37);
		UpperPanel.add(lblUsername);

		JLabel lblLogout = new JLabel("LOGOUT??");

		lblLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLogout.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(32, 178, 170)));
		lblLogout.setIcon(new ImageIcon(DashBoard.class.getResource("/imgs/20712467.jpg")));
		lblLogout.setForeground(Color.BLACK);
		lblLogout.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblLogout.setBackground(Color.WHITE);
		lblLogout.setBounds(1003, 45, 200, 66);
		UpperPanel.add(lblLogout);

		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setBackground(new Color(255, 255, 255));
		ButtonPanel.setBounds(0, 236, 1213, 46);
		DashBoardFrame.getContentPane().add(ButtonPanel);
		ButtonPanel.setLayout(null);

		JButton btnShare = new JButton("SHARE");
		btnShare.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnShare.setBorder(new MatteBorder(6, 6, 0, 6, (Color) new Color(0, 0, 0)));
		btnShare.setBackground(new Color(0, 153, 153));
		btnShare.setForeground(Color.BLACK);
		btnShare.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		btnShare.setBounds(0, 0, 399, 46);
		ButtonPanel.add(btnShare);

		JButton btnMyFiles = new JButton("MY FILES");
		btnMyFiles.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnMyFiles.setBorder(null);

		btnMyFiles.setBackground(Color.WHITE);
		btnMyFiles.setForeground(Color.BLACK);
		btnMyFiles.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		btnMyFiles.setBounds(401, 0, 432, 46);
		ButtonPanel.add(btnMyFiles);

		JButton btnProcess = new JButton("PROCESS");
		btnProcess.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnProcess.setBorder(null);
		btnProcess.setBackground(Color.WHITE);
		btnProcess.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		btnProcess.setBounds(837, 0, 376, 46);
		ButtonPanel.add(btnProcess);

		JPanel SharePanel = new JPanel();
		SharePanel.setBackground(new Color(0, 153, 153));
		SharePanel.setBounds(0, 279, 1213, 398);
		DashBoardFrame.getContentPane().add(SharePanel);
		SharePanel.setLayout(null);

		JButton btnReceiveFile = new JButton("Receive File");
		btnReceiveFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnReceiveFile.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		btnReceiveFile.setBackground(new Color(255, 255, 255));
		btnReceiveFile.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 29));
		btnReceiveFile.setBounds(125, 232, 298, 71);
		SharePanel.add(btnReceiveFile);

		JPanel DownloadPanel = new JPanel();
		DownloadPanel.setBackground(new Color(255, 255, 255));
		DownloadPanel.setBounds(771, 24, 371, 337);
		SharePanel.add(DownloadPanel);
		DownloadPanel.setLayout(null);

		JButton btnStartDownload = new JButton("CHOOSE ACTION");
		btnStartDownload.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnStartDownload.setForeground(new Color(32, 178, 170));
		btnStartDownload.setEnabled(false);
		btnStartDownload.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 22));
		btnStartDownload.setBounds(49, 264, 279, 43);
		DownloadPanel.add(btnStartDownload);

		JLabel lblNoMetadataFile = new JLabel("Choose a Action!");
		lblNoMetadataFile.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNoMetadataFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoMetadataFile.setBounds(10, 10, 383, 53);
		DownloadPanel.add(lblNoMetadataFile);

		JLabel lblFileNameHead = new JLabel("File Name-");
		lblFileNameHead.setForeground(new Color(0, 153, 153));
		lblFileNameHead.setFont(new Font("FreeSerif", Font.BOLD, 20));
		lblFileNameHead.setBounds(10, 82, 172, 43);
		DownloadPanel.add(lblFileNameHead);

		JLabel lblFileName = new JLabel("GetFileName");
		lblFileName.setFont(new Font("Times New Roman", Font.BOLD, 17));
		lblFileName.setBounds(138, 82, 233, 43);
		DownloadPanel.add(lblFileName);

		JLabel lblFileSizeHead = new JLabel("File size-");
		lblFileSizeHead.setForeground(new Color(0, 139, 139));
		lblFileSizeHead.setFont(new Font("FreeSerif", Font.BOLD, 20));
		lblFileSizeHead.setBounds(10, 171, 172, 43);
		DownloadPanel.add(lblFileSizeHead);

		JLabel lblGetfilesize = new JLabel("GetFileSize\r\n");
		lblGetfilesize.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblGetfilesize.setBounds(138, 171, 233, 43);
		DownloadPanel.add(lblGetfilesize);

		JButton btnPublishFile = new JButton("Publish File");
		btnPublishFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPublishFile.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		btnPublishFile.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		btnPublishFile.setBackground(Color.WHITE);
		btnPublishFile.setBounds(125, 97, 298, 71);
		SharePanel.add(btnPublishFile);

		JPanel MyFilesPanel = new JPanel();
		MyFilesPanel.setBackground(new Color(0, 153, 153));
		// MyFilesPanel.setBackground(new Color(0, 153, 153));
		MyFilesPanel.setBounds(0, 279, 1213, 398);
		DashBoardFrame.getContentPane().add(MyFilesPanel);
		MyFilesPanel.setLayout(null);

		JLabel lblListOfFile = new JLabel("LIST OF FILE YOU HAVE IN .P2P FOLDER");
		lblListOfFile.setForeground(new Color(255, 255, 255));
		lblListOfFile.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblListOfFile.setBounds(224, 30, 834, 39);
		MyFilesPanel.add(lblListOfFile);
		MyFilesPanel.setVisible(false);

		JPanel ProcessPanel = new JPanel();
		ProcessPanel.setBackground(new Color(0, 153, 153));
		// ProcessPanel.setBackground(new Color(0, 153, 153));
		ProcessPanel.setBounds(0, 279, 1213, 398);
		DashBoardFrame.getContentPane().add(ProcessPanel);
		ProcessPanel.setLayout(null);
		progressbar = new JProgressBar();
		progressbar.setFont(new Font("Tahoma", Font.PLAIN, 52));
		progressbar.setForeground(new Color(51, 204, 204));
		progressbar.setBounds(475, 20, 674, 100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		ProcessPanel.add(progressbar);

		JLabel lblFile1 = new JLabel("");
		lblFile1.setForeground(Color.WHITE);
		lblFile1.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblFile1.setBounds(84, 48, 381, 41);
		ProcessPanel.add(lblFile1);

		JLabel lblFile2 = new JLabel("");
		lblFile2.setFont(new Font("Times New Roman", Font.BOLD, 21));
		lblFile2.setForeground(Color.WHITE);
		lblFile2.setBounds(84, 181, 381, 41);
		ProcessPanel.add(lblFile2);

		JLabel lblFile3 = new JLabel("");
		lblFile3.setForeground(Color.WHITE);
		lblFile3.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblFile3.setBounds(84, 292, 381, 41);
		ProcessPanel.add(lblFile3);
		ProcessPanel.setVisible(false);

		btnShare.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnMyFiles.setBorder(null);
				btnProcess.setBorder(null);
				btnShare.setBorder(new MatteBorder(6, 6, 0, 6, (Color) new Color(0, 0, 0)));
				// btnShare.setBorder(new LineBorder(new Color(0,0,0)));
				btnShare.setBackground(new Color(0, 153, 153));
				btnProcess.setBackground(new Color(255, 255, 255));
				btnMyFiles.setBackground(new Color(255, 255, 255));
				SharePanel.setVisible(true);
				ProcessPanel.setVisible(false);
				MyFilesPanel.setVisible(false);
			}
		});
		btnMyFiles.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				btnShare.setBorder(null);
				btnMyFiles.setBorder(new MatteBorder(6, 6, 0, 6, (Color) new Color(0, 0, 0)));
				btnShare.setBackground(new Color(255, 255, 255));
				btnProcess.setBackground(new Color(255, 255, 255));
				btnMyFiles.setBackground(new Color(0, 153, 153));
				btnProcess.setBorder(null);
				// btnMyFiles.setBorder(new LineBorder(new Color(0,0,0)));
				SharePanel.setVisible(false);
				ProcessPanel.setVisible(false);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(50, 100, 1100, 280);
				scrollPane.setBackground(new Color(0, 153, 153));
				MyFilesPanel.add(scrollPane);
				JLabel headerHead = new JLabel("File Name");
				headerHead.setFont(new Font("Times New Roman", Font.BOLD, 22));
				scrollPane.setColumnHeaderView(headerHead);
				ArrayList<Pair<String, Integer>> myFiles = peer.getMyFiles();
				Integer numFiles = myFiles.size();
				String filedesc[][] = new String[numFiles][2];
				Integer index = 0;
				for (Pair<String, Integer> myfile : myFiles) {
					System.out.println(myfile.getValue0());
					System.out.println(myfile.getValue1());
					filedesc[index][0] = myfile.getValue0();
					filedesc[index][1] = Integer.toString(myfile.getValue1());
					index++;
				}
				String filecolname[] = { "FileName", "Size" };
				JTable myFilesTable = new JTable(filedesc, filecolname);
				myFilesTable.setFont(new Font("FreeSerif", Font.PLAIN, 30));
				myFilesTable.getTableHeader().setFont(new Font("FreeSerif", Font.BOLD, 20));
				myFilesTable.setRowHeight(50);
				TableColumn sizeColumn = myFilesTable.getColumnModel().getColumn(0);
				sizeColumn.setPreferredWidth(600);
				myFilesTable.setEnabled(false);
				scrollPane.setViewportView(myFilesTable);
				scrollPane.setBackground(new Color(0, 139, 139));
				MyFilesPanel.setVisible(true);
			}
		});
		btnProcess.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnMyFiles.setBorder(null);
				btnProcess.setBorder(new MatteBorder(6, 6, 0, 6, (Color) new Color(0, 0, 0)));
				btnShare.setBackground(new Color(255, 255, 255));
				btnProcess.setBackground(new Color(0, 153, 153));
				btnMyFiles.setBackground(new Color(255, 255, 255));
				btnShare.setBorder(null);
				// btnProcess.setBorder(new LineBorder(new Color(0,0,0)));
				SharePanel.setVisible(false);
				MyFilesPanel.setVisible(false);
				ProcessPanel.setVisible(true);
			}
		});

		btnPublishFile.addMouseListener(new MouseAdapter() {

			@Override

			public void mouseClicked(MouseEvent e) {
				JFileChooser publishFileChoose = new JFileChooser(System.getProperty("user.home"));

				int r = publishFileChoose.showSaveDialog(null);
				publishFileName = publishFileChoose.getSelectedFile().getAbsolutePath();
				if (r == JFileChooser.APPROVE_OPTION) {
					// set the label to the path of the selected file
					if (System.getProperty("os.name").contains("Win")) {
						String[] filePathArray = publishFileName.split("\\\\");
						publishFileName = new String();
						for (String s : filePathArray) {
							publishFileName = '/' + s;
						}
					}
					peer.Publish(publishFileName);
					lblFileName.setText(publishFileChoose.getSelectedFile().getAbsolutePath());
					lblNoMetadataFile.setFont(new Font("Times New Roman", Font.BOLD, 16));
					lblNoMetadataFile.setText("File Selected Successfully!");
					lblNoMetadataFile.setForeground(new Color(0, 153, 153));

				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnPublishFile.setBorder(new LineBorder(new Color(0, 153, 153), 3));
				btnPublishFile.setForeground(new Color(0, 153, 153));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnPublishFile.setBorder(new LineBorder(new Color(0, 0, 0), 3));
				btnPublishFile.setForeground(new Color(0, 0, 0));
			}
		});

		btnReceiveFile.addMouseListener(new MouseAdapter() {
			@Override

			public void mouseClicked(MouseEvent e) {
				JFileChooser metadataChoose = new JFileChooser(System.getProperty("user.home"));
				int r = metadataChoose.showSaveDialog(null);
				if (r == JFileChooser.APPROVE_OPTION) {
					// set the label to the path of the selected file
					String actualFileName[]=(metadataChoose.getSelectedFile().getName()).split(".metadata");
					lblFileName.setText(actualFileName[0]);
					lblFile1.setText(actualFileName[0]);
					btnStartDownload.setEnabled(true);
					btnStartDownload.setText("Start Download");
					lblNoMetadataFile.setFont(new Font("Times New Roman", Font.BOLD, 16));
					lblNoMetadataFile.setText("METADATA SUCCESSFULLY SELECTED!");
					lblNoMetadataFile.setForeground(new Color(0, 153, 153));
					metaFileName = metadataChoose.getSelectedFile().getAbsolutePath();
					if (System.getProperty("os.name").contains("Win")) {
						String[] filePathArray = metaFileName.split("\\\\");
						metaFileName = new String();
						for (String s : filePathArray) {
							metaFileName += '/' + s;
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnReceiveFile.setBorder(new LineBorder(new Color(0, 153, 153), 3));
				btnReceiveFile.setForeground(new Color(0, 153, 153));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnReceiveFile.setBorder(new LineBorder(new Color(0, 0, 0), 3));
				btnReceiveFile.setForeground(new Color(0, 0, 0));
			}
		});

		lblLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblLogout.setForeground(new Color(0, 153, 153));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblLogout.setForeground(new Color(0, 0, 0));
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				int getStatus = JOptionPane.showConfirmDialog(lblLogout, "Do you want to surely logout??");
				if (getStatus == 0) {
					String logout[] = { "onkar", "onkar" };
					peer.Connect("LOGOUT", logout);
					Login login = new Login(new Peer());
					DashBoardFrame.setVisible(false);
					login.LoginFrame.setVisible(true);
				}

			}
		});

		btnStartDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						startProgressBar();
					}
				}).start();
			}
		});
	}
	public void startProgressBar() {
		float percentageTransferred = 0;
		FileLeecher fileLeecher = peer.leechFile(metaFileName);
		float totalPieces =(float) fileLeecher.getPendingPieces().size();
		try {
			while (percentageTransferred<100) {
				float pendingPieces = (float) fileLeecher.getPendingPieces().size();
				// fill the menu bar
				//System.out.println("Pending Piece = "+pendingPieces+"\ntotal Pieces"+totalPieces);
				percentageTransferred=(1-(float)(pendingPieces/totalPieces))*100;
				//System.out.println("percentage transfer = "+percentageTransferred+"");
				progressbar.setValue((int)percentageTransferred);
				
				// delay the thread
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}
	}
}