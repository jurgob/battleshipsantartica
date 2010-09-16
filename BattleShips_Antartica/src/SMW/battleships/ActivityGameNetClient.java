package SMW.battleships;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import SMW.battleships.core.BSGame;
import SMW.battleships.core.BSPlayer;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.network.BSClient;
import SMW.battleships.core.network.RemoteBSUser;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;



public class ActivityGameNetClient extends Activity {
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
    
     
    
  OptionValues.setRows(9);
  OptionValues.setColumns(9);
  BattleShips bs = new BattleShips(OptionValues.getRows(),OptionValues.getColumns());
  		
  		
  		
  		
  		
      
  		
  		
 			//user 2
 		
  		final BSView view= new BSView(this);
  		
  		
  		view.setModel(bs);
  		
  		
  		Socket socket =new Socket( );
  		BSUser remoteUser=new RemoteBSUser(socket);

  		BSClient client = new BSClient(remoteUser);
		try {
			client.startSession("localhost");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

  		BSPlayer player1 = new BSPlayer( bs, view.userStrategy()  );  // Istanziazione giocatori
		
		
 		
 		BSPlayer player2 = new BSPlayer( bs, remoteUser.userStrategy() );
	    
	    BSGame game = new BSGame( bs );                    // Configurazione gioco
	    game.addPlayer( player1 );
	    game.addPlayer( player2 );
	    game.start();     
	
	}
     
      
}
