package SMW.battleships.core.network;

import java.net.Socket;
import java.util.Observable;

import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.BattleShips.Shot;

public class RemoteBSUser  extends Transceiver  implements BSUser {


	  private class RemoteStrategy  implements BSStrategy {
	  
	    // Protocollo di NimStrategy:
	    
	    public BattleShips.Shot suggest( BattleShips bs ) {
	    
	      send( new RemoteSuggest(null) );        // ==> client
	      
	      return ( (BattleShips.Shot) receive() );        // <== client: risposta
	    }

		@Override
		public DisposeShip suggestDisposeShip(BattleShips bs) {
			// TODO Auto-generated method stub
			return null;
		}
	  }
	  
	  
	  private RemoteStrategy strategy = null;     // Strategia dell'utente remoto
	  Player player=null;
	  
	  // Costruttore:
	  
	  public RemoteBSUser( Socket socket ) {
	    
	    super( socket );
	  }
	  
	  
	  // Realizzazione del protocollo specificato da NimUser
	  
	  public void setModel( BattleShips bs ) {
	  
	    send( new RemoteSetModel(bs) );          // ==> client
	    Object ack = receive();                   // <== client: acknowledgement
	    bs.addObserver( this );                  // Osservatore del modello
	  }
	  
	  
	  public BSStrategy userStrategy() {
	  
	    if ( strategy == null ) {
	    
	      strategy = new RemoteStrategy();
	    }
	    return strategy;
	  }
	  
	  
	  // Realizzazione del protocollo di Observer
	  @Override
	  public void update( Observable obs, Object obj ) {
	  
	    send( new RemoteUpdate(obs,obj) );        // ==> client: notify
	  }


	@Override
	public void setPlayer(Player p) {
		this.player=p;		
	}


	

	}  // class RemoteNimUser
