package wanjinzhong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Scanner;

public class Client {
	private static  Socket socket;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static Scanner input;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		input = new Scanner(System.in);
		try {
			socket = new Socket("127.0.0.1",1235);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			new Thread(new Send()).start();
			new Thread(new Recieve()).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class Send implements Runnable {
		String message;
		@Override
		public void run() {
			try {
				while (true) {
					message = input.nextLine();
					out.writeUTF(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static class Recieve implements Runnable {
		public void run() {
			try {
				while (true) {
					System.out.println(in.readUTF());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
