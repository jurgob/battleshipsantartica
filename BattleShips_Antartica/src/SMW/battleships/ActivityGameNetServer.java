package SMW.battleships;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import SMW.battleships.core.BSGame;
import SMW.battleships.core.BSPlayer;
import SMW.battleships.core.BSUser;
import SMW.battleships.core.BattleShips;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.network.BSServer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ActivityGameNetServer extends Activity {
	BSView view;
	LinearLayout gameLayout;
	
	private class ModifyMessage extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey("status")) {
				String value = bundle.getString("status");
				// view.setText(value);
				waitView.setMsgLabel(value);
				try {
					wait(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				gameLayout.addView(view, 0);
			}
		}
	}	
	
	
	
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
		Handler handler;
		
		public WaitClient(Context c, Handler handler) {
			this.c=c;
			this.handler=handler;
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
			
			Message msg = handler.obtainMessage();
		    Bundle b = new Bundle();
		    b.putString("status", "client connected");
		    msg.setData(b);
		    handler.sendMessage(msg);
		    
			//waitView.setMsgLabel("client connected");
	  		//remoteUser.setModel(bs);
	  		BSPlayer player1 = new BSPlayer( bs, remoteUser.userStrategy() );  // Istanziazione giocatori
	 		//user 2
	 		 view= new BSView(this.c);
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
		 gameLayout = (LinearLayout) findViewById(R.id.game_layout);

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

		wc = new WaitClient(this, new ModifyMessage());
		wc.start();
		
		
		
		
	}
}
