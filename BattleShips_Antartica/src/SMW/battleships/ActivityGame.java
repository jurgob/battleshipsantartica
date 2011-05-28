package SMW.battleships;

import SMW.battleships.core.BSGame;
import SMW.battleships.core.BSPlayer;
import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.DummyStrategy;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.SequentialStrategy;
import SMW.battleships.core.SmartStrategy;
import SMW.battleships.core.SoSmartStrategy;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.OptionValues.Difficult;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ActivityGame extends Activity {
	BSView  gameView;
	
	private void initUI(){
		 setContentView(R.layout.game_activity);
	     LinearLayout gameLayout=(LinearLayout)findViewById(R.id.game_layout);       
	     //TODO set values trought an activity
	     OptionValues.setRows(9);
	     OptionValues.setColumns(9);
	     BattleShips bs = new BattleShips(OptionValues.getRows(),OptionValues.getColumns());
	     gameView=new BSView(this);
	     gameView.setModel(bs);
	     BSPlayer human= new BSPlayer(bs,gameView.userStrategy() );
	     BSPlayer computer = null;
	     switch (OptionValues.difficult) {
		case VERYEASY:
			computer=new BSPlayer(bs, new SequentialStrategy());
			break;
		case EASY:
			computer=new BSPlayer(bs, new DummyStrategy());
			break;
		case NORMAL:
			computer=new BSPlayer(bs, new SmartStrategy());
			break;
		case HARD:
			computer=new BSPlayer(bs, new SoSmartStrategy());
			break;

		default:
			computer=new BSPlayer(bs, new SoSmartStrategy());
			break;
		}
	     BSGame game = new BSGame(bs);
	     game.addPlayer(human);
	     gameView.setPlayer(human.player);
	     game.addPlayer(computer);
	     gameLayout.addView(gameView, 0); 
	     game.start();
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    initUI();
      
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.game_activity);
	    initUI();

	    
	}

}
