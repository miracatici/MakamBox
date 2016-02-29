package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Server {

	public static void main(String[] args) {
		BufferedWriter bw = null ;
		try {
			bw = new BufferedWriter(new FileWriter("/var/www/html/test2.txt"));
			int a = 0;
			while (true){
				bw.write(String.valueOf(a));
				a++;
				bw.flush();
				Thread.sleep(2000);
			}
		} catch (IOException e) {
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
