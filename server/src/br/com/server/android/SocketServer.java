package br.com.server.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {

	private final static Logger logger = Logger
			.getLogger(SocketServer.class.toString());

	private String host;
	private int port;
	private String folder;

	public SocketServer(String host, int port, String folder) {
		super();
		this.host = host;
		this.port = port;
		this.folder = folder;
	}

	public void serve() {
		ServerSocket serverSocket = null;

		logger.info("Iniciando servidor no endereço: " + this.host + ":"
				+ this.port);

		try {
			// Cria a conexão servidora
			serverSocket = new ServerSocket(port, 1,
					InetAddress.getByName(host));

			logger.info("Conexão com o servidor aberta no endereço: "
					+ this.host + ":" + this.port);

			// Fica esperando pela conexão cliente
			while (true) {
				logger.info("Aguardando conexões...");
				Socket socket = null;
				InputStream input = null;
				OutputStream output = null;
				try {
					socket = serverSocket.accept();
					input = socket.getInputStream();
					output = socket.getOutputStream();

					// Realiza o parse da requisição recebida
					String requestString = convertStreamToString(input);
					logger.info("Conexão recebida. Conteúdo:\n" + requestString);
					Path path = Paths.get(this.folder, new SimpleDateFormat(
							"yyyyMMddssSSS'.file'").format(new Date())
							.toString());
					Files.write(path, requestString.getBytes());

					String responseString = "OK";
					sendResponse(output, responseString);

				} catch (Exception e) {
					logger.log(Level.SEVERE, "Erro ao executar servidor!", e);
					sendResponse(output, "ERROR");
					continue;
				} finally {
					// Fecha a conexão
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (IOException e) {
						logger.log(Level.SEVERE,
								"Erro ao fechar socket servidor!", e);
						continue;
					}
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro ao iniciar servidor!", e);
			return;
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendResponse(OutputStream output, String responseString)
			throws IOException {
		logger.info("Resposta enviada. Conteúdo:\n"
				+ responseString);
		output.write(responseString.getBytes());
	}

	private String convertStreamToString(InputStream is) {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[2048];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int i = reader.read(buffer);
				writer.write(buffer, 0, i);
			} catch (IOException e) {
				logger.log(Level.SEVERE,
						"Erro ao converter stream para string", e);
				return "";
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Informe 3 parâmetros: Host, porta e caminho para geração dos arquivos.");
		} else {
			SocketServer sfw = new SocketServer(args[0],
					Integer.parseInt(args[1]), args[2]);
			sfw.serve();
		}
	}

}
