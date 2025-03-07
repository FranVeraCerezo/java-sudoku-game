package pkgClient;

public class DemoClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9000;

        MyClient myClient = new MyClient(host, port);
        myClient.conect();
    }
}
