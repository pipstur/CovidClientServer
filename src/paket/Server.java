package paket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	public static List<ClientHandler> onlineUsers = new ArrayList<>();
	
	public static void main(String[] args) {
		int port = 9001;
		ServerSocket serverSoket = null;
		Socket soketZaKomunikaciju = null;
		try {
			serverSoket = new ServerSocket(port);
			while(true) {
				System.out.println("Cekam na konekciju...");
				soketZaKomunikaciju = serverSoket.accept();
				System.out.println("Doslo je do konekcije!");
				
				ClientHandler klijent = new ClientHandler(soketZaKomunikaciju);
				
				//onlineUsers.add(klijent);
				
				klijent.start();
				
			}
	
		}
		catch (IOException e) {
			
			System.out.println("Greska prilikom pokretanja servera!");
		}
	}
}

