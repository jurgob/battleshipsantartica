package SMW.battleships.core;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import android.util.Log;

import SMW.battleships.GameView_OLD;


/**
 * 
 * 
 * */


public class BattleShips extends Observable {
	public class InvalidMoveException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InvalidMoveException() {
			super("Invalid Move, point already hitted");
		}
	}
	
	private void setSizeShipToDisplace(Player p, int size){
		if(p==Player.ONE) sizeShipToDisplaceP1=size;
		if(p==Player.TWO) sizeShipToDisplaceP2=size;
	}
	
	public int getSizeShipToDisplace(Player p){
		if(p==Player.ONE) return sizeShipToDisplaceP1;
		if(p==Player.TWO) return  sizeShipToDisplaceP2;
		return 0;	
	}
	
	private int sizeShipToDisplaceP1;
	private int sizeShipToDisplaceP2;
	/**
	 * Ship rappresenta una nave all'interno del gioco
	 * 
	 */
	public class Ship {
		int size;

		public Ship(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
	}

	class Score {
		private int player1hits;
		private int player2hits;

		public int getScore(Player p) {
			int tmp = 0;
			if (p == Player.ONE)
				tmp = player1hits;
			if (p == Player.TWO)
				tmp = player2hits;
			return tmp;
		}

		public void increaseScore(Player p) {
			if (p == Player.ONE)
				player1hits++;
			if (p == Player.TWO)
				player2hits++;

		}

	}

	
	
	
	private List<Ship> shipsP1;
	private List<Ship> shipsP2;
	public enum GameStatus{
		DISPLACE, CONFLICT, ENDED 
		
	}
	
	
	public enum State {
		SEA, SHIP, SEA_HITTED, SHIP_HITTED
	}

	public enum Player {
		ONE, TWO
	}

	public enum InsertOrientation {
		HORIZONTAL, VERTICAL
	}

	
	
	public static abstract class Move {
		public final int x, y;

		public Move(int x, int y) {
			this.x = x;
			this.y = y;

		}

		

	}// end of class Move

	public static  class Shot extends Move {
		
		public Shot(int x, int y) {
			super(x,y);
		}

		public boolean equals(Shot v) {
			if (this.x == v.x && this.y == v.y)
				return true;
			return false;
		}

	}// end of class Move
	
	public static class DisposeShip extends Move {
		public Player p;
		public final InsertOrientation o;
		
		public DisposeShip(int x, int y, InsertOrientation o, Player p) {
			super(x,y);
			this.p=p;
			this.o=o;
			

		}
		public boolean equals(DisposeShip m) {
			if (this.x == m.x && this.y == m.y && this.p==m.p && m.o==m.o)
				return true;
			return false;
		}

	}// end of class Move
	
	
	
	
	
	
	public static class MoveDisposeShip {
		public final int x, y;

		public MoveDisposeShip(int x, int y, int size, InsertOrientation o) {
			this.x = x;
			this.y = y;
		}
		

	}// end of class Move
	
	public GameStatus status=null;
	private State player1Field[][];
	private State player2Field[][];
	private int rows;
	private int columns;
	private int toHit;
	private Player currentPlayer = null;
	private Player currentEnemy=null;
	
	private List<Ship> turnMyShips = null;
	private State turnMyField[][] = null;
	private State turnEnemyField[][] = null;
	public Score score;

	public BattleShips(int rows, int columns) {
		score = new Score();
		
		shipsP1 = new ArrayList<Ship>();
		shipsP1.add(new Ship(5));
		shipsP1.add(new Ship(4));
		shipsP1.add(new Ship(3));
		shipsP1.add(new Ship(3));
		shipsP1.add(new Ship(2));
		shipsP1.add(new Ship(2));

		shipsP2 = new ArrayList<Ship>();
		shipsP2.add(new Ship(5));
		shipsP2.add(new Ship(4));
		shipsP2.add(new Ship(3));
		shipsP2.add(new Ship(3));
		shipsP2.add(new Ship(2));
		shipsP2.add(new Ship(2));

		setSizeShipToDisplace(Player.ONE, shipsP1.get(shipsP1.size()-1).getSize());
		setSizeShipToDisplace(Player.TWO, shipsP2.get(shipsP2.size()-1).getSize());

		
		Iterator<Ship> itr = shipsP1.iterator();
		while (itr.hasNext()) {
			toHit += itr.next().getSize();

		}

		this.rows = rows;
		this.columns = columns;
		player1Field = new State[rows][columns];
		player2Field = new State[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				player1Field[i][j] = State.SEA;
				player2Field[i][j] = State.SEA;
			}
		}
		//currentPlayer = Player.TWO;
		changeTurn(Player.ONE);
	}

	
	/**
	 * 
	 * 
	 * @param shot
	 * @throws Exception
	 * 
	 * show accetta un parametro di tipo Move, rappresenta uno sparo all'interno del gioco
	 * 
	 */
	public void shot(BattleShips.Move shot) throws Exception {
		System.out.println("shot by " + currentPlayer+"on x: "+shot.x+"  y: "+shot.y);
		synchronized (this) {

//			try{
				State result=null;
				if (shot.x > this.columns || shot.y > rows)
					throw new InvalidMoveException();
				if (turnEnemyField[shot.x][shot.y] == State.SEA)
					result = State.SEA_HITTED;
				if (turnEnemyField[shot.x][shot.y] == State.SHIP) {
					result = State.SHIP_HITTED;
					score.increaseScore(currentPlayer);
				}
				if (turnEnemyField[shot.x][shot.y] == State.SEA_HITTED)
					throw new InvalidMoveException();
				if (turnEnemyField[shot.x][shot.y] == State.SHIP_HITTED)
					throw new InvalidMoveException();
				turnEnemyField[shot.x][shot.y]=result;
//				}catch (InvalidMoveException e) {
//					System.out.println("invalid move or already selected, chose another one");
//					return;
//				}
			changeTurn(currentEnemy);
		}
		inform(null);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param currentInsertOrientation
	 * @param p
	 *
	 * disposeShip consente di disporre una nave all'interno del proprio campo di battaglia
	 */
	
	public void disposeShip(DisposeShip m){
		disposeShip(m.x,m.y,m.o,m.p);
	}
	
	public void disposeShip(int x, int y, InsertOrientation o, Player p) {

		System.out.println("dispose ship: "+   x+" "+y+" "+" "+o.toString()+" "+p.toString());
		synchronized (this) {
			List<Ship>  ships=null;
			State[][] field=null;
			if(p==Player.ONE){
				ships=shipsP1;
				field=player1Field;
			}
			if(p==Player.TWO){
				ships=shipsP2;
				field=player2Field;
			}
			
			if (x <= getXSize() && y <= getYSize() && ships.size() > 0) {
				int shipIndex = ships.size() - 1;
				int size = ships.get(shipIndex).getSize();
				//setSizeShipToDisplace(p, ships.get(ship+1).getSize());
				Log.d("BS", "size ship to displace: "+size);
				
				for (int i = 0; i < size; i++) {
					// TODO several checks
					Log.d("BS", "dispose Ship x: "+x+"y: "+y);
					field[x][y] = State.SHIP;
					if (o == InsertOrientation.VERTICAL)
						y ++;
					if (o == InsertOrientation.HORIZONTAL)
						x++;
				}

				ships.remove(shipIndex);
				if(shipIndex>0)   setSizeShipToDisplace(p, ships.get(ships.size()-1).getSize());

//				setSizeShipToDisplace(p, size);
				//changeTurn(currentEnemy);
			}
		}
		inform(null);

	}

	/*
	* questo metodo fà parte del protocollo Observer -  Observable, viene usato per notificare agli Osservatori un 
	* cambiamento di stato all'interno dell'oggetto BattleShips
	*/
	private void inform(Object info) {
		setChanged(); // Protocollo di Observable
		notifyObservers(info);
		try { // Persistenza

			Thread.sleep(1000); // msec

		} catch (InterruptedException ie) {
		}

	}

	/**
	 * torna il numero di righe del campo di battaglia
	 * @return
	 */
	public int getXSize() {
		return this.columns;
	}
	
	/**
	 * torna il numero di colonne del campo di battaglia
	 * 
	 * @return
	 */
	public int getYSize() {
		return this.rows;

	}
	
	
	/**
	 * Ritorna il campo del giocatore p
	 * 
	 * @param p
	 * @return
	 */
	public State[][] getField(Player p){
		State[][] field=null;
		if(p==Player.ONE)field=player1Field;
		if(p==Player.TWO)field=player2Field;
		
		return field;
	}
	
	/**
	 * ritorna il giocatore al quale è assegnato il turno
	 */
	
	public Player getCurrenPlayer(){
		return currentPlayer;
	}
	/**
	 * ritorna il numero di navi che devono ancora essere inserite all'interno del campo di battaglia
	 * @return
	 */
	
	public int shipsToInsert() {

		return turnMyShips.size();
	}

	/**
	 * ritorna il campo di battaglia del giocatore al quale è assegnato il turno corrente
	 * @return
	 */
	public State[][] getMyField() {

		return turnMyField;

	}

	/*
	 * metodo interno che incapsula l'azione di cambio turno, 
	 * changeTUrn(p), passa il turno al giocatore p
	 */
	private void changeTurn(Player p) {
		if (p == Player.TWO) {
			currentPlayer = Player.TWO;
			turnMyField = player2Field;
			turnEnemyField = player1Field;
			turnMyShips = shipsP2;
			currentEnemy=Player.ONE;
		} else {
			currentPlayer = Player.ONE;
			currentEnemy = Player.TWO;
			turnMyField = player1Field;
			turnEnemyField = player2Field;
			turnMyShips = shipsP1;
		}	
		System.out.println("change TURN, act p: "+ currentPlayer);
	}

	
	
	public synchronized boolean conflictOn(){
		boolean tmp=false;
		if(shipsP1.size()==0 && shipsP2.size()==0) tmp=true;
		
		return tmp;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public synchronized boolean over() {
		// TODO to complete
		if (score.getScore(Player.ONE) == toHit
				|| score.getScore(Player.TWO) == toHit)
			return true;
		else
			return false;
		// return false;
	}


	public static Player getEnemy(Player p) {
		Player enemy=null;
		if(p==Player.ONE)enemy=Player.TWO;
		if(p==Player.TWO)enemy=Player.ONE;
		
		return enemy;
	}
	public int getScore(Player p){
		return score.getScore(p);
		
		
	}
	
	
}
