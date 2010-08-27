package SMW.battleships.core.network;

import java.util.Observable;

public class RemoteUpdate  extends RemoteAction {

	  public final Observable obs;
	  public final Object obj;
	  
	  
	  public RemoteUpdate( Observable obs, Object obj ) {
	  
	    this.obs = obs;
	    this.obj = obj;
	  }

	}  // class RemoteUpdate