package SMW.battleships;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.BattleShips.DisposeShip;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.BattleShips.Shot;
import SMW.battleships.core.BattleShips.State;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
	TableRow results;
	Canvas c;

	Invalidate invalidate;
	
	private List<BattleShips.DisposeShip> disposeShipMoves;

	private class Invalidate extends Handler{
		public void handleMessage(Message msg) {
			invalidate();
		}
		
	}
		
	
	private class UserStrategy implements BSStrategy, OnTouchListener {

	

		@Override
		public synchronized boolean onTouch(View v, MotionEvent event) {
			onTouchEvent(event);
			float x = event.getX();
			float y = event.getY();
			float h = fieldView.getHeight() / fieldView.vTiles;
			float w = fieldView.getWidth() / fieldView.oTiles;
			int sx = (int) (x / w);
			int sy = (int) (y / h);
			
			if (fieldView.getPlayerToShow() != ME) {
				if (bs.getField(fieldView.getPlayerToShow())[sx][sy] != State.SEA_HITTED
						&& bs.getField(playerToShow)[sx][sy] != State.SHIP_HITTED) {
					Log.i("view", "Good selection");
					fieldView.selectTile(event);
					notify();
				} else {
					Log.i("view", "Bad Selection:" + sx + " " + sy);
				}
			}else{
				
				
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

			
			return new DisposeShip(fieldView.getSelectedX(), fieldView.getSelectedY(), InsertOrientation.HORIZONTAL, ME);
		}
	}

	public BSView(Context context) {
		super(context);
		invalidate = new Invalidate();
		
		fieldView = new BSFieldView(context);

		final Button showMyField = new Button(context);
		final Button showEnemyField = new Button(context);
		showMyField.setText(R.string.show_my_field);
		showEnemyField.setText(R.string.show_enemy_field);

		showMyField.setEnabled(true);
		showEnemyField.setEnabled(false);
		showMyField.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPlayerToShow(ME);
				showMyField.setEnabled(false);
				showEnemyField.setEnabled(true);
				// invalidate();
				invalidate.sendMessage(new Message());			}
		});
		showEnemyField.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPlayerToShow(ENEMY);
				showMyField.setEnabled(true);
				showEnemyField.setEnabled(false);
				// invalidate();
				invalidate.sendMessage(new Message());
			}
		});

		c = new Canvas();
		myScore = new TextView(context);
		enemyScore = new TextView(context);
		messageLabel = new TextView(context);
		myScore.setText("MyScore: 0");
		enemyScore.setText("Enemy Score: 0");
		messageLabel.setText("Dispose your ships");

		results = new TableRow(context);

		results.addView(myScore);
		results.addView(enemyScore);
		results.addView(messageLabel);

		this.addView(results);

		TableRow buttons = new TableRow(context);
		buttons.addView(showMyField);
		buttons.addView(showEnemyField);
		this.addView(buttons);

		this.addView(fieldView);
		this.setWillNotDraw(false);
		// this.

		// TODO:delete this, use GUI
//		disposeShipMoves = new ArrayList<BattleShips.DisposeShip>();
//		disposeShipMoves.add(new DisposeShip(0, 1,
//				InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1, 2,
//				InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(2, 3,
//				InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1, 5,
//				InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(1, 7,
//				InsertOrientation.HORIZONTAL, ME));
//		disposeShipMoves.add(new DisposeShip(7, 0, InsertOrientation.VERTICAL,ME));

		// end to-delete section

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		myScore.setText("My Score: " + myScoreVal);
		enemyScore.setText("Enemy Score: " + enemyScoreVal);
		//invalidate();

	}

	@Override
	public void setModel(BattleShips bs) {
		this.bs = bs;
		fieldView.bs = bs;
		bs.addObserver(this);
		// invalidate();
		invalidate.sendMessage(new Message());

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
		if (bs.over()) {
			Intent myIntent = new Intent(getContext(), ActivityGameOver.class);
			getContext().startActivity(myIntent);
			// GameActivity.this.startActivity(myIntent);

			// Intent myIntent = new Inte
			// Intent myIntent = new Intent();
			// this.startActivity(myIntent);
		} else {
			if (bs.conflictOn())
				//messageLabel.setText("Conflict On!");
			//myScoreVal = bs.getScore(ME);
			//enemyScoreVal = bs.getScore(ENEMY);
			System.out.println("NEW SCORE: " + myScoreVal);

		}
		// myScore.invalidate();
		// results.invalidate();
		// invalidate();
		// field=((BattleShips)observable).getMyField();
		invalidate.sendMessage(new Message());
		
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

}
