import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectionHandler extends Thread{
  private Socket socket;
  private String remoteHost = "localhost"; //default
  private int port = 42028; //default
  private boolean connectionAlive;
  private boolean running = true;
  private OutputStream out;
  private InputStream in;
  private ObjectInputStream objIn;
  private DataOutputStream dataOut;
  
  
  
  
  public ConnectionHandler(String remoteHost, int port) {
    super();
    this.remoteHost = remoteHost;
    this.port = port;
  }

  public void run() {
    connectionAlive = false;
    while(running) {
      if (connectionAlive) {
        try {
          while(objIn.available() <= 0) {}
          PowerGridClientMain.gsReadable = false;
          PowerGridClientMain.gamestate = (Gamestate) objIn.readObject();
          PowerGridClientMain.gsReadable = true;
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } 
      else {
        connectionAlive = establishConnection();
        if (connectionAlive) {
          continue;
        }
      }

      
    }
  }
  
  public boolean establishConnection() {
    try {
        socket = new Socket(remoteHost, port);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        objIn = new ObjectInputStream(in);
        dataOut = new DataOutputStream(out);
    } catch (UnknownHostException e) {
        System.out.println("error unknown host exception");
        return false;
    } catch (IOException e) {
        System.out.println("connection failed");
        return false;
    } 
    System.out.println("connection successful to "+remoteHost);
    return true;
  }
  
  
}
