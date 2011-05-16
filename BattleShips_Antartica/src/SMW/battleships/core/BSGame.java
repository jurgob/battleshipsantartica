package SMW.battleships.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import SMW.battleships.core.BattleShips.GameStatus;
import SMW.battleships.core.BattleShips.Player;
import android.util.Log;



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
			Log.i("game", "Player "+p+"start ships dispose");
			
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			p.addShip();
			
			
			waitTShipsDisposition.countDown();
			//disposeFinished=true;
			Log.i("game","DISPOSITION FINISHED by Player "+p);
//			super.run();
			
		}
		
	}
	
	
	public BSGame(BattleShips bs){
		this.bs=bs;
		players = new ArrayList<BSPlayer>() {
			private static final long serialVersionUID = 1L;
		};	
	}
	

	
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
					
				bs.status= GameStatus.DISPLACE;
				
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
				bs.status= GameStatus.CONFLICT;
				gameOn=true;
				Log.i("game","GAME STARTED");
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
				e.printStackTrace();
			}
			
			current=(current+1 )%n;
			

		}
		Log.i("game","GAME OVER!!");
	}
}
