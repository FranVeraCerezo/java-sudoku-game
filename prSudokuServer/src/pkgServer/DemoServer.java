package pkgServer;

public class DemoServer{

	public static void main(String[] args) {
		int port = 9000;

		MyServer server = new MyServer(port);
		server.listen();
	}

}
