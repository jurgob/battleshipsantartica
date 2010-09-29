package SMW.battleships;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import SMW.battleships.core.OptionValues;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.network.BSClient;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ActivityGameNetClient extends Activity {
	BSView view;
	BSClient client;
	ConnectToServerView connectView;
	LinearLayout gameLayout;
	private class ModifyMessage extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey("status")) {
				String value = bundle.getString("status");
				// view.setText(value);
				connectView.setMsgLabel(value);
				try {
					wait(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//gameLayout.addView(view, 0);
				
			}
		}
	}	

	
	
	
	private class ConnectToServer extends Thread {
		Handler handler;
		
		
		public ConnectToServer(Handler handler) {
			this.handler=handler;
		}
		
		@Override
		public void run() {
			
			

			try {
				Log.i("net", "Try to connect with server");
				client.startSession("10.0.2.2");
				
				Log.i("net", "Conneted with server");
				view.setPlayer(Player.ONE);
			} catch (ConnectException e) {
				finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = handler.obtainMessage();
		    Bundle b = new Bundle();
		    b.putString("status", "client connected");
		    msg.setData(b);
		    handler.sendMessage(msg);
			
		}

	}

	private class ConnectToServerView extends LinearLayout {
		TextView label;
		public ConnectToServerView(Context context) {
			super(context);
			this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			this.setGravity(Gravity.CENTER);
			this.setOrientation(HORIZONTAL);

			 label = new TextView(context);
			Button connect = new Button(context);
			connect.setText("Connect");

			connect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ConnectToServer cts = new ConnectToServer(new ModifyMessage());
					cts.start();
				}
			});
			addView(label);
			addView(connect);
		}
		public void setMsgLabel(String msg) {
			this.label.setText(msg);
			
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_net_client_activity);
		 gameLayout = (LinearLayout) findViewById(R.id.game_layout);
		view = new BSView(this);
		client = new BSClient(view);
		connectView= new ConnectToServerView(this);
		gameLayout.addView(connectView,0);

	}

}
