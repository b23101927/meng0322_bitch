import java.net.Socket;

public class ClientData {
	Socket socket;
	
	public ClientData(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
