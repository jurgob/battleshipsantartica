package SMW.battleships.core;

import java.util.Observable;
import java.util.Observer;

import SMW.battleships.core.BattleShips.Player;
import android.os.BatteryManager;

public interface BSUser extends Observer {
	
	public void setModel(BattleShips bs);
	public BSStrategy userStrategy();
	public void setPlayer(Player p);
	
}
