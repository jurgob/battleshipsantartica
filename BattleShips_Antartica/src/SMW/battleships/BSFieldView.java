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
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TableLayout;

public class BSFieldView extends View {
	private Paint paintSea;
	private Paint paintShip;
	private Paint paintSeaHitted;
	private Paint paintShipHitted;
	private Paint paintSelect;
	Canvas c;
	public int oTiles;
	public int vTiles;
	int selected;
	int selectedX;
	int selectedY;
	// State field[][];
	public BattleShips bs;

	Context context;
	Player playerToShow=null;
	Player ME;
	Player ENEMY;


	public BSFieldView(Context context) {
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
	}



	@Override
	protected void onDraw(Canvas canvas) {
		if(playerToShow!=null) paintCampo(canvas);
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
//			if (i == selected) {
//				p = paintSelect;
//			} else {
				if (field[x][y] == State.SEA)
					p = paintSea;
				if (field[x][y] == State.SHIP)
					p = paintShip;
				if (field[x][y] == State.SEA_HITTED)
					p = paintSeaHitted;
				if (field[x][y] == State.SHIP_HITTED)
					p = paintShipHitted;
//			}
			c.drawRect(x * w + b, y * h + b, x * w + w - b, y * h + h - b, p);
		}
		this.c = c;
		
	}

	public int getSelectedX() {
		return selectedX;
	}

	public int getSelectedY() {
		return selectedY;
	}

	
	
	public Player getPlayerToShow() {
		return playerToShow;
	}

	public void setPlayerToShow(Player playerToShow) {
		this.playerToShow = playerToShow;
	}
	public void setPlayer(Player p) {
		ME=p;
		ENEMY=BattleShips.getEnemy(p);
		setPlayerToShow(ENEMY);
	}



	

}
