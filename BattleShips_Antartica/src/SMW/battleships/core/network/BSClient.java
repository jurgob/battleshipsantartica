package SMW.battleships.core.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.OptionValues;

public class BSClient {


	  private static final int PORT = 5002;                            // Come NimServer
	   
	  private class ClientSession  extends Transceiver  implements Runnable {
	    // Costruttore:
	    public ClientSession( Socket socket ) {
	    
	      super( socket );
	      
	      ( new Thread(this) ).start();                                // Avvio ascolto socket
	    }
	    // Thread di ascolto del socket
	    public void run() {
	      do {                                                         // Socket event loop
	      
	        Object action = receive();                                 // <== server
	        
	        if( action instanceof RemoteSetModel ) {
	        
	          proxy.mirror( ((RemoteSetModel) action).bs );   
	          
	          user.setModel( proxy );                                  // Inizializzazione
	          
	          send( new RemoteAck() );                                 // ==> server: ack
	        
	        } else if ( action instanceof RemoteUpdate   ) {
	        
	          proxy.mirror( (BattleShips) ((RemoteUpdate) action).obs );       // Aggiornamento stato
	          
	          user.update( proxy, ((RemoteUpdate) action).obj );       // Notifica osservatori
	        
	        } else if ( action instanceof RemoteSuggest  ) {
	        
	          // ( (RemoteSuggest) action ).nim  non utilizzato
	          
	          send( strategy.suggest(proxy) );                         // ==> server: risposta
	        }
	      } while ( !proxy.over() );
	      
	      close();                                                     // Gioco concluso
	      
	      setChanged();                                                // Notifica chiusura
	      notifyObservers();                                           // della sessione
	    }
	  
	  }  // inner class ClientSession
	  
	  
	  private static class BSProxy  extends BattleShips {
	  
	    private BattleShips bs = null;
	  
	    public BSProxy(int x, int y) {
	    
	      super( x,y );  // Stato interno ereditato da Nim non utilizzato
	    }
	    
	    // Protocollo di Nim, che rimanda alla copia incapsulata
	    
	    public int getXSize() {
	    
	      return bs.getXSize();
	    }
	    
	    public int getYSize() {
	    
	      return bs.getYSize();
	    }
    
	    public boolean over() {
	    
	      return bs.over();
	    }
	    
	    
	    void mirror( BattleShips bs ) {
	     
	      this.bs = bs;
	    }
	  
	  }  // nested class NimProxy


	  private final BSProxy proxy = new BSProxy(OptionValues.getRows(), OptionValues.getColumns());
	  
	  private final BSUser user;
	  private final BSStrategy strategy;
	  
	  
	  // Costruttore:
	  
	  public BSClient( BSUser user ) {
	  
	    this.user = user;
	    strategy  = user.userStrategy();
	  }
	  
	  
	  public Observable startSession( String addr )  throws IOException {
	  
	    Socket socket = new Socket( InetAddress.getByName(addr), PORT );
	    
	    return  new ClientSession( socket );
	  }

	} 