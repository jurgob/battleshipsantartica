package SMW.battleships.core;

import SMW.battleships.core.BattleShips.InvalidMoveException;
import SMW.battleships.core.BattleShips.Player;

public class BSPlayer {
	private final BattleShips bs;
	private final BSStrategy strategy;
	public Player player;
	
	public BSPlayer(BattleShips bs, BSStrategy s){
		this.bs=bs;
		this.strategy=s;
	}
	
	public void play() throws Exception{
		if(!bs.over()){
			//TODO: suggest also dispose ship
			BattleShips.Move move= strategy.suggest(bs);
			bs.shot(move);
			
		}
		
	}	
	public void addShip(){
		if(!bs.over() ){
			BattleShips.DisposeShip move =strategy.suggestDisposeShip(bs);
			move.p=this.player;
			bs.disposeShip(move);
			
		}
			
		
	}
}
