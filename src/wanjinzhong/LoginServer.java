package wanjinzhong;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Properties;

public class LoginServer {
	private static ServerSocket server;
	private static Socket socket;
	private static DataInputStream in;
	private static DataOutputStream out;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			server = new ServerSocket(1236);
			while (true) {
				socket = server.accept();
				// clientList.add(socket);
				System.out.println("有新客户机连入。");
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				new Thread(new Operate()).start();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	static class Operate implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (true) {
					int type = in.readInt();
					String name = in.readUTF();
					System.out.println(name);
					String pwd = in.readUTF();
					System.out.println(pwd);
					if(type == 1){
						out.writeInt(check(name,pwd));
					}else if(type == 2){
						out.writeInt(regist(name,pwd));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
		}
		
		public int regist(String name, String pwd){

			Properties proIn = new Properties();
			try {
				proIn.load(new FileInputStream("C:\\users.properties"));
				if (proIn.containsKey(name))
					return 401;
				RandomAccessFile file = new RandomAccessFile("C:\\users.properties","rw");
				file.seek(file.length());
				file.writeBytes("\r\n" + name + "=" + pwd);
				file.close();
				return 200;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("异常");
				return 500;
			}
		}

		public int check(String name, String pwd) {
			Properties pro = new Properties();
			try {
				pro.load(new FileInputStream("C:\\users.properties"));
				if (!pro.containsKey(name))
					return 401;
				if (pro.getProperty(name).equals(pwd)){
					System.out.println(pro.getProperty(name));
					return 200;
				}
				else return 402;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("异常");
				return 500;
			}
		}
	}

}
