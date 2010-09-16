package SMW.battleships.core.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.util.Log;

public class BSServer  extends ServerSocket {


	  private static final int PORT = 5554;       // Come NimClient
	  
	  private static final int TIMEOUT = 600000;  // msec  = 10 minuti
	  
	  
	  // Costruttore:
	  
	  public BSServer()  throws IOException {
	  
	    super( PORT );
	    setSoTimeout( TIMEOUT );
	    Log.i("I", "server initialized");
	  }
	  
	  
	  public RemoteBSUser getBSUser()  throws SocketTimeoutException, IOException {
		Log.i("I", "server waiting for client");
	    Socket socket = accept();
	    Log.i("I", "client connected");

	    return  new RemoteBSUser( socket );
	  }

	}  // class NimServer
