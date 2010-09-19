package SMW.battleships;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

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
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ActivityGameNetServer extends Activity {
	private class ViewWaitClientConnection extends LinearLayout {
		TextView yourIp,waitClient;
		public ViewWaitClientConnection(Context context) {
			super(context);
			this.setOrientation(VERTICAL);
			this.setGravity(Gravity.CENTER);
			this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		    yourIp = new TextView(context);
			waitClient= new TextView(context);
			yourIp.setText("Your ip: " + this.getLocalIpAddress());
			this.addView(yourIp);
			this.addView(waitClient);
			
			Button back = new Button(context);
			back.setText("Back");
			back.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					wc.interrupt();
				    ActivityGameNetServer.this.finish();
				}
			});
			this.addView(back);
		}
		public void setMsgLabel(String text){
			this.waitClient.setText(text);
			
		}
		private String getLocalIpAddress() {
		    try {
		        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
		            NetworkInterface intf = en.nextElement();
		            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
		                InetAddress inetAddress = enumIpAddr.nextElement();
		                if (!inetAddress.isLoopbackAddress()) {
		                    return inetAddress.getHostAddress().toString();
		                }
		            }
		        }
		    } catch (SocketException ex) {
		        Log.e("net", ex.toString());
		    }
		    return null;
		}
		
	}

	
	BSUser remoteUser = null;
    BSServer  server = null;
    ViewWaitClientConnection waitView;
    BattleShips bs;
    WaitClient wc;
    
	private class WaitClient extends Thread{
		Context c;
		
		public WaitClient(Context c) {
			this.c=c;
		}
		
		@Override
		public void run() {
			try {
				Log.i("net", "waiting for player client connection");
				remoteUser = server.getBSUser();
				Log.i("net","player connected");
			} catch (SocketTimeoutException e) {
				try {
					Log.w("timeout", "rserver timeout");
					server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			waitView.setMsgLabel("client connected");
	  		//remoteUser.setModel(bs);
	  		BSPlayer player1 = new BSPlayer( bs, remoteUser.userStrategy() );  // Istanziazione giocatori
	 		//user 2
	 		final BSView view= new BSView(this.c);
	 		view.setModel(bs);
	 		BSPlayer player2 = new BSPlayer( bs, view.userStrategy() );
		    BSGame game = new BSGame( bs );                    // Configurazione gioco
		    game.addPlayer( player1 );
		    view.setPlayer(player2.player);
		    game.addPlayer( player2 );
		    
		    game.start(); 
		    
		}
		
		
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_net_server_activity);
		LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game_layout);

		try {
			server = new BSServer();
		} catch (Exception e) {
			// TODO: handle exception
		}

		OptionValues.setRows(9);
		OptionValues.setColumns(9);

		bs = new BattleShips(OptionValues.getRows(), OptionValues.getColumns());
		waitView = new ViewWaitClientConnection(this);
		waitView.setMsgLabel("waiting for a player client connection");
		gameLayout.addView(waitView, 0);

		wc = new WaitClient(this);
		wc.start();
		
		
		
		
	}
}
