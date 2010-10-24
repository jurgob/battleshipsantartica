package SMW.battleships;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.BattleShips.Shot;
import SMW.battleships.core.BattleShips.State;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class GameView_OLD extends View implements BSUser {
	private Paint paintSea;
	private Paint paintShip;
	private Paint paintSeaHitted;
	private Paint paintShipHitted;
	private Paint paintSelect;
	private BSStrategy strategy = null;
	Canvas c;
	int oTiles;
	int vTiles;
	int selected;
	int selectedX;
	int selectedY;
	// State field[][];
	private BattleShips bs;

	Context context;
	Player playerToShow=null;
	Player ME;
	Player ENEMY;
	
	TextView myScore;
	TextView enemyScore;
	private Handler invHandler;
	
	
//	private List<BattleShips.DisposeShip> disposeShipMoves;

	
	private class Invalidate extends Handler{
		public void handleMessage(Message msg) {
			invalidate();
		}
		
	}
	
	
	private class UserStrategy implements BSStrategy, OnTouchListener {
		
		@Override
		public synchronized Shot suggest(BattleShips bs) {
			System.out.println("wait player suggest");
			setOnTouchListener(this);
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
			setOnTouchListener(null);
			System.out.println("end player suggest");

			return new Shot(getSelectedX(), getSelectedY());
		}

		@Override
		public synchronized boolean onTouch(View v, MotionEvent event) {
			onTouchEvent(event);
			if(playerToShow != ME){
				float x = event.getX();
				float y = event.getY();
				float h = getHeight() / vTiles;
				float w = getWidth() / oTiles;
				int sx = (int) (x / w);
				int sy = (int) (y / h);
				if(bs.getField(playerToShow)[sx][sy]!=State.SEA_HITTED && bs.getField(playerToShow)[sx][sy]!=State.SHIP_HITTED ){
					System.out.println("SELEZIONE VALIDA");
					
					selectTile(event);
					notify();
				}
				}
			
			
			//invalidate();
			return true;
			// return false;
		}

		@Override
		public DisposeShip suggestDisposeShip(BattleShips bs) {
		
//			int location=disposeShipMoves.size()-1;
//			DisposeShip m=disposeShipMoves.get(location);
//			disposeShipMoves.remove(location);
//			return m;			
			System.out.println("wait player suggest");
			setOnTouchListener(this);
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
			setOnTouchListener(null);
			System.out.println("end player suggest");

			return new BattleShips.DisposeShip(getSelectedX(), getSelectedY(), InsertOrientation.HORIZONTAL, ME);
			
		}
	}

	public GameView_OLD(Context context) {
		super(context);
		
		this.context=context;
		this.c = new Canvas();
		paintSea = new Paint();
		paintSea.setColor(Color.argb(255, 0, 0, 255));
		paintSea.setTextSize(25);
		paintSea.setAntiAlias(true);
		paintShip = new Paint();
		paintShip.setColor(Color.argb(255, 150, 150, 150));
		paintShip.setTextSize(25);
		paintShip.setAntiAlias(true);
		paintSeaHitted = new Paint();
		paintSeaHitted.setColor(Color.argb(255, 0, 0, 150));
		paintSeaHitted.setTextSize(25);
		paintSeaHitted.setAntiAlias(true);
		paintShipHitted = new Paint();
		paintShipHitted.setColor(Color.argb(255, 200, 200, 200));
		paintShipHitted.setTextSize(25);
		paintShipHitted.setAntiAlias(true);
		paintSelect = new Paint();
		paintSelect.setColor(Color.argb(255, 255, 255, 255));
		oTiles = OptionValues.getColumns();
		vTiles = OptionValues.getRows();
		selected = oTiles * vTiles + 1;
		//playerToShow=ENEMY;
		
		invHandler = new Invalidate();
		
		//TODO:delete this, use GUI
//		disposeShipMoves=new ArrayList<BattleShips.DisposeShip>();
//		disposeShipMoves.add(new DisposeShip(0,1, InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1,2, InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(2,3, InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1,5, InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1,7, InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(7,0, InsertOrientation.VERTICAL, ME));
		
		
		//end to-delete section
		
	}



	@Override
	protected void onDraw(Canvas canvas) {
		if(playerToShow!=null) paintCampo(canvas);
	
		//TextView myScore = (TextView) findViewById(R.id.score_value);
		if(myScore!= null){
			myScore.setText("hot");
			System.out.println("print result");
		}
		
		
		//invalidate();
	}

	
	public void selectTile(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		float h = getHeight() / vTiles;
		float w = getWidth() / oTiles;
		selectedX = (int) (x / w);
		selectedY = (int) (y / h);
		this.selected = selectedY * oTiles + selectedX;
		System.out.println("selected: " + getSelectedX() + " "
				+ getSelectedY());
	}

	protected void paintCampo(Canvas c) {
		//System.out.println("paint Field ");
		int b = 1;
		State[][] field = bs.getField(playerToShow);
		float h = getHeight() / vTiles;
		float w = getWidth() / oTiles;
		for (int i = 0; i < vTiles * oTiles; i++) {
			int x = (i % oTiles);
			int y = (Math.round(i / oTiles));
			Paint p = null;
			if (i == selected && playerToShow != ME) {
				p = paintSelect;
			} else {
				if (field[x][y] == State.SEA)
					p = paintSea;
				if (field[x][y] == State.SHIP)
					p = paintShip;
				if (field[x][y] == State.SEA_HITTED)
					p = paintSeaHitted;
				if (field[x][y] == State.SHIP_HITTED)
					p = paintShipHitted;
			}
			c.drawRect(x * w + b, y * h + b, x * w + w - b, y * h + h - b, p);
			// c.drawRect(x*w +b , y*h +b, x*w+w-b , y*h+h-b, paintSea);
		}
		this.c = c;
		
	}

	public int getSelectedX() {
		return selectedX;
	}

	public int getSelectedY() {
		return selectedY;
	}

	@Override
	public void setModel(BattleShips bs) {
		this.bs = bs;
		bs.addObserver(this);

	}

	@Override
	public BSStrategy userStrategy() {
		if (strategy == null) {
			strategy = new UserStrategy();
		}
		return strategy;
	}

	
	public Player getPlayerToShow() {
		return playerToShow;
	}

	public void setPlayerToShow(Player playerToShow) {
		this.playerToShow = playerToShow;
	}
	@Override
	public void update(Observable observable, Object data) {
		System.out.println("Update view model");
		if(bs.over()){
			 Intent myIntent = new Intent(context, ActivityGameOver.class);
	    	 context.startActivity(myIntent);
	    	 
			 //GameActivity.this.startActivity(myIntent);
			
			//Intent myIntent = new Inte
			 //Intent myIntent = new Intent();
	    	 //this.startActivity(myIntent);
		}else{
			//TextView myScore = (TextView) findViewById(R.id.score_value);
			//TextView enemyScore = (TextView)findViewById(R.id.opponents_score_value);
			//myScore.setText(""+bs.getScore(ME));
			
			//Intent repaintScore = new In
			//System.out.println(bs.getScore(ME));
			
//			enemyScore.setText(bs.getScore(ENEMY));
			
			
		}
		invHandler.sendMessage(null);
		//invalidate();
		// field=((BattleShips)observable).getMyField();
	}

public void setScoreLabels(TextView my, TextView enemy){
	this.myScore=my;
	this.enemyScore=enemy;
	
}

	@Override
	public void setPlayer(Player p) {
		ME=p;
		ENEMY=BattleShips.getEnemy(p);
		setPlayerToShow(ENEMY);
		// TODO Auto-generated method stub
		
	}

}
