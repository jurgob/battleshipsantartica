package SMW.battleships.core.network;



import java.util.*;
import java.io.*;
import java.net.*;


public class Transceiver  extends Observable {


  private static final int TIMEOUT = 300000;    // Timeout: 5 minuti
  
  
  private final Socket socket;
  
  private ObjectInputStream  input  = null;     // Canali di comunicazione
  private ObjectOutputStream output = null;
  
  
  // Costruttore:
  
  protected Transceiver( Socket socket ) {
  
    this.socket  = socket;
    
    try {
    
      socket.setSoTimeout( TIMEOUT );
    
      output = new ObjectOutputStream( socket.getOutputStream() );
      input  = new ObjectInputStream( socket.getInputStream() );
      
      trace( socket.getInetAddress() + ": socket streams opened" );
    
    } catch ( IOException ioe ) {
      
      trace( socket.getInetAddress() + ": failure opening socket streams (" + ioe + ")" );
      close();
    }
  }
  
  
  // Comunicazione client-server
  
  protected void send( Object obj ) {
  
    try {
    
      output.writeObject( obj );
      output.flush();                           // Invio immediato
      output.reset();                           // 'senza memoria'
      
      trace( "sent: " + obj );
    
    } catch ( IOException ioe ) {
    
      trace( socket.getInetAddress() + ": send failure (" + ioe + ")" );
      close();
    }
  }
  
  
  protected Object receive() {
  
    try {
    
      Object obj = input.readObject();
      
      trace( "received: " + obj );
      
      return obj;
    
    } catch ( IOException ioe ) {
    
      trace( socket.getInetAddress() + ": receive failure (" + ioe + ")" );
      close();
    
    } catch ( ClassNotFoundException cnfe ) {
    
      trace( socket.getInetAddress() + ": received unknown object (" + cnfe + ")" );
      close();
    }
    return null;
  }
  
  
  protected void close() {
  
    try {
    
      socket.close();
      
      trace( socket.getInetAddress() + ": socket closed" );
    
    } catch ( IOException ioe ) {}
  }
  
  
  // Feedback eventi relativi alla rete
  
  private void trace( String event ) {
    
    setChanged();
    notifyObservers( event );
  }

}  // class Transceiver
