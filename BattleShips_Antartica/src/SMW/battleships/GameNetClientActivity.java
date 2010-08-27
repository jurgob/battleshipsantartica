package SMW.battleships;

import SMW.battleships.core.BSGame;
import SMW.battleships.core.BSPlayer;
import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.DummyStrategy;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.SequentialStrategy;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Player;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class GameNetClientActivity extends Activity {
	//final GameView  game=new GameView(this);
//	private class UserStrategy  implements BSStrategy,  OnTouchListener  {
//
//		@Override
//		public synchronized Move  suggest(BattleShips bs) {
//			return new Move(game.getSelectedX(), game.getSelectedY());
//		}
//
//		@Override
//		public boolean  onTouch(View v, MotionEvent event) {
//			// TODO Auto-generated method stub
//			game.onTouchEvent(event);
//			return false;
//			
//		}
//		
//		
//	}
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
     setContentView(R.layout.game_net_client_activity);
     LinearLayout gameLayout=(LinearLayout)findViewById(R.id.game_layout);       
     //TODO set values trought an activity
//     OptionValues.setRows(9);
//     OptionValues.setColumns(9);
//     BattleShips bs = new BattleShips(OptionValues.getRows(),OptionValues.getColumns());
//     final BSView  gameView=new BSView(this);
//     gameView.setModel(bs);
//     BSPlayer human= new BSPlayer(bs,gameView.userStrategy() );
//     BSPlayer computer = new BSPlayer(bs, new SequentialStrategy());
//     BSGame game = new BSGame(bs);
//     game.addPlayer(human);
//     gameView.setPlayer(human.player);
//     game.addPlayer(computer);
//     gameLayout.addView(gameView, 0); 
//     System.out.println("PLAYERS");
//     System.out.println(human.player);
//     System.out.println(gameView.ME);
//     System.out.println(gameView.ENEMY);
//     System.out.println(computer.player);
//     
//     game.start();
      
	}
}
