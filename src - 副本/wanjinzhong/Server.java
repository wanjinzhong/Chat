package wanjinzhong;

import java.io.*;
import java.net.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private static ServerSocket server;
	private static Socket socket;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static String message;
	private static Scanner input;
	private static ArrayList<Socket> clientList = new ArrayList<Socket>();
	
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		input = new Scanner(System.in);
		try {
			server = new ServerSocket(1235);
			while (true) {
				socket = server.accept();
				clientList.add(socket);
				System.out.println("有新客户机连入。");
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				new Thread(new Send()).start();
				new Thread(new Recieve()).start();
				

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	static class Send implements Runnable {
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
					System.out.println(MessageFormat.format("{0}:{1}  {2}",socket.getInetAddress(), socket.getPort(), in.readUTF()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
