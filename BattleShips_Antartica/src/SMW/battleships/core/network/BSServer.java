package SMW.battleships.core.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class BSServer  extends ServerSocket {


	  private static final int PORT = 5321;       // Come NimClient
	  
	  private static final int TIMEOUT = 600000;  // msec  = 10 minuti
	  
	  
	  // Costruttore:
	  
	  public BSServer()  throws IOException {
	  
	    super( PORT );
	    
	    setSoTimeout( TIMEOUT );
	  }
	  
	  
	  public RemoteBSUser getBSUser()  throws SocketTimeoutException, IOException {
	  
	    Socket socket = accept();
	    
	    return  new RemoteBSUser( socket );
	  }

	}  // class NimServer
