package SMW.battleships.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Shot;

public class DummyStrategy implements BSStrategy{
	List<Shot> history= new ArrayList<Shot>();
	
	@Override
	public  Shot suggest(BattleShips bs) {
		
			boolean originalMove=false;
			int x=0;
			int y=0;
			Shot m =null;
			Random r= new Random(19580427);
			
			while(!originalMove){
//				x = ((int)Math.random()*bs.getXSize()*10 )%bs.getXSize();
//				y = ((int)Math.random()*bs.getYSize()*10 )%bs.getYSize();
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
					if(move.x==x || move.y==y){
						alreadyUsed=true;
					}	
				}
				if(!alreadyUsed) originalMove=true;
			
			}
			
			history.add( m);	
			return m;
	}

	@Override
	public DisposeShip suggestDisposeShip(BattleShips bs) {
		// TODO Auto-generated method stub
		return null;
	}

}
