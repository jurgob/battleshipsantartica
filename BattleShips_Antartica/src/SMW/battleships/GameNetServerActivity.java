package SMW.battleships;

import java.io.IOException;
import java.net.SocketTimeoutException;

import SMW.battleships.core.BSGame;
import SMW.battleships.core.BSPlayer;
import SMW.battleships.core.BSStrategy;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.DummyStrategy;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.SequentialStrategy;
import SMW.battleships.core.BattleShips.InsertOrientation;
import SMW.battleships.core.BattleShips.Move;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.network.BSServer;
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



public class GameNetServerActivity extends Activity {
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
     setContentView(R.layout.game_net_server_activity);
     LinearLayout gameLayout=(LinearLayout)findViewById(R.id.game_layout);       
     
     
     //settings.serverUp( server );
     
     //clearMonitor();
     //display( "started server at " + InetAddress.getLocalHost() );
     
//     serverAddress.setText( "localhost" );
//     serverStarter.setEnabled( false );
//     clientStarter.setEnabled( false );
//     serverAddress.setEditable( false );
     //busy();
     
//     //TODO set values trought an activity
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
     BSServer  server = null;
     try {
 		server = new BSServer();
 	} catch (IOException e) {
 		// TODO Auto-generated catch block
 		e.printStackTrace();
 	}
    
    OptionValues.setRows(9);
  OptionValues.setColumns(9);
  BattleShips bs = new BattleShips(OptionValues.getRows(),OptionValues.getColumns());
  		BSUser user1 = null;
		try {
			user1 = server.getBSUser();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		user1.setModel(bs);
  		BSStrategy strategy2= new SequentialStrategy(); 
 	
 		BSPlayer player1 = new BSPlayer( bs, user1.userStrategy() );  // Istanziazione giocatori
	    BSPlayer player2 = new BSPlayer( bs, strategy2 );
	    
	    BSGame game = new BSGame( bs );                    // Configurazione gioco
	    game.addPlayer( player1 );
	    game.addPlayer( player2 );
	    game.start();     
	
	}
}
