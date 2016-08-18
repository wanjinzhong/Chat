package wanjinzhong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client extends JFrame {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private Scanner input;
	private String name;
	private JTextArea msgArea = new JTextArea();
	private JTextArea inputArea = new JTextArea();
	private JButton sendBtn = new JButton("∑¢ÀÕ");
	private JScrollPane msgScroll = new JScrollPane(msgArea);
	private JScrollPane inputScroll = new JScrollPane(inputArea);
	private JComboBox<String> sendMethodBox = new JComboBox<String>(new String[] { "Enter∑¢ÀÕ", "Alt+Enter∑¢ÀÕ" });
	private JList friendList = new JList();
	public Client(String name) {
		this.name = name;
		init();
		start();

	}

	private void init() {
		JPanel jp1 = new JPanel();
		jp1.setLayout(new BorderLayout());
		jp1.add(inputScroll, BorderLayout.CENTER);
		JPanel jp2 = new JPanel();
		jp2.add(sendMethodBox);
		jp2.add(sendBtn);
		jp1.add(jp2, BorderLayout.SOUTH);
		jp1.setPreferredSize(new Dimension(100, 100));
		setLayout(new BorderLayout());
		add(msgScroll, BorderLayout.CENTER);
		add(jp1, BorderLayout.SOUTH);
		msgArea.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 15));
		msgArea.setBorder(BorderFactory.createTitledBorder("œ˚œ¢"));
		inputArea.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 15));
		inputArea.setBorder(BorderFactory.createTitledBorder(" ‰»Î"));
		inputArea.setMaximumSize(new Dimension(1920, 100));
		msgArea.setEditable(false);
		sendBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				send();
			}
		});

		inputArea.addKeyListener(new SendMethod());
		this.setTitle("¡ƒÃÏ∆˜ - " + name);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 500);
		this.setMinimumSize(new Dimension(500, 300));
		this.setLocationRelativeTo(null);
	}

	void send() {
		String message = inputArea.getText();
		if (message.equals(""))
			return;
		msgArea.append("Œ“£∫\n" + message + "\n\n");
		inputArea.setText("");
		try {
			out.writeUTF(message);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void start() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket("wanjinzhong.imwork.net", 47070);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(name);
			// new Thread(new Send()).start();
			new Thread(new Recieve()).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// static class Send implements Runnable {
	// String message;
	// @Override
	// public void run() {
	// try {
	// while (true) {
	// message = input.nextLine();
	// out.writeUTF(message);
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

	class Recieve implements Runnable {
		public void run() {
			try {
				while (true) {
					String name = in.readUTF();
					String msg = in.readUTF();
					msgArea.append(MessageFormat.format("{0}\n{1}\n\n", name, msg));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class SendMethod extends KeyAdapter {
		
		public void keyPressed(KeyEvent key) {
			System.out.println(sendMethodBox.getSelectedIndex());
			int index = sendMethodBox.getSelectedIndex();
			if (index == 0) {
				if ((key.getKeyCode() == KeyEvent.VK_ENTER) && key.isAltDown()) {
					inputArea.append("\n");
				} else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
					System.out.println("∑¢ÀÕ");
					inputArea.setText("");
				}
			}
			if (index == 1) {
				if (key.getKeyCode() == KeyEvent.VK_ENTER && key.isAltDown()) {
					send();
				} 
			}
			
		}
	}
}
