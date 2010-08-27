package SMW.battleships.core;

import java.util.ArrayList;
import java.util.List;

import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.BattleShips.Shot;

public class SequentialStrategy implements BSStrategy {
	static int x=0;
	static int y=0;
	private List<BattleShips.DisposeShip> disposeShipMoves;
	
	public SequentialStrategy() {
		// TODO Auto-generated constructor stub
		disposeShipMoves=new ArrayList<BattleShips.DisposeShip>();
		disposeShipMoves.add(new DisposeShip(1,1, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,2, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,3, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,5, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,6, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(8,0, InsertOrientation.VERTICAL, null));
		
		

	}
	
	@Override
	public  Shot suggest(BattleShips bs) {
		if(x==bs.getXSize()){
			y++;
			x=0;
		}
		Shot m= new Shot(x,y);
		x++;

		return m;
	}

	@Override
	public DisposeShip suggestDisposeShip(BattleShips bs) {
		// TODO Auto-generated method stub
		int location=disposeShipMoves.size()-1;
		DisposeShip m=disposeShipMoves.get(location);
		disposeShipMoves.remove(location);
		return m;
	
	}

}
