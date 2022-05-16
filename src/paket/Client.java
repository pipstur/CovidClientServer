package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client implements Runnable {

	static Socket soketZaKomunikaciju = null;
	static BufferedReader serverInput = null;
	static PrintStream serverOutput = null;
	static BufferedReader unosSaTastature = null;
	public static void main(String[] args) {
		try {
			soketZaKomunikaciju = new Socket("localhost", 9001);
			serverInput = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			serverOutput = new PrintStream(soketZaKomunikaciju.getOutputStream());
			
			unosSaTastature = new BufferedReader(new InputStreamReader(System.in));
			
			new Thread(new Client()).start();
			
			String input;
			
			while(true) {
				input = serverInput.readLine();
				System.out.println(input);
				if(input.startsWith(">>>Dovidjenja")) {
					break;
				}
			}
			soketZaKomunikaciju.close();
		} catch (IOException e) {
			System.out.println("SERVER JE PAO OOOOo");
		}
	}

	@Override
	public void run() {
		String message;
		while(true) {
			try {
				message = unosSaTastature.readLine();
				serverOutput.println(message);
				if(message.equals("***quit")) {
					soketZaKomunikaciju.close();
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
		}
		
	}

}
