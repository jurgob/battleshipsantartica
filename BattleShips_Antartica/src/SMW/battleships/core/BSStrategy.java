package SMW.battleships.core;

import SMW.battleships.core.BattleShips.Player;

public interface BSStrategy {
	
	
	public BattleShips.Shot suggest(BattleShips bs);	
	public BattleShips.DisposeShip suggestDisposeShip(BattleShips bs);
	
	
}
