package SMW.battleships;

import java.util.List;
import java.util.Observable;

import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.GameStatus;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.BattleShips.Shot;
import SMW.battleships.core.BattleShips.State;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BSView extends TableLayout implements BSUser {
	private BattleShips bs;

	Player playerToShow = null;
	int myScoreVal, enemyScoreVal;
	BSFieldView fieldView;
	private BSStrategy strategy = null;
	Player ME, ENEMY;
	TextView myScore, enemyScore, messageLabel;
	LinearLayout results;
	Canvas c;
	int totShips = 6;
	Invalidate invalidate;
	TextView currentField;
	BattleShips.InsertOrientation currentInsertOrientation;
	final Button changeOrientation ;

	private LinearLayout buttons;

	private class Invalidate extends Handler{
		public void handleMessage(Message msg) {
			try{
				System.out.println("buttons size: "+buttons.getChildCount());
				if(bs.conflictOn() && buttons.getChildCount()==4 )buttons.removeViewAt(0);
				Bundle b= msg.getData(); 
				if(b.getString("type").equals("up_score")){
					
					setStatus(b.getString("status"));
					setMyScore(b.getInt("myScore"));
					setEnemyScore(b.getInt("enemyScore"));
				}
				if(b.getString("type").equals("up_status")){
					setStatus(b.getString("status"));
					
				}
				invalidate();
			}finally{
				invalidate();
				
			}
		}
		
	}
		
	
	private class UserStrategy implements BSStrategy, OnTouchListener {

		private boolean isValidDisposeShipPosition(int x,int y, InsertOrientation o, Player p){
			int shipSize=bs.getSizeShipToDisplace(p);
			Log.d("UI", "size shop to dispose: "+ shipSize);
			
			//TODO: actually horizontal orientation is the only one permitted by UI
			if(o==InsertOrientation.HORIZONTAL && fieldView.oTiles-x< shipSize ) return false;
			if(o==InsertOrientation.VERTICAL&& fieldView.vTiles-y< shipSize ) return false;
			for (int i = 0; i < shipSize; i++) {
				int mx=x;
				int my=y;
				if(o==InsertOrientation.HORIZONTAL) mx= x+i;
				if(o==InsertOrientation.VERTICAL) my =y+i;
				Log.d("UI", "isValidPosition:"+x+" "+y+" size: "+shipSize +" field width: "+fieldView.oTiles + "field height: "+fieldView.vTiles);
				if(bs.getField(p)[mx][my]!= State.SEA  ) return false;
			}	
			
//			bs.getField(ME)[sx][sy] == State.SEA && sx < fieldView.oTiles-3
			return true;
			
		}

		@Override
		public synchronized boolean onTouch(View v, MotionEvent event) {
			onTouchEvent(event);
			float x = event.getX();
			float y = event.getY();
			float h = fieldView.getHeight() / fieldView.vTiles;
			float w = fieldView.getWidth() / fieldView.oTiles;
			int sx = (int) (x / w);
			int sy = (int) (y / h);
			
			if ( bs.status==GameStatus.CONFLICT &&  fieldView.getPlayerToShow() != ME) {
				if (bs.getField(fieldView.getPlayerToShow())[sx][sy] != State.SEA_HITTED
						&& bs.getField(playerToShow)[sx][sy] != State.SHIP_HITTED) {
					Log.i("view", "Good selection");
					fieldView.selectTile(event);
					notify();
				} else {
					Log.i("view", "Bad Selection:" + sx + " " + sy);
				}
			}
			
			if ( bs.status==GameStatus.DISPLACE &&  fieldView.getPlayerToShow() == ME) {
				Log.d("UI","Check if is valid dispose");
				if(  isValidDisposeShipPosition(sx,sy,currentInsertOrientation,fieldView.getPlayerToShow()) ){
					Log.d("UI","Move is valid !!");	
					fieldView.selectTile(event);
					notify();	
				}
			}
			// invalidate();
			return true;
		}

		@Override
		public synchronized Shot suggest(BattleShips bs) {
			Log.i("view","wait player suggest");
			fieldView.setOnTouchListener(this);
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
			fieldView.setOnTouchListener(null);
			Log.i("view","end player suggestion");
			Message m = new Message();
			Bundle b = new Bundle();
			b.putString("type", "refresh");
//			b.putString("status", "Enemy's turn");
			m.setData(b);
			invalidate.sendMessage(m);			
			return new Shot(fieldView.getSelectedX(), fieldView.getSelectedY());
		}
		
		@Override
		public synchronized  DisposeShip suggestDisposeShip(BattleShips bs) {

//			int location = disposeShipMoves.size() - 1;
//			DisposeShip m = disposeShipMoves.get(location);
//			disposeShipMoves.remove(location);
				
			System.out.println("wait player suggest");
			fieldView.setOnTouchListener(this);
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
			setOnTouchListener(null);
			System.out.println("end player suggest");

			
			return new DisposeShip(fieldView.getSelectedX(), fieldView.getSelectedY(), currentInsertOrientation, ME);
		}
	}

	private void paintChangeOrientationButton(){
		switch (currentInsertOrientation) {
		case VERTICAL:
			changeOrientation.setText("|");
			break;
		case HORIZONTAL:
			changeOrientation.setText("-");
			break;
		}
		
	}
	private void changeCurrentOrientation(){
		switch (currentInsertOrientation) {
		case VERTICAL:
			currentInsertOrientation=InsertOrientation.HORIZONTAL;
			break;
		case HORIZONTAL:
			currentInsertOrientation=InsertOrientation.VERTICAL;
			break;
		}
		
	}
	
	
	
	
	public BSView(Context context) {
		super(context);
		
		System.out.println("BSVIEW COSTRUCTOR");
		invalidate = new Invalidate();
		
		fieldView = new BSFieldView(context);

		final Button showMyField = new Button(context);
		//final Button showMyField = (Button)this.findViewById(R.id.my_field_button);
		final Button showEnemyField = new Button(context);
		//final Button showEnemyField = (Button)this.findViewById(R.id.enemy_field_button); 
		currentField = new TextView(context);
		currentField.setText(R.string.enemy_field);
		
		showMyField.setText("<<");
		showEnemyField.setText(">>");

		showMyField.setEnabled(true);
		showEnemyField.setEnabled(false);
		showMyField.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPlayerToShow(ME);
				showMyField.setEnabled(false);
				showEnemyField.setEnabled(true);
				currentField.setText(R.string.my_field);
				Message m = new Message();
				Bundle b = new Bundle();
				b.putString("type", "change_field");
				m.setData(b);
				invalidate.sendMessage(m);			}
		});
		showEnemyField.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPlayerToShow(ENEMY);
				showMyField.setEnabled(true);
				showEnemyField.setEnabled(false);
				currentField.setText(R.string.enemy_field);
				Message m = new Message();
				Bundle b = new Bundle();
				b.putString("type", "change_field");
				m.setData(b);
				invalidate.sendMessage(m);			}
		});
		changeOrientation = new Button(context);
		currentInsertOrientation = BattleShips.InsertOrientation.VERTICAL;
		paintChangeOrientationButton();
		changeOrientation.setOnClickListener(
				new OnClickListener() {
						//TODO: it is buggy
						@Override
						public void onClick(View v) {
							changeCurrentOrientation();
							paintChangeOrientationButton();
							Message m = new Message();
							Bundle b = new Bundle();
							b.putString("type", "refresh");
							Message msg = new Message();
							msg.setData(b);
							invalidate.sendMessage(msg);
					}
				}
		);

		
		c = new Canvas();
		myScore = new TextView(context);
		enemyScore = new TextView(context);
		messageLabel = new TextView(context);
		
		setMyScore(0);
		setEnemyScore(0);
		messageLabel.setText("Dispose your ships");
		results = new LinearLayout(context);
		results.addView(myScore);
		results.addView(enemyScore);
		TableRow statusBar = new TableRow(context);
		statusBar.addView(messageLabel);
		this.addView(results);
		buttons = new LinearLayout(context);
		buttons.addView(changeOrientation);
		buttons.addView(showMyField);
		buttons.addView(showEnemyField);
		buttons.addView(currentField);

		this.addView(buttons );
		
		this.addView(statusBar);

		this.addView(fieldView);
		int spacingH= 10;
		int spacingV= 2;
		results.setGravity(Gravity.LEFT);
		myScore.setGravity(Gravity.LEFT);
		myScore.setPadding(0, 0, spacingH, 0);
		enemyScore.setGravity(Gravity.LEFT);
		currentField.setTextSize(25);
		currentField.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		currentField.setGravity(Gravity.CENTER );
		
		//showMyField.setLayoutParams(new LayoutParams(new MarginLayoutParams(0, 0)));
		statusBar.setPadding(0, spacingV, 0, spacingV);
		
		this.setPadding(spacingH, spacingV, spacingH, spacingV);
		
		
		this.setWillNotDraw(false);
	

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//setMyScore(myScoreVal);
		//setEnemyScore(enemyScoreVal);
		
		//invalidate();
	}

	@Override
	public void setModel(BattleShips bs) {
		this.bs = bs;
		fieldView.bs = bs;
		bs.addObserver(this);
		// invalidate();
		Bundle b = new Bundle();
		b.putString("type", "refresh");
		Message msg = new Message();
		msg.setData(b);
		
		invalidate.sendMessage(msg);
	}
	@Override
	public BSStrategy userStrategy() {
		if (strategy == null) {
			strategy = new UserStrategy();
		}
		return strategy;
	}

	@Override
	public void update(Observable observable, Object data) {
		System.out.println("Update view model");
		Message msg = new Message();
		Bundle b = new Bundle();
		
		if (bs.over()) {
			Intent myIntent = new Intent(getContext(), ActivityGameOver.class);
			getContext().startActivity(myIntent);
		} else {
			if (bs.conflictOn() ){
					b.putString("type", "up_score");
					if(bs.getCurrenPlayer() == ME) b.putString("status", "Your Turn");
					if(bs.getCurrenPlayer() == ENEMY) b.putString("status", "Enemy's Turn");
					b.putInt("myScore", bs.getScore(ME) );
					b.putInt("enemyScore", bs.getScore(ENEMY));
				}else {
					b.putString("type", "up_status");
					b.putString("status", "Dispose your ship (ship size: "+bs.getSizeShipToDisplace(ME)+")"	);
			}	
			msg.setData(b);
			invalidate.sendMessage(msg);
		}
		
	}

	@Override
	public void setPlayer(Player p) {
		ME = p;
		ENEMY = BattleShips.getEnemy(p);
		setPlayerToShow(ENEMY);
		fieldView.setPlayer(p);

	}

	public void setPlayerToShow(Player playerToShow) {
		this.playerToShow = playerToShow;
		fieldView.setPlayerToShow(playerToShow);
	}

	public void setStatus(String msg){
		this.messageLabel.setText(msg);
		
	}
	
	public void setMyScore(int scr){
		this.myScore.setText("My Score: "+scr);
		
	}
	
	public void setEnemyScore(int scr){
		this.enemyScore.setText("Enemy Score: "+scr);
		
	}
	
	
}
