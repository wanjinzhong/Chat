package wanjinzhong;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownload {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL("http://img2.3lian.com/2014/c7/12/d/10.jpg");
			URLConnection connect = url.openConnection();
			DataInputStream in = new DataInputStream(connect.getInputStream());
			File file = new File("3.jpg");
			OutputStream out = new FileOutputStream(file);
			byte[] b = new byte[4096];
			int length = 0;
			while ((length = in.read(b)) > 0) {
				out.write(b, 0, length);
				out.flush();
			}
			in.close();
			out.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
