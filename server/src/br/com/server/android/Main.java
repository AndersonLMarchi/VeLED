/*
 * This is a simple server application
 * This server receive a string message from the Android mobile phone
 * and show it on the console.
 * Author by Lak J Comspace
 * Edited by Anderson Luis Marchi
 * This server is used to receive a client message and show it on a LED panel with Arduino
 */

package br.com.server.android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	private static ServerSocket serverSocket;
	private static Socket clientSocket, server;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static DataOutputStream out;
	private static String message;

	public static void main(String[] args) throws IOException{

		try {
			serverSocket = new ServerSocket(12345);  //Server socket			
		} catch (IOException e) {
			System.out.println("Could not listen on port: 12345");
		}
		
		while (true) {
			try {
				clientSocket = serverSocket.accept();   //aceita a conexao com o cliente
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader); //pega a mensagem do cliente
				message = bufferedReader.readLine();
				String opcao = "";

				if(message.equals("TCelcius"))
					opcao = "1";
				else if(message.equals("TKelvin"))
					opcao = "2";                
				else if(message.equals("TFahrenheit"))
					opcao = "3";
				else if(message.equals("UmidRelAr"))
					opcao = "4";
				else                
					opcao = message;
				
				System.out.println(opcao);
				
				server = new Socket("192.168.1.177", 8080);
				out = new DataOutputStream(server.getOutputStream()); 	
				out.writeBytes(opcao);
//				out.writeChars(opcao);
//				out.writeUTF(opcao);
				out.close();
				server.close();
				inputStreamReader.close();
				clientSocket.close();

			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}		
	}
}