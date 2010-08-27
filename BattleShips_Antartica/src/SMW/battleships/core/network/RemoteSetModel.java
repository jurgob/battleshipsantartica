package SMW.battleships.core.network;

import SMW.battleships.core.BattleShips;

/**
 * 
 * @author jurgo
 *incapsula l'oggetto che viene trasferito in rete
 *
 */

public class RemoteSetModel extends RemoteAction{
	public final BattleShips bs;  // Informazione immutabile, liberamente accessibile
	  
	  
	  public RemoteSetModel( BattleShips bs ) {
	  
	    this.bs = bs;
	  }
}
	  
	  
