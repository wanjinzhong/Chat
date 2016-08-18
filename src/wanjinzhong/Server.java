package wanjinzhong;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.*;

public class Server {
	private static ServerSocket server;
	private static Socket socket;
	private static HashMap<Socket, String> clientMap;
	private static String name;
	private static String message;
	private static Scanner input;
	private static DataInputStream in;
	private static final int SYSTEM_MSG = 1;
	private static final int NORMAL_MSG = 0;
	
	// private static ArrayList<Socket> clientList;

	public static void main(String[] args) {
		clientMap = new HashMap<Socket, String>();
		// TODO Auto-generated method stub
		try {
			server = new ServerSocket(1235);
			while (true) {
				socket = server.accept();
				in = new DataInputStream(socket.getInputStream());
				name = in.readUTF();
				clientMap.put(socket, name);
				new Thread(new Server().new Recieve(socket, name)).start();
				notifyAll(SYSTEM_MSG, MessageFormat.format("{0}已上线，当前在线人数{1}人", name, clientMap.size()),socket);
				System.out.println(
						MessageFormat.format("{0}({1}:{2})连入", name, socket.getInetAddress(), socket.getPort()));
				// new Thread(new Send()).start();

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public static synchronized void notifyAll(int type,String msg, Socket me) {
		Iterator<Socket> it = clientMap.keySet().iterator();
		String time = getTime();
		String title = null;
		try {
			while (it.hasNext()) {
				Socket s = it.next();
				if (me != s) {
					DataOutputStream o = new DataOutputStream(s.getOutputStream());
					switch(type){
					case NORMAL_MSG:title = clientMap.get(me) +" "+ time;break;
					case SYSTEM_MSG:title = "系统消息";break;
					}
					o.writeUTF(title);
					o.writeUTF(msg);
				}
			}
			String writeString = title + "\r\n" + msg;
			writeFile(writeString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getTime(){
		Calendar c = Calendar.getInstance();
		return MessageFormat.format("{0}-{1}-{2} {3}:{4}:{5}",
				c.get(Calendar.YEAR)+"",c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)); 
	}
	public static void writeFile(String msg){
		try {
			FileOutputStream fileOut = new FileOutputStream(new File("D:\\messageRecord.txt"));
			fileOut.write(msg.getBytes());
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// static class Send implements Runnable {
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
		private  Socket socket;
		private  String name;
		private DataInputStream getdata;

		public Recieve(Socket socket, String name) {
			this.socket = socket;
			this.name = name;
			try {
				getdata = new DataInputStream(this.socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public  void run() {
			try {
				while (true) {
					String msg = getdata.readUTF();
					Server.notifyAll(NORMAL_MSG,msg,this.socket);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				clientMap.remove(socket);
				Server.notifyAll(SYSTEM_MSG,MessageFormat.format("{0}已下线，当前在线人数{1}人", name, clientMap.size()),this.socket);
			}
		}

		
	}

}
