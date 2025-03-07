package pkgClient;

import pkgPanel.Controller;
import pkgPanel.Panel;

import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class NetworkClient{
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    /*
     * Register host and port.
     * Connection won't actually be established until call connect()
     */

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /*
     * The easy part. Connecting to server is almost always same
     * Call Socket constructor,
     * use try/catch blocks for UnknownHostException and IOException,
     * close Socket
     */
    public void conect(){
        try {
            Socket client = new Socket(host, port);
            JFrame frame = new JFrame("Sudoku Game");
            Controller controller = new Controller(frame);
            Panel panel = new Panel(frame, controller);
            handleConnection(client, panel);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + host);
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
            ioe.printStackTrace();
        }
    }

    /*
     * The method to override
     * when making a network client for a task
     */
    protected abstract void handleConnection(Socket client, Panel panel) throws IOException;
}
