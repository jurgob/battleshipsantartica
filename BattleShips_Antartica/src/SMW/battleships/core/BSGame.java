package SMW.battleships.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Player;

public class BSGame extends Thread {

	private final BattleShips bs;
	private List<BSPlayer> players;
	private boolean gameOn=false;
	final CountDownLatch waitTShipsDisposition = new CountDownLatch(2);

	
	class AddShips extends Thread{
		BSPlayer p;
		boolean disposeFinished;
		
		public AddShips(BSPlayer p) {
			this.p=p;
			disposeFinished=false;
		}
		
		@Override
		public void run() {
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			waitTShipsDisposition.countDown();
			//disposeFinished=true;
			System.out.println("DISPOSITION FINISHED");
//			super.run();
			
		}
//		public synchronized boolean isDisposeFinished() {
//			return disposeFinished;
//		}
		
	}
	
	
	public BSGame(BattleShips bs){
		this.bs=bs;
		players = new ArrayList<BSPlayer>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		};	
	}
	
	
//	public void addShip(){
//		
//		
//	}
	
	public synchronized void addPlayer(BSPlayer player){
		if(!gameOn) players.add(player);
		if(players.size()==1)player.player=Player.ONE;
		if(players.size()==2)player.player=Player.TWO;
	}
	
	
	public void run(){
		int n;
		synchronized (this) {
			
			n=players.size();
			if(n==2 ){
				
				
//				players.get(0).addShip();
//				players.get(0).addShip();
//				players.get(0).addShip();
//				players.get(0).addShip();
//				players.get(0).addShip();
//				players.get(0).addShip();
//				
//				
//				players.get(1).addShip();
//				players.get(1).addShip();
//				players.get(1).addShip();
//				players.get(1).addShip();
//				players.get(1).addShip();
//				players.get(1).addShip();
				
				
				AddShips p1= new AddShips(players.get(0));
				AddShips p2= new AddShips(players.get(1));
				
				p1.start();
				p2.start();
				try {
					waitTShipsDisposition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
//			    bs.disposeShip(new DisposeShip(0,0, InsertOrientation.HORIZONTAL, Player.ONE));
//			    bs.disposeShip(new DisposeShip(0,1, InsertOrientation.HORIZONTAL, Player.ONE));
//			    bs.disposeShip(new DisposeShip(0,2, InsertOrientation.HORIZONTAL, Player.ONE));
//			    bs.disposeShip(new DisposeShip(0,3, InsertOrientation.HORIZONTAL, Player.ONE));
//			    bs.disposeShip(new DisposeShip(0,4, InsertOrientation.HORIZONTAL, Player.ONE));
//			    bs.disposeShip(new DisposeShip(0,5, InsertOrientation.HORIZONTAL, Player.ONE));
//			     
//			    bs.disposeShip(new DisposeShip(1,1, InsertOrientation.HORIZONTAL, Player.TWO));
//			    bs.disposeShip(new DisposeShip(1,2, InsertOrientation.HORIZONTAL, Player.TWO));
//			    bs.disposeShip(new DisposeShip(1,3, InsertOrientation.HORIZONTAL, Player.TWO));
//			    bs.disposeShip(new DisposeShip(1,5, InsertOrientation.HORIZONTAL, Player.TWO));
//			    bs.disposeShip(new DisposeShip(1,6, InsertOrientation.HORIZONTAL, Player.TWO));
//			    bs.disposeShip(new DisposeShip(8,0, InsertOrientation.VERTICAL, Player.TWO));
//			     
				
				
				gameOn=true;
				System.out.println("GAME STARTED");
			}else{
				return;
			}
		}//end sync
		int current=0;
		while(!bs.over()){
			try {
				(players.get(current)).play();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			current=(current+1 )%n;
			

		}
		System.out.println("GAME OVER!!");
	}
}
