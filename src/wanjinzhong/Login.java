package wanjinzhong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Login extends JFrame {
	private static DataInputStream in;
	private static DataOutputStream out;

	private JLabel nameLabel = new JLabel("用户名");
	private JLabel pwdLabel = new JLabel("密码");
	private JTextField name = new JTextField();
	private JPasswordField pwd = new JPasswordField();
	private JButton loginBtn = new JButton("登陆");
	private JButton registBtn = new JButton("注册");

	public Login() {
		init();
		Socket s;
		try {
			s = new Socket("wanjinzhong.imwork.net", 48552);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		loginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					for (char c : name.getText().toCharArray()) {
						if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c))) {
							JOptionPane.showMessageDialog(null, "用户名只能是英文或数字！");
							return;
						}
					}
					out.writeInt(1);
					out.writeUTF(name.getText());
					out.writeUTF(String.valueOf(pwd.getPassword()));
					switch (in.readInt()) {
					case 401:
						JOptionPane.showMessageDialog(null, "没有该用户！");
						break;
					case 200:
						Client client = new Client(name.getText());
						client.setVisible(true);
						Login.this.setVisible(false);
						break;
					case 402:
						JOptionPane.showMessageDialog(null, "用户名或密码错误！");
						break;
					case 500:
						JOptionPane.showMessageDialog(null, "服务器错误");
						break;
					}

				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		registBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					for (char c : name.getText().toCharArray()) {
						if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c))) {
							JOptionPane.showMessageDialog(null, "用户名只能是英文或数字！");
							return;
						}
					}
					out.writeInt(2);
					out.writeUTF(name.getText());
					out.writeUTF(String.valueOf(pwd.getPassword()));
					switch (in.readInt()) {
					case 401:
						JOptionPane.showMessageDialog(null, "用户名已存在");
						break;
					case 200:
						JOptionPane.showMessageDialog(null, "注册成功");
						break;
					case 500:
						JOptionPane.showMessageDialog(null, "服务器错误");
						break;
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

	}

	private void init() {
		JPanel jp1 = new JPanel();
		jp1.setLayout(new GridLayout(2, 2));
		pwdLabel.setSize(50, 30);
		jp1.add(nameLabel);
		jp1.add(name);
		jp1.add(pwdLabel);
		jp1.add(pwd);
		JPanel jp2 = new JPanel();
		jp2.add(loginBtn);
		jp2.add(registBtn);
		name.setSize(100, 30);
		pwd.setSize(100, 30);
		setLayout(new BorderLayout());
		add(jp1, BorderLayout.CENTER);
		add(jp2, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Login login = new Login();
		login.setSize(400, 150);
		login.setDefaultCloseOperation(EXIT_ON_CLOSE);
		login.setTitle("登陆/注册");
		login.setLocationRelativeTo(null);
		login.setVisible(true);
	}

}
