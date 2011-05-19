package SMW.battleships.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.test.IsolatedContext;
import android.util.Log;

import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Shot;
import SMW.battleships.core.BattleShips.State;

public class SmartStrategy implements BSStrategy{
	List<Shot> history= new ArrayList<Shot>();
	private List<BattleShips.DisposeShip> disposeShipMoves;
	BattleShips bs;
	State[][] field;
	int state=0;
	int[] suggX={-1,0,1,0};
	int[] suggY={0,1,0,-1};
	int tryDirection=0;
	Move firstHitted=null;
	boolean isNotRandomTry=false;
	boolean iKnowShipOrientation = false;
	boolean iHaveChangedDirection =true;
	
	public SmartStrategy() {
		disposeShipMoves=new ArrayList<BattleShips.DisposeShip>();
		disposeShipMoves.add(new DisposeShip(1,1, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,2, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,3, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,5, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(1,6, InsertOrientation.HORIZONTAL, null));
		disposeShipMoves.add(new DisposeShip(8,0, InsertOrientation.VERTICAL, null));
	}
	private   Shot randomShotSugggest(BattleShips bs){

		boolean originalMove=false;
		int x=0;
		int y=0;
		Shot m =null;
		Random r= new Random(System.currentTimeMillis());
		
		while(!originalMove){
//			x = ((int)Math.random()*bs.getXSize()*10 )%bs.getXSize();
//			y = ((int)Math.random()*bs.getYSize()*10 )%bs.getYSize();
			
			r.setSeed(System.currentTimeMillis());
			x =r.nextInt(bs.getXSize()-1);
			y = r.nextInt(bs.getYSize()-1);
			System.out.println("size: "+bs.getXSize()+" "+bs.getYSize() );
			System.out.println("dummy strategy generated: "+x+" "+y);
			m=new Shot(x, y);
			if(history.size()<1)originalMove=true;
			Iterator<Shot> itr= history.iterator();
			boolean alreadyUsed=false;
			while (itr.hasNext() && !alreadyUsed ) {
				BattleShips.Move move = (BattleShips.Move) itr.next();
				if(move.x==x && move.y==y){
					alreadyUsed=true;
				}	
			}
			if(!alreadyUsed) originalMove=true;
		
		}
		
		return m;
		
	}
	
	private boolean areValidCoord(int x, int y) {
		boolean res=true;
		
		if(x >= bs.getXSize()-1  || x <0) res=false;
		if(y >= bs.getYSize()-1 || y <0) res=false;
		if(field[x][y] == BattleShips.State.SHIP_HITTED) res=false;  
		if(field[x][y] == BattleShips.State.SEA_HITTED) res=false;
		return res;
		
	}
	
	@Override
	public  Shot suggest(BattleShips bs) {
		this.bs=bs;
		Shot m =null;
		Shot suggest =null;
		Shot lastMove=null;
		Shot last2Move=null;
		Shot last3Move=null;
		boolean lastHit = false;
		boolean last2Hit= false;
		boolean last3Hit= false;
		 field = bs.getField(BattleShips.getEnemy(bs.getCurrenPlayer()));
		if(history.size() > 0){ 
			lastMove = history.get(history.size()-1);
			if(field[lastMove.x][lastMove.y] == BattleShips.State.SHIP_HITTED ) lastHit=true;
			
		}
		if(history.size() > 1){ 
			last2Move = history.get(history.size()-2);
			if(field[last2Move.x][last2Move.y] == BattleShips.State.SHIP_HITTED ) last2Hit=true;
		}
		
		if(history.size() > 2){ 
			last3Move = history.get(history.size()-3);
			if(field[last3Move.x][last3Move.y] == BattleShips.State.SHIP_HITTED ) last3Hit=true;
		}
	
		
		
		if(lastHit && state==0 ){
			firstHitted=lastMove;
			System.out.println("-- FIRST HITTED: "+firstHitted.x+" "+firstHitted.y);
			while (state<5) {
				state++;
				int sx=lastMove.x+suggX[state-1];
				int sy=lastMove.y+suggY[state-1];
				if(  areValidCoord(  sx, sy)  ){
					System.out.println("--NON RANDOM: "+sx+" "+sy);
					m= new Shot(sx, sy);
					break;
				}
			}
			System.out.println("--NON RANDOM STATE: "+state	);

			
			//Shot( lastMove.x+suggX[0],  lastMove.y+suggY[0]);
		}else if(lastHit && (state>0) ){
			while (state<5) {
				int sx=lastMove.x+suggX[state-1];
				int sy=lastMove.y+suggY[state-1];
				if(  areValidCoord(  sx, sy)  ){
					System.out.println("--NON RANDOM 2: "+sx+" "+sy);
					m= new Shot(sx, sy);
					break;
				}
				state++;
			}
			
		}
		
		if(!lastHit && state == 1){
			System.out.println("--CAMBRIO DI ROTTA ORIZZONTALE!!!!");
			if(firstHitted != null){
					System.out.println("--FIRST HIT PRESENT!!!!");
					int sx=firstHitted.x+suggX[2];
					int sy=firstHitted.y+suggY[2];
					if(  areValidCoord(  sx, sy)  ){
						System.out.println("--NON RANDOM 3: "+sx+" "+sy);
						m= new Shot(sx, sy);
						state=3;
					}
				
			} 
		} else	if(!lastHit && state == 3){
			System.out.println("--CAMBRIO DI ROTTA VERTCALE!!!!");
			if(firstHitted != null){
					System.out.println("--FIRST HIT PRESENT!!!!");
					int sx=firstHitted.x+suggX[3];
					int sy=firstHitted.y+suggY[3];
					if(  areValidCoord(  sx, sy)  ){
						System.out.println("--NON RANDOM 3: "+sx+" "+sy);
						m= new Shot(sx, sy);
						state=4;
					}
				
			} 
		}	 
		
		
		if(m == null ){
			System.out.println("--RANDOM MOVE");
			m = randomShotSugggest(bs);
			state=0;
			isNotRandomTry=false;
			iHaveChangedDirection=false;
			iKnowShipOrientation=false;
			tryDirection=0;
			firstHitted=null;
		}
		
		history.add( m);	

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
